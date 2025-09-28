package com.qingfan.stocksync.exception;

public class VendorFileException extends RuntimeException {

    public VendorFileException(String message) {
        super(message);
    }

    public VendorFileException(String message, Throwable cause) {
        super(message, cause);
    }

}
