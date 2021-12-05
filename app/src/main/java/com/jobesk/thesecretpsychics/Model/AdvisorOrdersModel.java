package com.jobesk.thesecretpsychics.Model;

import java.io.Serializable;

public class AdvisorOrdersModel  implements Serializable {

    String email,userImage,heading,body,videLink,date,name,isSeeen,isCompleted;
    String replyHeading,replyDetails,replyVideo;

    String advisorID;
    String id;

    String userID;
    public String getAdvisorID() {
        return advisorID;
    }

    public void setAdvisorID(String advisorID) {
        this.advisorID = advisorID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReplyHeading() {
        return replyHeading;
    }

    public void setReplyHeading(String replyHeading) {
        this.replyHeading = replyHeading;
    }

    public String getReplyDetails() {
        return replyDetails;
    }

    public void setReplyDetails(String replyDetails) {
        this.replyDetails = replyDetails;
    }

    public String getReplyVideo() {
        return replyVideo;
    }

    public void setReplyVideo(String replyVideo) {
        this.replyVideo = replyVideo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsSeeen() {
        return isSeeen;
    }

    public void setIsSeeen(String isSeeen) {
        this.isSeeen = isSeeen;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getVideLink() {
        return videLink;
    }

    public void setVideLink(String videLink) {
        this.videLink = videLink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
