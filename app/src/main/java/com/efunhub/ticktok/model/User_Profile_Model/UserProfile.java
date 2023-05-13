
package com.efunhub.ticktok.model.User_Profile_Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfile {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("district")
    @Expose
    private String district;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("other")
    @Expose
    private String other;

    @SerializedName("insta_id")
    @Expose
    private String insta_id;

    @SerializedName("facebook_id")
    @Expose
    private String facebook_id;

    @SerializedName("youtube_id")
    @Expose
    private String youtube_id;

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("otp_status")
    @Expose
    private String otpStatus;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("point")
    @Expose
    private String point;
    @SerializedName("profile_img")
    @Expose
    private String profile_img;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("allPosts")
    @Expose
    private List<AllPost> allPosts = null;
    //    @SerializedName("followings")
//    @Expose
//    private List<Follow_List> followings = null;
//    @SerializedName("followers")
//    @Expose
//    private List<Follow_List> followers = null;
    @SerializedName("total_posts")
    @Expose
    private Integer totalPosts;
    @SerializedName("total_followings")
    @Expose
    private Integer totalFollowings;
    @SerializedName("total_followers")
    @Expose
    private Integer totalFollowers;
    @SerializedName("other_profile_following")
    @Expose
    private Integer otherProfileFollowing;

    @SerializedName("Follow")
    @Expose

    private String Follow_status;

    public String getFollow_status() {
        return Follow_status;
    }

    public void setFollow_status(String follow_status) {
        Follow_status = follow_status;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getInsta_id() {
        return insta_id;
    }

    public void setInsta_id(String insta_id) {
        this.insta_id = insta_id;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getOtpStatus() {
        return otpStatus;
    }

    public void setOtpStatus(String otpStatus) {
        this.otpStatus = otpStatus;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<AllPost> getAllPosts() {
        return allPosts;
    }

    public void setAllPosts(List<AllPost> allPosts) {
        this.allPosts = allPosts;
    }

//    public List<Follow_List> getFollowings() {
//        return followings;
//    }
//
//    public void setFollowings(List<Follow_List> followings) {
//        this.followings = followings;
//    }

//    public List<Follow_List> getFollowers() {
//        return followers;
//    }
//
//    public void setFollowers(List<Follow_List> followers) {
//        this.followers = followers;
//    }

    public Integer getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(Integer totalPosts) {
        this.totalPosts = totalPosts;
    }

    public Integer getTotalFollowings() {
        return totalFollowings;
    }

    public void setTotalFollowings(Integer totalFollowings) {
        this.totalFollowings = totalFollowings;
    }

    public Integer getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(Integer totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public Integer getOtherProfileFollowing() {
        return otherProfileFollowing;
    }

    public void setOtherProfileFollowing(Integer otherProfileFollowing) {
        this.otherProfileFollowing = otherProfileFollowing;
    }

}
