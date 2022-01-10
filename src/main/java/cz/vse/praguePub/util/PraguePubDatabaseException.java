package cz.vse.praguePub.util;

public class PraguePubDatabaseException extends Exception {
    public PraguePubDatabaseException(String message) {
        super(message);
    }

    public PraguePubDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PraguePubDatabaseException(Throwable cause) {
        super(cause);
    }

    public PraguePubDatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
