package com.game.mancala.repository;

import com.game.mancala.model.PlayerPitsData;
import com.game.mancala.model.dto.PlayerPitsDataDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerPitsRepository extends CrudRepository<PlayerPitsData, Long> {

    List<PlayerPitsData> findByPlayerGameGameIdOrderBySequence(Long gameId);

}
