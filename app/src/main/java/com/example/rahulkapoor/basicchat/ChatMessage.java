package com.example.rahulkapoor.basicchat;

import java.util.Date;

public class ChatMessage {

    private String messageUser, messageText;

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(final String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(final String messageText) {
        this.messageText = messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(final long messageTime) {
        this.messageTime = messageTime;
    }

    private long messageTime;//bound to this class;


    public ChatMessage(final String msgText, final String msgUser) {

        this.messageText = msgText;
        this.messageUser = msgUser;

        messageTime = new Date().getTime();

    }

    public ChatMessage() {
        //default contructor;
    }


}
