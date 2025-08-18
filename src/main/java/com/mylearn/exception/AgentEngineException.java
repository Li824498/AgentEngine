package com.mylearn.exception;

public class AgentEngineException extends RuntimeException{
    public AgentEngineException(String message) {
        super(message);
    }

    public AgentEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
