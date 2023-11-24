package com.game.mancala.service;

import com.game.mancala.model.Game;
import com.game.mancala.model.Player;
import com.game.mancala.model.PlayerGame;
import com.game.mancala.model.PlayerPitsData;
import com.game.mancala.repository.PlayerPitsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerPitsRepository playerPitsRepository;

    @InjectMocks
    private PlayerService playerService;

    private List<Player> playersList;
    private List<PlayerPitsData> playerPitsList;

    @BeforeEach
    public void setup() {
        Game game = Game.builder().id(1L).startTime(Instant.now()).build();
        Player p1 = Player.builder().isMyTurn(true).id(1L).name("ABC").build();
        Player p2 = Player.builder().isMyTurn(false).id(2L).name("XYZ").build();

        playersList = List.of(p1, p2);

        PlayerGame pg1 = PlayerGame.builder().player(p1).game(game).build();
        PlayerGame pg2 = PlayerGame.builder().player(p2).game(game).build();

        playerPitsList= List.of(PlayerPitsData.builder().pebblesCount(6).playerGame(pg1).isStore(false).sequence(1).build(),
                PlayerPitsData.builder().pebblesCount(6).playerGame(pg1).isStore(false).sequence(2).build(),
                PlayerPitsData.builder().pebblesCount(6).playerGame(pg2).isStore(false).sequence(3).build(),
                PlayerPitsData.builder().pebblesCount(6).playerGame(pg2).isStore(false).sequence(4).build());
    }

    @Test
    void shouldGetPlayerDetails() {
        when(playerPitsRepository.findByPlayerGameGameIdOrderBySequence(1L)).thenReturn(playerPitsList);

        Assertions.assertEquals(playersList, playerService.getPlayerDetails(1L));
    }
}
