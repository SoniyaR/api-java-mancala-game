package com.game.mancala.model.dto;

import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Value;

public interface IPlayerPitsDataDto {

    Long getId();
    int getSequence();
    int getPebblesCount();
    boolean isStore();
    @Value("#{target.player.id}")
    Long getPlayerId();
    @Value("#{target.player.name}")
    String getPlayerName();
    @Value("#{target.game.id}")
    Long getGameId();


//    void setId(Long id);
//    void setSequence(int sequence);
//    void setPebblesCount(int count);
//    void setIsStore(boolean isStore);
//    void setPlayerId(Long playerId);
//    void setPlayerName(String playerName);
//    void setGameId(Long gameId);
}
