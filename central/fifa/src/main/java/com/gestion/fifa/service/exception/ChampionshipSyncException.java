package com.gestion.fifa.service.exception;


public class ChampionshipSyncException extends RuntimeException {
  public ChampionshipSyncException(String message, Throwable cause) {
    super(message, cause);
  }
  public ChampionshipSyncException(String message) {
    super(message);
  }
}

