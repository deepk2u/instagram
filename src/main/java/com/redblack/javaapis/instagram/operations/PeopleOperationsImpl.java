package com.redblack.javaapis.instagram.operations;

import org.slf4j.*;
import org.springframework.web.client.RestTemplate;

import com.redblack.javaapis.instagram.beans.InstagramUser;

/**
 * 
 * @author deepak
 *
 */
public class PeopleOperationsImpl implements PeopleOperations {

    private static Logger logger = LoggerFactory.getLogger(PeopleOperationsImpl.class);

    private static final String API_URL_ROOT = "https://api.instagram.com/v1";

    private static final String GET_URL = API_URL_ROOT + "/users/{user-id}/?access_token={accessToken}";

    private RestTemplate restTemplate;

    public PeopleOperationsImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public InstagramUser getInstagramUser(String userid, String accesstoken) {
        String url = GET_URL.replace("{user-id}", userid).replace("{accessToken}", accesstoken);
        if (logger.isDebugEnabled()) {
            logger.debug("URL : " + url);
        }
        InstagramUser instagramUser = restTemplate.getForObject(url, InstagramUser.class);
        return instagramUser;
    }

}
