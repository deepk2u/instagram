package com.redblack.javaapis.instagram.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author deepak
 *
 */
public class InstagramUser {

    /**
     * 
     * { "data": { "id": "1574083", "username": "snoopdogg", "full_name":
     * "Snoop Dogg", "profile_picture":
     * "http://distillery.s3.amazonaws.com/profiles/profile_1574083_75sq_1295469061.jpg"
     * , "bio": "This is my bio", "website": "http://snoopdogg.com", "counts": {
     * "media": 1320, "follows": 420, "followed_by": 3410 } }
     * 
     */

    private String id;

    private String username;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("profile_picture")
    private String profilePicture;

    private String bio;

    private String website;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
