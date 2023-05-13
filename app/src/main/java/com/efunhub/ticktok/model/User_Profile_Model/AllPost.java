
package com.efunhub.ticktok.model.User_Profile_Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AllPost {

    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("post_data")
    @Expose
    private List<Post> postData = null;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public List<Post> getPostData() {
        return postData;
    }

    public void setPostData(List<Post> postData) {
        this.postData = postData;
    }

}
