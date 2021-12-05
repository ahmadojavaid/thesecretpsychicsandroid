package com.jobesk.thesecretpsychics.Model;

public class ClientPsychicModel {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ROW = 1;


    private int type;

    String isLiveChat,isLiveVideo;
    String screenName;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getIsLiveChat() {
        return isLiveChat;
    }

    public void setIsLiveChat(String isLiveChat) {
        this.isLiveChat = isLiveChat;
    }


    public String getIsLiveVideo() {
        return isLiveVideo;
    }

    public void setIsLiveVideo(String isLiveVideo) {
        this.isLiveVideo = isLiveVideo;
    }

    public String getAdvisorID() {
        return advisorID;
    }

    String profileVideo;

    public String getProfileVideo() {
        return profileVideo;
    }

    public void setProfileVideo(String profileVideo) {
        this.profileVideo = profileVideo;
    }

    public void setAdvisorID(String advisorID) {
        this.advisorID = advisorID;
    }

    String psychicID,email,isOnline,serviceName,advisorID;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String image,name,description,ratting;

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPsychicID() {
        return psychicID;
    }

    public void setPsychicID(String psychicID) {
        this.psychicID = psychicID;
    }

    public static int getTypeHeader() {
        return TYPE_HEADER;
    }

    public static int getTypeRow() {
        return TYPE_ROW;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }
}
