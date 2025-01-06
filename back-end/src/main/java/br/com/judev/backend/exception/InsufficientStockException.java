package br.com.judev.backend.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message){ super(message);}
}
