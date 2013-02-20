package de.chrfritz.jolokiamunin.jolokia;

public class FetcherException extends Exception {
    public FetcherException() {
    }

    public FetcherException(String s) {
        super(s);
    }

    public FetcherException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FetcherException(Throwable throwable) {
        super(throwable);
    }
}
