package com.dwes.security.error.exception;

public class VideojuegoNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public VideojuegoNotFoundException(String message) {
        super(message);
    }
}