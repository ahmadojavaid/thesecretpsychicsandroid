package com.jobesk.thesecretpsychics.ClientActivities.Chat;

public class ClientGetChatMessagesModel {


    public static final int TYPE_left = 1;
    public static final int TYPE_right = 2;

    String sendBy, sentTo, message, chatStatus, created_at;

    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
