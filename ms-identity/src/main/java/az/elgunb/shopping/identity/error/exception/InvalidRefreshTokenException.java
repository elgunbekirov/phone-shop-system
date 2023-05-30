package az.elgunb.shopping.identity.error.exception;

import az.elgunb.shopping.common.error.exception.CommonAuthException;

public class InvalidRefreshTokenException extends CommonAuthException {

    private static InvalidRefreshTokenException instance;

    private InvalidRefreshTokenException() {
        super(401, "Invalid refresh token");
    }

    public static InvalidRefreshTokenException getInstance() {
        if (instance == null)
            synchronized (InvalidRefreshTokenException.class) {
                instance = new InvalidRefreshTokenException();
            }
        return instance;
    }

}
