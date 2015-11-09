package com.redblack.javaapis.instagram.oauth;

/**
 * Interface for internal use.
 *
 * @author deepak
 *
 */
public interface OAuth2RefreshCallback {

    /**
     * Attempts to refresh the token.
     * @return the new access token
     */
    String refreshToken();
}