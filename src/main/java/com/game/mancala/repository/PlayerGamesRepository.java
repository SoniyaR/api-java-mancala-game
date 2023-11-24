package com.game.mancala.repository;

import com.game.mancala.model.Game;
import com.game.mancala.model.PlayerGame;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerGamesRepository extends CrudRepository<PlayerGame, Long> {
}
