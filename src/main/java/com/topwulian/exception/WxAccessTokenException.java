package com.topwulian.exception;

public class WxAccessTokenException extends  RuntimeException {

    private static final long serialVersionUID = 3880206998166270511L;

    public WxAccessTokenException() {
        super();
    }

    public WxAccessTokenException(String message) {
        super(message);
    }

    public WxAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
