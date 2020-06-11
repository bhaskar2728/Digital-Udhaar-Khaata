package com.example.digitaludhaarkhata.Model;

public class Chats {
    private String sender;
    private String receiver;
    private String from;
    private String to;
    private String amount;
    private String date;

    public Chats(String sender,String receiver,String to,String from,String amount,String date){
        this.sender = sender;
        this.receiver = receiver;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = date;
    }
    public Chats(){

    }

    public String getDate(){

        return date;
    }
    public String getSender(){

        return  sender;
    }
    public String getReceiver(){

        return  receiver;
    }
    public String getTo(){

        return  to;
    }
    public String getFrom(){

        return  from;
    }
    public String getAmount(){

        return amount;
    }
    public  void setDate(String date){
        this.date = date;
    }
    public void setSender(String sender){
        this.sender = sender;
    }
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    public void setFrom(String from){
        this.from = from;
    }
    public void setTo(String to){
        this.to = to;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }
}
//test