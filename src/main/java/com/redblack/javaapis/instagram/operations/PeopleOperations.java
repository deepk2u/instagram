package com.redblack.javaapis.instagram.operations;

import com.redblack.javaapis.instagram.beans.InstagramUser;

/**
 * 
 * @author deepak
 *
 */
public interface PeopleOperations {

    InstagramUser getInstagramUser(String userid, String accesstoken);

}
