package com.game.mancala.exception;


public class BusinessRuleException extends RuntimeException{
    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
