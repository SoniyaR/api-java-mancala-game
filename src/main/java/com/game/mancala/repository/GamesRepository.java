package com.game.mancala.repository;

import com.game.mancala.model.Game;
import com.game.mancala.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesRepository extends CrudRepository<Game, Long> {
}
