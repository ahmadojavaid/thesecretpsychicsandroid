package com.jobesk.thesecretpsychics.Model;

import java.io.Serializable;

public class ClientOrdersModel implements Serializable {

    String id, advisorId, userId, order_heading, order_details, order_video, order_status, created_at, name;
    String reply_heading, reply_details, reply_Video, appIcons, categoryName;

    String image;
    String isLiveChat,isOnline;

    String textChatRate;

    String isSeen, isCompleted, isReviewed;


    public String getTextChatRate() {
        return textChatRate;
    }

    public void setTextChatRate(String textChatRate) {
        this.textChatRate = textChatRate;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsLiveChat() {
        return isLiveChat;
    }

    public void setIsLiveChat(String isLiveChat) {
        this.isLiveChat = isLiveChat;
    }

    public String getIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(String isReviewed) {
        this.isReviewed = isReviewed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrder_heading() {
        return order_heading;
    }

    public void setOrder_heading(String order_heading) {
        this.order_heading = order_heading;
    }

    public String getOrder_details() {
        return order_details;
    }

    public void setOrder_details(String order_details) {
        this.order_details = order_details;
    }

    public String getOrder_video() {
        return order_video;
    }

    public void setOrder_video(String order_video) {
        this.order_video = order_video;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReply_heading() {
        return reply_heading;
    }

    public void setReply_heading(String reply_heading) {
        this.reply_heading = reply_heading;
    }

    public String getReply_details() {
        return reply_details;
    }

    public void setReply_details(String reply_details) {
        this.reply_details = reply_details;
    }

    public String getReply_Video() {
        return reply_Video;
    }

    public void setReply_Video(String reply_Video) {
        this.reply_Video = reply_Video;
    }

    public String getAppIcons() {
        return appIcons;
    }

    public void setAppIcons(String appIcons) {
        this.appIcons = appIcons;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
