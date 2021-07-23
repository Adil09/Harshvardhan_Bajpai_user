package com.harshvardhanbajpai;

 class ReplyModel {
    String id,reply,replyto,date;

    public ReplyModel(String id, String reply, String replyto, String date) {
        this.id = id;
        this.reply = reply;
        this.replyto = replyto;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplyto() {
        return replyto;
    }

    public void setReplyto(String replyto) {
        this.replyto = replyto;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
