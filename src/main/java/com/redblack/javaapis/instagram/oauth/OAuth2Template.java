package com.redblack.javaapis.instagram.oauth;

import java.util.*;

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.util.MultiValueMap;

import com.redblack.javaapis.instagram.StringUtils;

/**
 * 
 * @author deepak
 *
 */
public class OAuth2Template extends org.springframework.social.oauth2.OAuth2Template {

    public OAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        return extractAccessGrant(getRestTemplate().postForObject(accessTokenUrl, parameters, Map.class));
    }

    @SuppressWarnings("unchecked")
    private AccessGrant extractAccessGrant(Map<String, Object> result) {
        ArrayList<String> arr = (ArrayList<String>) result.get("scope");
        String scope = StringUtils.join(arr.iterator(), "+");
        return createAccessGrant((String) result.get("access_token"), scope, (String) result.get("refresh_token"),
                getIntegerValue(result, "expires_in"), result);
    }

    // Retrieves object from map into an Integer, regardless of the object's
    // actual type. Allows for flexibility in object type (eg, "3600" vs 3600).
    private Long getIntegerValue(Map<String, Object> map, String key) {
        try {
            return Long.valueOf(String.valueOf(map.get(key))); // normalize to
                                                               // String before
                                                               // creating
                                                               // integer value;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
