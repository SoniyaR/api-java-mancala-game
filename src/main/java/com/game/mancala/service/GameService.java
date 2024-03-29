package com.game.mancala.service;

import com.game.mancala.exception.BusinessRuleException;
import com.game.mancala.model.Game;
import com.game.mancala.model.Player;
import com.game.mancala.model.PlayerGame;
import com.game.mancala.model.PlayerPitsData;
import com.game.mancala.model.api.CreateGameDto;
import com.game.mancala.model.dto.GameDetailsDto;
import com.game.mancala.repository.GamesRepository;
import com.game.mancala.repository.PlayerGamesRepository;
import com.game.mancala.repository.PlayerPitsRepository;
import com.game.mancala.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final PlayerRepository playerRepository;
    private final PlayerPitsRepository playerPitsRepository;
    private final GamesRepository gamesRepository;
    private final PlayerGamesRepository playerGamesRepository;

    private final Integer PITS_PER_PLAYER = 6;
    private final Integer PEBBLES_PER_PIT = 6;

    public GameDetailsDto createNewGame(CreateGameDto createGameDto) {

        if (createGameDto.getPlayerName().length % 2 == 1) {
            throw new BusinessRuleException("Can not create game...");
        }
        List<Player> players = new ArrayList<>();
        List.of(createGameDto.getPlayerName()).forEach(name -> {
            if (name.isBlank()) {
                throw new BusinessRuleException("Please enter valid player name...");
            }

            Optional<Player> playerOpt = playerRepository.findByName(name.trim());
            Player savedPlayer = playerOpt.orElseGet(() -> playerRepository.save(Player.builder().name(name.trim()).isMyTurn(false).build()));
            players.add(savedPlayer);
        });

        Game game = Game.builder().gameUuid(UUID.randomUUID()).startTime(Instant.now()).build();
        game = gamesRepository.save(game);
        List<PlayerGame> playerGameList = createPlayerGameMapping(game, players);
        generatePlayerPitsData(playerGameList);
        setTurnForFirstPlayer(players);
        return getGameDetails(game.getId());
    }

    private List<PlayerGame> createPlayerGameMapping(Game game, List<Player> players) {
        List<PlayerGame> list = players.stream().map(player -> PlayerGame.builder().player(player).game(game).build()).collect(Collectors.toList());
        return (List<PlayerGame>) playerGamesRepository.saveAll(list);
    }

    private void setTurnForFirstPlayer(List<Player> players) {
        List<Player> toUpdate = new ArrayList<>();
        IntStream.range(0, players.size()).forEach(i -> {
            if (i == 0) {
                toUpdate.add(players.get(i).toBuilder().isMyTurn(true).build());
            } else {
                toUpdate.add(players.get(i).toBuilder().isMyTurn(false).build());
            }
        });
        playerRepository.saveAll(toUpdate);
    }

    public GameDetailsDto makeMove(Long pitId, Long playerId) {
        Optional<PlayerPitsData> selectedPitData = playerPitsRepository.findById(pitId);
        if (selectedPitData.isEmpty()) throw new BusinessRuleException("Pit does not exists");

        PlayerPitsData selectedPit = selectedPitData.get();
        Player currentPlayer = selectedPit.getPlayerGame().getPlayer();
        boolean isPlayersTurn = currentPlayer.isMyTurn();
        if (!isPlayersTurn) {
            throw new BusinessRuleException("Wait for your turn...");
        }

        boolean isPitClickable = !selectedPit.isStore()
                && currentPlayer.getId().equals(playerId)
                && selectedPit.getPebblesCount() > 0;
        if (!isPitClickable) {
            throw new BusinessRuleException("Incorrect Pit, select different one.");
        }

        Game currentGame = selectedPit.getPlayerGame().getGame();
        List<PlayerPitsData> pitsData = distributePebbles(selectedPit, playerId, currentGame.getId());

        boolean isGameOver = isGameOver(pitsData);
        if (isGameOver) {
            finishTheGame(currentGame.getId());
        }
        return getGameDetails(currentGame.getId(), isGameOver);
    }

    private void generatePlayerPitsData(List<PlayerGame> playerGames) {
        int total_pits_per_player = PITS_PER_PLAYER + 1;
        List<PlayerPitsData> pitsData = new ArrayList<>();
        IntStream.range(0, playerGames.size() * total_pits_per_player).forEach(i -> {
            PlayerPitsData pitData = PlayerPitsData.builder()
                    .playerGame(playerGames.get(i / total_pits_per_player))
                    .sequence(i + 1)
                    .build();
            if ((i + 1) % (total_pits_per_player) == 0) {
                pitData = pitData.toBuilder().pebblesCount(0).isStore(true).build();
            } else {
                pitData = pitData.toBuilder().pebblesCount(PEBBLES_PER_PIT).isStore(false).build();
            }

            pitsData.add(pitData);
        });
        playerPitsRepository.saveAll(pitsData);
    }

    public GameDetailsDto getGameDetails(Long gameId) {
        List<PlayerPitsData> pits = playerPitsRepository.findByPlayerGameGameIdOrderBySequence(gameId);
        return GameDetailsDto.builder()
                .pitsData(pits.stream().map(PlayerPitsData::toDto).collect(Collectors.toList()))
                .build();
    }

    public GameDetailsDto getGameDetails(UUID gameId) {
        List<PlayerPitsData> pits = playerPitsRepository.findByPlayerGameGameGameUuidOrderBySequence(gameId);
        return GameDetailsDto.builder()
                .pitsData(pits.stream().map(PlayerPitsData::toDto).collect(Collectors.toList()))
                .build();
    }

    public GameDetailsDto getGameDetails(Long gameId, boolean isGameOver) {
        List<PlayerPitsData> pits = playerPitsRepository.findByPlayerGameGameIdOrderBySequence(gameId);
        return GameDetailsDto.builder()
                .pitsData(pits.stream().map(PlayerPitsData::toDto).collect(Collectors.toList()))
                .isGameOver(isGameOver)
                .winnerName(isGameOver ? getWinnerName(pits) : null)
                .build();
    }

    private String getWinnerName(List<PlayerPitsData> pits) {
        Optional<PlayerPitsData> winnerPit = pits.stream().filter(PlayerPitsData::isStore)
                .max(Comparator.comparing(PlayerPitsData::getPebblesCount));
        if (winnerPit.isEmpty()) {
            System.err.println("Winner could not be determined.");
            return null;
        }
        return winnerPit.get().getPlayerGame().getPlayer().getName();
    }

    public boolean isGameOver(List<PlayerPitsData> pits) {
        List<Player> playerList = getPlayersForCurrentGame(pits);

        for (Player player : playerList) {
            int result = pits.stream().filter(p -> p.getPlayerGame().getPlayer().getId().equals(player.getId()) && !p.isStore())
                    .map(PlayerPitsData::getPebblesCount).reduce(Integer::sum).orElse(-1);
            if (result == 0) {
                return true;
            }
        }
        return false;
    }

    private void finishTheGame(Long gameId) {
        List<PlayerPitsData> pitsData = playerPitsRepository.findByPlayerGameGameIdOrderBySequence(gameId);
        List<Player> playersForGame = getPlayersForCurrentGame(pitsData);
        playersForGame.forEach(player -> {
            List<PlayerPitsData> pitsToUpdate = pitsData.stream()
                    .filter(p -> p.getPlayerGame().getPlayer().getId().equals(player.getId()))
                    .toList();
            int stonesToAddStore = pitsToUpdate.stream()
                    .filter(pit -> !pit.isStore())
                    .map(PlayerPitsData::getPebblesCount)
                    .reduce(Integer::sum).orElse(0);

            pitsToUpdate.forEach(pit -> {
                if (pit.isStore()) {
                    pit.incrementPebbleCountBy(stonesToAddStore);
                } else {
                    pit.resetPebbleCountToZero();
                }
            });
            playerPitsRepository.saveAll(pitsToUpdate);
        });

        Game gameToUpdate = pitsData.get(0).getPlayerGame().getGame();
        gamesRepository.save(gameToUpdate.toBuilder().isGameOver(true).endTime(Instant.now()).build());
    }

    private List<PlayerPitsData> distributePebbles(PlayerPitsData selectedPit, Long playerId, Long gameId) {
        List<PlayerPitsData> pits = playerPitsRepository.findByPlayerGameGameIdOrderBySequence(gameId);
        int totalPits = pits.size();
        int pickedPebbles = selectedPit.getPebblesCount();
        int selectedPitSequence = selectedPit.getSequence();
        boolean switchPlayerTurn = true;

        pits.get(selectedPitSequence - 1).resetPebbleCountToZero();

        int startIndex = selectedPitSequence;
        for (int i = 0; i < pickedPebbles; i++) {
            PlayerPitsData currentPit = pits.get((startIndex + i) % totalPits);
            boolean isLastPebble = (i == pickedPebbles - 1);
            boolean shouldAddPebble = true;

            if (!currentPit.getPlayerGame().getPlayer().getId().equals(playerId) && currentPit.isStore()) {
                startIndex += 1;
                currentPit = pits.get((startIndex + i) % totalPits);
                currentPit.incrementPebbleCount();
            } else {
                if (isLastPebble) {
                    if (currentPit.getPebblesCount() == 0 && !currentPit.isStore()) {
                        captureOpponentsStones(currentPit, pits);
                        shouldAddPebble = false;
                    }
                    if (currentPit.isStore()) {
                        switchPlayerTurn = false;
                    }
                }
                if (shouldAddPebble) {
                    currentPit.incrementPebbleCount();
                }
            }
        }

        if (switchPlayerTurn) {
            changePlayersTurn(playerId, pits);
        }
        return (List<PlayerPitsData>) playerPitsRepository.saveAll(pits);
    }

    public void changePlayersTurn(Long playerId, List<PlayerPitsData> pits) {
        List<Player> players = getPlayersForCurrentGame(pits);
        int total_players = players.size();
        OptionalInt optInt = IntStream.range(0, total_players).filter(i -> players.get(i).getId().equals(playerId)).findFirst();
        if (optInt.isEmpty()) throw new BusinessRuleException("player not found.");

        playerRepository.updatePlayersTurn(players.get(optInt.getAsInt()).getId(), false);
        playerRepository.updatePlayersTurn(players.get((optInt.getAsInt() + 1) % total_players).getId(), true);
    }

    public void captureOpponentsStones(PlayerPitsData currentPit, List<PlayerPitsData> pits) {
        List<Player> players = getPlayersForCurrentGame(pits);
        int totalPitsInGame = (PITS_PER_PLAYER + 1) * players.size();

        PlayerPitsData pitToCapture = pits.get(totalPitsInGame - currentPit.getSequence() - 1);
        int pitsToAddInStore = pitToCapture.getPebblesCount() + 1;

        List<PlayerPitsData> dataToSave = new ArrayList<>();
        PlayerPitsData storePit = getStorePitForCurrentPlayer(pits, currentPit.getPlayerGame().getPlayer().getId());
        if (!ObjectUtils.isEmpty(storePit)) {
            storePit = storePit.toBuilder().pebblesCount(storePit.incrementPebbleCountBy(pitsToAddInStore)).build();
            dataToSave.add(storePit);
            currentPit = currentPit.toBuilder().pebblesCount(0).build();
            dataToSave.add(currentPit);
            pitToCapture.resetPebbleCountToZero();
            dataToSave.add(pitToCapture);

            playerPitsRepository.saveAll(dataToSave);
        }
    }

    private PlayerPitsData getStorePitForCurrentPlayer(List<PlayerPitsData> pits, Long playerId) {
        return pits.stream().filter(p -> p.getPlayerGame().getPlayer().getId().equals(playerId) && p.isStore())
                .findFirst().orElse(null);
    }

    private List<Player> getPlayersForCurrentGame(List<PlayerPitsData> pitsData) {
        return pitsData.stream().map(p -> p.getPlayerGame().getPlayer()).distinct().collect(Collectors.toList());
    }

}
