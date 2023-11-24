package com.game.mancala.service;

import com.game.mancala.exception.BusinessRuleException;
import com.game.mancala.model.Game;
import com.game.mancala.model.Player;
import com.game.mancala.model.PlayerGame;
import com.game.mancala.model.PlayerPitsData;
import com.game.mancala.model.api.CreateGameDto;
import com.game.mancala.model.dto.GameDetailsDto;
import com.game.mancala.model.dto.PlayerPitsDataDto;
import com.game.mancala.repository.GamesRepository;
import com.game.mancala.repository.PlayerGamesRepository;
import com.game.mancala.repository.PlayerPitsRepository;
import com.game.mancala.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock GamesRepository gamesRepository;
    @Mock PlayerRepository playerRepository;
    @Mock PlayerPitsRepository playerPitsRepository;
    @Mock
    PlayerGamesRepository playerGamesRepository;
    @InjectMocks GameService gameService;

    private List<PlayerPitsData> pitsData = new ArrayList<>();
    private List<PlayerPitsData> updatedPitsData = new ArrayList<>();
    private List<PlayerPitsDataDto> expectedPlayerPitsList;
    private List<PlayerPitsDataDto> expectedPlayerPitsAfterMove;
    private Player p1, p2;
    private Game game;
    private Instant createdTime;

    @BeforeEach
    public void setup() {
        createdTime = Instant.now();
        p1 = Player.builder().id(1L).isMyTurn(true).name("ABC").build();
        p2 = Player.builder().id(2L).isMyTurn(false).name("XYZ").build();
        game = Game.builder().id(1L).startTime(createdTime).build();

        PlayerGame pg1 = PlayerGame.builder().game(game).player(p1).id(1L).build();
        PlayerGame pg2 = PlayerGame.builder().game(game).player(p2).id(2L).build();
        pitsData = List.of(
                PlayerPitsData.builder().id(1L).pebblesCount(6).playerGame(pg1).isStore(false).sequence(1).build(),
                PlayerPitsData.builder().id(2L).pebblesCount(6).playerGame(pg1).isStore(false).sequence(2).build(),
                PlayerPitsData.builder().id(3L).pebblesCount(6).playerGame(pg1).isStore(false).sequence(3).build(),
                PlayerPitsData.builder().id(4L).pebblesCount(6).playerGame(pg1).isStore(false).sequence(4).build(),
                PlayerPitsData.builder().id(5L).pebblesCount(6).playerGame(pg1).isStore(false).sequence(5).build(),
                PlayerPitsData.builder().id(6L).pebblesCount(6).playerGame(pg1).isStore(false).sequence(6).build(),
                PlayerPitsData.builder().id(7L).pebblesCount(0).playerGame(pg1).isStore(true).sequence(7).build(),
                PlayerPitsData.builder().id(8L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(8).build(),
                PlayerPitsData.builder().id(9L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(9).build(),
                PlayerPitsData.builder().id(10L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(10).build(),
                PlayerPitsData.builder().id(11L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(11).build(),
                PlayerPitsData.builder().id(12L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(12).build(),
                PlayerPitsData.builder().id(13L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(13).build(),
                PlayerPitsData.builder().id(14L).pebblesCount(0).playerGame(pg2).isStore(true).sequence(14).build());

        expectedPlayerPitsList = List.of(
                PlayerPitsDataDto.builder().id(1L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(6).sequence(1).build(),
                PlayerPitsDataDto.builder().id(2L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(6).sequence(2).build(),
                PlayerPitsDataDto.builder().id(3L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(6).sequence(3).build(),
                PlayerPitsDataDto.builder().id(4L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(6).sequence(4).build(),
                PlayerPitsDataDto.builder().id(5L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(6).sequence(5).build(),
                PlayerPitsDataDto.builder().id(6L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(6).sequence(6).build(),
                PlayerPitsDataDto.builder().id(7L).playerName("ABC").playerId(1L).gameId(1L).store(true).pebblesCount(0).sequence(7).build(),
                PlayerPitsDataDto.builder().id(8L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(8).build(),
                PlayerPitsDataDto.builder().id(9L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(9).build(),
                PlayerPitsDataDto.builder().id(10L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(10).build(),
                PlayerPitsDataDto.builder().id(11L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(11).build(),
                PlayerPitsDataDto.builder().id(12L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(12).build(),
                PlayerPitsDataDto.builder().id(13L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(13).build(),
                PlayerPitsDataDto.builder().id(14L).playerName("XYZ").playerId(2L).gameId(1L).store(true).pebblesCount(0).sequence(14).build());

        updatedPitsData = List.of(
                PlayerPitsData.builder().id(1L).pebblesCount(6).playerGame(pg1).isStore(false).sequence(1).build(),
                PlayerPitsData.builder().id(2L).pebblesCount(6).playerGame(pg1).isStore(false).sequence(2).build(),
                PlayerPitsData.builder().id(3L).pebblesCount(0).playerGame(pg1).isStore(false).sequence(3).build(),
                PlayerPitsData.builder().id(4L).pebblesCount(7).playerGame(pg1).isStore(false).sequence(4).build(),
                PlayerPitsData.builder().id(5L).pebblesCount(7).playerGame(pg1).isStore(false).sequence(5).build(),
                PlayerPitsData.builder().id(6L).pebblesCount(7).playerGame(pg1).isStore(false).sequence(6).build(),
                PlayerPitsData.builder().id(7L).pebblesCount(1).playerGame(pg1).isStore(true).sequence(7).build(),
                PlayerPitsData.builder().id(8L).pebblesCount(7).playerGame(pg2).isStore(false).sequence(8).build(),
                PlayerPitsData.builder().id(9L).pebblesCount(7).playerGame(pg2).isStore(false).sequence(9).build(),
                PlayerPitsData.builder().id(10L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(10).build(),
                PlayerPitsData.builder().id(11L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(11).build(),
                PlayerPitsData.builder().id(12L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(12).build(),
                PlayerPitsData.builder().id(13L).pebblesCount(6).playerGame(pg2).isStore(false).sequence(13).build(),
                PlayerPitsData.builder().id(14L).pebblesCount(0).playerGame(pg2).isStore(true).sequence(14).build());

        expectedPlayerPitsAfterMove = List.of(
                PlayerPitsDataDto.builder().id(1L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(6).sequence(1).build(),
                PlayerPitsDataDto.builder().id(2L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(6).sequence(2).build(),
                PlayerPitsDataDto.builder().id(3L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(0).sequence(3).build(),
                PlayerPitsDataDto.builder().id(4L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(7).sequence(4).build(),
                PlayerPitsDataDto.builder().id(5L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(7).sequence(5).build(),
                PlayerPitsDataDto.builder().id(6L).playerName("ABC").playerId(1L).gameId(1L).store(false).pebblesCount(7).sequence(6).build(),
                PlayerPitsDataDto.builder().id(7L).playerName("ABC").playerId(1L).gameId(1L).store(true).pebblesCount(1).sequence(7).build(),
                PlayerPitsDataDto.builder().id(8L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(7).sequence(8).build(),
                PlayerPitsDataDto.builder().id(9L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(7).sequence(9).build(),
                PlayerPitsDataDto.builder().id(10L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(10).build(),
                PlayerPitsDataDto.builder().id(11L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(11).build(),
                PlayerPitsDataDto.builder().id(12L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(12).build(),
                PlayerPitsDataDto.builder().id(13L).playerName("XYZ").playerId(2L).gameId(1L).store(false).pebblesCount(6).sequence(13).build(),
                PlayerPitsDataDto.builder().id(14L).playerName("XYZ").playerId(2L).gameId(1L).store(true).pebblesCount(0).sequence(14).build());
    }

    @Test
    void shouldThrowErrorCreateNewGame() throws BusinessRuleException {
        String[] players = new String[3];
        players[0] = "ABC"; players[1] = "XYZ"; players[2] = "PQR";
        CreateGameDto createGameDto = CreateGameDto.builder()
                .playerName(players).build();
        Assertions.assertThrows(BusinessRuleException.class, () -> gameService.createNewGame(createGameDto));
    }

    @Test
    void shouldThrowErrorBlankNameCreateNewGame() throws BusinessRuleException {
        String[] players = new String[3];
        players[0] = "ABC"; players[1] = "";
        CreateGameDto createGameDto = CreateGameDto.builder()
                .playerName(players).build();

        Assertions.assertThrows(BusinessRuleException.class, () -> gameService.createNewGame(createGameDto));
    }

    @Test
    void shouldCreateNewGame() throws BusinessRuleException {
        String[] players = new String[2];
        players[0] = "ABC"; players[1] = "XYZ";
        CreateGameDto createGameDto = CreateGameDto.builder()
                .playerName(players).build();

        Mockito.when(playerRepository.findByName(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        BDDMockito.given(playerRepository.save(ArgumentMatchers.any())).willReturn(p1);
        BDDMockito.given(playerRepository.save(ArgumentMatchers.any())).willReturn(p2);
        Mockito.when(gamesRepository.save(ArgumentMatchers.any())).thenReturn(game);
        Mockito.when(playerPitsRepository.saveAll(ArgumentMatchers.any())).thenReturn(pitsData);
        Mockito.when(playerPitsRepository.findByPlayerGameGameIdOrderBySequence(game.getId())).thenReturn(pitsData);
        Mockito.when(playerGamesRepository.saveAll(ArgumentMatchers.any()))
                .thenReturn(List.of(PlayerGame.builder().game(game).player(p1).build(),
                        PlayerGame.builder().game(game).player(p2).build()));

        GameDetailsDto expectedDto = GameDetailsDto.builder().isGameOver(false)
                .pitsData(expectedPlayerPitsList).build();
        GameDetailsDto dto = gameService.createNewGame(createGameDto);

        org.assertj.core.api.Assertions.assertThat(dto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @Test
    void shouldGetGameDetails() {
        Mockito.when(playerPitsRepository.findByPlayerGameGameIdOrderBySequence(game.getId())).thenReturn(pitsData);
        GameDetailsDto expectedDto = GameDetailsDto.builder().isGameOver(false)
                .pitsData(expectedPlayerPitsList).build();
        GameDetailsDto dto = gameService.getGameDetails(game.getId());

        org.assertj.core.api.Assertions.assertThat(dto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @Test
    void shouldMakeMoveByPlayerSuccess() throws BusinessRuleException {

        Long pitId = 3L;
        PlayerGame playerGame = PlayerGame.builder().player(p1).game(game).build();
        PlayerPitsData optPitData = PlayerPitsData.builder().sequence(3).isStore(false).pebblesCount(6).id(3L).playerGame(playerGame).build();
        Mockito.when(playerPitsRepository.findById(pitId)).thenReturn(Optional.of(optPitData));
        Mockito.when(playerPitsRepository.findByPlayerGameGameIdOrderBySequence(game.getId()))
                .thenReturn(pitsData);
        Mockito.when(playerRepository.updatePlayersTurn(1L, false)).thenReturn(1);

        GameDetailsDto responseDto = gameService.makeMove(pitId, 1L);
        GameDetailsDto expectedDto = GameDetailsDto.builder().isGameOver(false)
                .pitsData(expectedPlayerPitsAfterMove).build();

        org.assertj.core.api.Assertions.assertThat(responseDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }
    @Test
    void shouldMakeMoveByPlayerFailureWithoutTurn() throws BusinessRuleException {
        Long pitId = 3L;
        Player playerTest = Player.builder().name("ABC").id(1L).isMyTurn(false).build();
        PlayerGame playerGame = PlayerGame.builder().player(playerTest).game(game).build();
        PlayerPitsData optPitData = PlayerPitsData.builder().sequence(3).isStore(false).pebblesCount(6).id(3L).playerGame(playerGame).build();
        Mockito.when(playerPitsRepository.findById(pitId)).thenReturn(Optional.of(optPitData));

        Assertions.assertThrows(BusinessRuleException.class, () -> gameService.makeMove(pitId, 1L));
    }
    @Test
    void shouldMakeMoveByPlayerFailureWithStore() throws BusinessRuleException {

        Long pitId = 3L;
        PlayerGame playerGame = PlayerGame.builder().player(p1).game(game).build();
        PlayerPitsData optPitData = PlayerPitsData.builder().sequence(3).isStore(true).pebblesCount(6).id(3L).playerGame(playerGame).build();
        Mockito.when(playerPitsRepository.findById(pitId)).thenReturn(Optional.of(optPitData));

        Assertions.assertThrows(BusinessRuleException.class, () -> gameService.makeMove(pitId, 1L));
    }

    @Test
    void shouldMakeMoveByPlayerFailureWithNoPit() {
        Long pitId = 100L;
        Mockito.when(playerPitsRepository.findById(pitId)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessRuleException.class, () -> gameService.makeMove(pitId, 1L));
    }

    @Test
    void shouldTestRestartGameForSamePlayers() {

    }
}
