package com.redblack.javaapis.instagram;

import org.springframework.social.oauth2.*;

import com.redblack.javaapis.instagram.oauth.*;
import com.redblack.javaapis.instagram.oauth.OAuth2Template;
import com.redblack.javaapis.instagram.operations.*;

public class InstagramFactory extends AbstractOAuth2ServiceProvider<Instagram> {

    private static final String TOKEN_URL = "https://instagram.com/oauth/authorize";
    private static final String AUTH_URL = "https://instagram.com/oauth/authorize";

    private InstagramConfiguration config;

    public InstagramFactory(String clientId, String clientSecret, InstagramConfiguration config) {
        super(createOAuthTemplate(clientId, clientSecret));
        this.config = config;
    }

    private static OAuth2Template createOAuthTemplate(String clientId, String clientSecret) {
        OAuth2Template template = new OAuth2Template(clientId, clientSecret, AUTH_URL, TOKEN_URL);
        template.setUseParametersForClientAuthentication(true);
        return template;
    }

    public InstagramFactory(String clientId, String clientSecret) {
        this(clientId, clientSecret, new InstagramConfiguration());
    }

    public Instagram getApi(String accessToken) {
        // proxy configuring needs to be done before instantiation the client
        // object,
        // because otherwise system settings are read instead
        configureProxy();

        InstagramImpl plus = new InstagramImpl(accessToken, config);
        plus.init();
        return plus;
    }

    private void configureProxy() {
        // overriding values specified with -D, if any
        if (config.getProxyHost() != null) {
            System.setProperty("http.proxyHost", config.getProxyHost());
        }
        if (config.getProxyPort() != 0) {
            System.setProperty("http.proxyPort", String.valueOf(config.getProxyPort()));
        }
    }

    public final class DefaultOAuth2RefreshCallback implements OAuth2RefreshCallback {
        private String accessToken;
        private String refreshToken;
        private OAuth2RefreshListener listener;

        public DefaultOAuth2RefreshCallback(String accessToken, String refreshToken, OAuth2RefreshListener listener) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.listener = listener;
        }

        public String refreshToken() {
            if (refreshToken == null) {
                return null;
            }
            AccessGrant grant = InstagramFactory.this.getOAuthOperations().refreshAccess(refreshToken, null);
            if (listener != null) {
                listener.tokensRefreshed(accessToken, grant);
            }
            return grant.getAccessToken();
        }
    }
}
