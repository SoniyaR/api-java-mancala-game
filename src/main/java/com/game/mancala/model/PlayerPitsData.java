package com.game.mancala.model;

import com.game.mancala.model.dto.PlayerPitsDataDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_pit_data")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class PlayerPitsData {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "pebbles_count")
    private int pebblesCount;

    @ManyToOne
    @JoinColumn(name = "player_game_id", referencedColumnName = "id")
    private PlayerGame playerGame;

//    @ManyToOne
//    @JoinColumn(name = "player_id", referencedColumnName = "id")
//    private Player player;

//    @ManyToOne
//    @JoinColumn(name = "game_id", referencedColumnName = "id")
//    private Game game;

    @Column(name = "is_store")
    private boolean isStore;

    public void incrementPebbleCount() {
        this.pebblesCount = this.pebblesCount + 1;
    }
    public int incrementPebbleCountBy(int count) {
        return this.pebblesCount + count;
    }

    public void resetPebbleCountToZero() {
        this.pebblesCount = 0;
    }

    public static PlayerPitsDataDto toDto(PlayerPitsData pitData) {
        return PlayerPitsDataDto.builder()
                .id(pitData.getId())
                .store(pitData.isStore())
                .playerId(pitData.getPlayerGame().getPlayer().getId())
                .gameId(pitData.getPlayerGame().getGame().getId())
                .pebblesCount(pitData.getPebblesCount())
                .sequence(pitData.getSequence())
                .playerName(pitData.getPlayerGame().getPlayer().getName())
                .build();
    }
}
