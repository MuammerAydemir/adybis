package com.muammer.adybis.base.exceptions;

public abstract class NoAvailableResourceException extends RuntimeException {
    public NoAvailableResourceException(String message) {
        super(message);
    }
}
