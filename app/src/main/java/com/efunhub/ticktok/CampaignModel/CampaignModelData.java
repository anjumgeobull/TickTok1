

package com.efunhub.ticktok.CampaignModel;

import java.util.List;

public class CampaignModelData {
    private int status;
    private String message;
    private List<Data> dataList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public static class Data {
        private String id;
        private String userAutoId;
        private String isVideo;
        private String frequency;
        private String campaign_name;
        private String campaignType;
        private String campaignLimit;
        private String startDate;
        private String endDate;
        private String country;
        private String state;
        private String area;
        private String gender;
        private String age;
        private String links;
        private String status;
        private String isImage;
        private String images;
        private String videos;
        private String updatedAt;
        private String createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserAutoId() {
            return userAutoId;
        }

        public void setUserAutoId(String userAutoId) {
            this.userAutoId = userAutoId;
        }

        public String getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(String isVideo) {
            this.isVideo = isVideo;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getCampaignName() {
            return campaign_name;
        }

        public void setCampaignName(String campaign_name) {
            this.campaign_name = campaign_name;
        }

        public String getCampaignType() {
            return campaignType;
        }

        public void setCampaignType(String campaignType) {
            this.campaignType = campaignType;
        }

        public String getCampaignLimit() {
            return campaignLimit;
        }

        public void setCampaignLimit(String campaignLimit) {
            this.campaignLimit = campaignLimit;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate ;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country ;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state ;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area ;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender ;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age ;
        }

        public String getLinks() {
            return links;
        }

        public void setLinks(String links) {
            this.links = links ;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status ;
        }


        public String getIsImagege() {
            return isImage;
        }

        public void setIsImage(String isImage) {
            this.isImage = isImage ;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images ;
        }


        public String getVideos() {
            return videos;
        }

        public void setVideos(String videos) {
            this.videos = videos ;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt ;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt ;
        }


    }}