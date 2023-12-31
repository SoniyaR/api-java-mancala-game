package com.game.mancala.service;

import com.game.mancala.model.Player;
import com.game.mancala.model.PlayerPitsData;
import com.game.mancala.repository.PlayerPitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlayerService {
    @Autowired private PlayerPitsRepository pitsRepository;

    public List<Player> getPlayerDetails(Long gameId) {
        List<PlayerPitsData> pitsData = pitsRepository.findByPlayerGameGameIdOrderBySequence(gameId);
        return pitsData.stream().map(p-> p.getPlayerGame().getPlayer()).distinct().collect(Collectors.toList());
    }
}
