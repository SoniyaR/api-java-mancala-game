package com.game.mancala.controller;

import com.game.mancala.exception.BusinessRuleException;
import com.game.mancala.model.api.CreateGameDto;
import com.game.mancala.model.dto.GameDetailsDto;
import com.game.mancala.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired private GameService gameService;

    @PostMapping
    public GameDetailsDto createNewGame(@RequestBody CreateGameDto createGameDto) {
        return gameService.createNewGame(createGameDto);
    }

    @GetMapping(path = "/{id}")
    public GameDetailsDto getGameDetailsById(@PathVariable Long id) {
        return gameService.getGameDetails(id);
    }

    @GetMapping(path = "/id/{id}")
    public GameDetailsDto getGameDetailsById(@PathVariable UUID id) {
        return gameService.getGameDetails(id);
    }

    @PutMapping(path = "/pit/{id}/makemove")
    public GameDetailsDto makeMove(@PathVariable Long id, @RequestParam Long playerId) {
        GameDetailsDto response = gameService.makeMove(id, playerId);
        return response;
    }
}
