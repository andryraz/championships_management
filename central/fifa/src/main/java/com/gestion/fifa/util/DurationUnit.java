package com.gestion.fifa.util;


public enum DurationUnit {
    SECOND(1),
    MINUTE(60),
    HOUR(3600);

    private final int toSeconds;

    DurationUnit(int toSeconds) {
        this.toSeconds = toSeconds;
    }

    public long convert(long seconds) {
        return seconds / toSeconds;
    }
}