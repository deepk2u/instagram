package com.redblack.javaapis.instagram.oauth;

import org.springframework.social.oauth2.AccessGrant;

/**
 * 
 * @author deepak
 *
 */
public interface OAuth2RefreshListener {

    /**
     * Invoked when a token is refreshed.
     * @param accessToken the old access token
     * @param accessGrant the new access grant (containing both tokens)
     */
    void tokensRefreshed(String oldAccessToken, AccessGrant accessGrant);
}
