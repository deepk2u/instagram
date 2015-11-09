//CHECKSTYLE:OFF
package com.redblack.javaapis.instagram.operations;

import java.lang.reflect.*;

import org.springframework.http.client.*;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.util.*;

import com.redblack.javaapis.instagram.InstagramConfiguration;

/**
 * 
 * @author deepak
 *
 */
public class InstagramImpl extends AbstractOAuth2ApiBinding implements Instagram {
    private static boolean HTTP_COMPONENTS_AVAILABLE = ClassUtils.isPresent("org.apache.http.client.HttpClient",
            ClientHttpRequestFactory.class.getClassLoader());

    private PeopleOperations peopleOperations;

    public InstagramImpl(String accessToken, InstagramConfiguration config) {
        super(accessToken);
        // (doing work in constructor because the superclass does it that forces
        // us to do it)
        configure(config);
        // Wrap the request factory with a BufferingClientHttpRequestFactory so
        // that the error handler can do repeat reads on the response.getBody()
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory());
        getRestTemplate().setRequestFactory(requestFactory);
    }

    private static final Field requestFactoryDelegateField = ReflectionUtils.findField(InterceptingClientHttpRequestFactory.class,
            "requestFactory");
    static {
        requestFactoryDelegateField.setAccessible(true);
    }

    private void configure(InstagramConfiguration config) {
        // TODO retrying

        // well, that's ugly..
        // https://issues.springsource.org/browse/SOCIAL-196,
        // https://jira.springsource.org/browse/SOCIAL-266
        ClientHttpRequestFactory clientRequestFactory = getRestTemplate().getRequestFactory();
        if (clientRequestFactory instanceof InterceptingClientHttpRequestFactory) {
            clientRequestFactory = (ClientHttpRequestFactory) ReflectionUtils.getField(requestFactoryDelegateField, clientRequestFactory);
        }
        if (HTTP_COMPONENTS_AVAILABLE) {
            if (config.getConnectTimeout() > 0) {
                Method setter = ReflectionUtils.findMethod(clientRequestFactory.getClass(), "setConnectTimeout", int.class);
                setter.setAccessible(true);
                ReflectionUtils.invokeMethod(setter, clientRequestFactory, config.getReadTimeout());
            }
            if (config.getReadTimeout() > 0) {
                Method setter = ReflectionUtils.findMethod(clientRequestFactory.getClass(), "setReadTimeout", int.class);
                setter.setAccessible(true);
                ReflectionUtils.invokeMethod(setter, clientRequestFactory, config.getReadTimeout());
            }
        } else {
            SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) clientRequestFactory;
            if (config.getConnectTimeout() > 0) {
                factory.setConnectTimeout(config.getConnectTimeout());
            }
            if (config.getReadTimeout() > 0) {
                factory.setReadTimeout(config.getReadTimeout());
            }
        }
    }

    public void init() {
        peopleOperations = new PeopleOperationsImpl(getRestTemplate());
    }

    public PeopleOperations getPeopleOperations() {
        if (peopleOperations == null) {
            throw new IllegalStateException("Call init() before using this object");
        }
        return peopleOperations;
    }

}
