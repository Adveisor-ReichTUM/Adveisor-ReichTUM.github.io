package com.adveisor.g2.monopoly.engine.service.model;

public class Message {
    String sender;
    String message;

    public Message(String sender, String message){
        this.sender = sender;
        this.message = message;
    }

    public String getSender(){
        return this.sender;
    }

    public String getMessage(){
        return this.message;
    }
}
