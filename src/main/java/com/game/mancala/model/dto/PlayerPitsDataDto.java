package com.game.mancala.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class PlayerPitsDataDto {
    private Long id;
    private int sequence;
    private int pebblesCount;
    private boolean store;
    private Long playerId;
    private String playerName;
    private Long gameId;
}
