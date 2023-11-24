package com.game.mancala.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class GameDetailsDto {

    private List<PlayerPitsDataDto> pitsData;
    private boolean isGameOver;
    private String winnerName;
}
