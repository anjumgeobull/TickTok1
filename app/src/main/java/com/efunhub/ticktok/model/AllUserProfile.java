package com.efunhub.ticktok.model;

import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;

import java.util.ArrayList;

public class AllUserProfile extends BaseServiceResponseModel {
    public ArrayList<AllUserProfile.ProfileList> data;

    public ArrayList<AllUserProfile.ProfileList> getData() {
        return data;
    }

    public void setData(ArrayList<AllUserProfile.ProfileList> data) {
        this.data = data;
    }

    public static class ProfileList {
        public String _id;
        public String country;
        public String name;
        public String mobile;
        public String email;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

