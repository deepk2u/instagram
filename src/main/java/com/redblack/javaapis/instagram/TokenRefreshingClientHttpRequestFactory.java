package com.redblack.javaapis.instagram;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.util.ReflectionUtils;

import com.redblack.javaapis.instagram.oauth.OAuth2RefreshCallback;

/**
 * 
 * @author deepak
 *
 */
public class TokenRefreshingClientHttpRequestFactory implements ClientHttpRequestFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRefreshingClientHttpRequestFactory.class);

    private ClientHttpRequestFactory delegate;
    private OAuth2RefreshCallback callback;
    /**
     * Reference to the list of interceptors for the current rest template.
     * Used to replace the existing oauth2 interceptor with a new one, having the refreshed token
     */
    private List<ClientHttpRequestInterceptor> requestInterceptors;

    public TokenRefreshingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory, OAuth2RefreshCallback callback, List<ClientHttpRequestInterceptor> requestInterceptors) {
        this.delegate = requestFactory;
        this.callback = callback;
        this.requestInterceptors = requestInterceptors;
    }

    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        ClientHttpRequest request = delegate.createRequest(uri, httpMethod);
        return new TokenRefreshingClientHttpRequest(request);

    }

    /**
     * 
     * @author deepak
     *
     */
    private final class TokenRefreshingClientHttpRequest implements ClientHttpRequest {
        private ClientHttpRequest delegate;

        TokenRefreshingClientHttpRequest(ClientHttpRequest delegate) {
            this.delegate = delegate;
        }
        public HttpMethod getMethod() {
            return delegate.getMethod();
        }
        public URI getURI() {
            return delegate.getURI();
        }
        public HttpHeaders getHeaders() {
            return delegate.getHeaders();
        }
        public OutputStream getBody() throws IOException {
            return delegate.getBody();
        }
        public ClientHttpResponse execute() throws IOException {
            ClientHttpResponse response = delegate.execute();
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                LOGGER.info("Token is invalid (got 401 response). Trying get a new token using the refresh token");

                String newToken = callback.refreshToken();
                if (newToken == null) {
                    return response;
                } else {
                    LOGGER.info("New token obtained, retrying the request with it");
                    // reflectively set the new token at the oauth2 interceptor (no nicer way, alas)
                    for (ClientHttpRequestInterceptor interceptor: requestInterceptors) {
                        if (interceptor.getClass().getName().equals("org.springframework.social.oauth2.OAuth2RequestInterceptor")) {
                            Field field = ReflectionUtils.findField(interceptor.getClass(), "accessToken");
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, interceptor, newToken);
                        }
                    }

                    // create a new request, using the new token, but don't go through the refreshing factory again
                    // because it may cause an endless loop if all tokens are invalid
                    ClientHttpRequest newRequest = TokenRefreshingClientHttpRequestFactory.this.delegate.createRequest(delegate.getURI(), delegate.getMethod());
                    return newRequest.execute();
                }
            }
            return response;
        }
    }
}
