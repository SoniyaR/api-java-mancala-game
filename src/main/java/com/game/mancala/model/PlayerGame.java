package com.game.mancala.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player_game")
@ToString
@Getter
public class PlayerGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

    //one game can have multiple players
    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player player;

}
