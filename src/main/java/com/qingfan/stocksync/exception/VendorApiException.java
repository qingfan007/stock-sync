package com.qingfan.stocksync.exception;

public class VendorApiException extends RuntimeException {

    public VendorApiException(String message) {
        super(message);
    }

    public VendorApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
