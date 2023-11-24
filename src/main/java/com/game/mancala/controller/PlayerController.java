package com.game.mancala.controller;

import com.game.mancala.model.Player;
import com.game.mancala.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping(path = "/games/{id}")
    private List<Player> getPlayersDetail(@PathVariable Long id) {
        return playerService.getPlayerDetails(id);
    }
}
