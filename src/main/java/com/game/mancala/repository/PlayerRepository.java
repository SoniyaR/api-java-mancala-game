package com.game.mancala.repository;

import com.game.mancala.model.Player;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    @Modifying
    @Query(value = "update Player set isMyTurn = :isTurn where id = :playerId")
    int updatePlayersTurn(Long playerId, boolean isTurn);

    Optional<Player> findByName(String name);
}
