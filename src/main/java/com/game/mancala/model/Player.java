package com.game.mancala.model;

import jakarta.persistence.*;
import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player")
@ToString
@Getter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_my_turn")
    private boolean isMyTurn;

//    @OneToMany(fetch = FetchType.LAZY)
//    private PlayerPitsData[] pits;
}
