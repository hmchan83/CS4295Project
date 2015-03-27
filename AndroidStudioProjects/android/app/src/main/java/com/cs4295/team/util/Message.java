package com.cs4295.team.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marcus on 3/27/2015.
 */
public class Message {
    private int msgid;
    private int replyid;
    private int teamid;
    private int uid;
    private String msg;
    private String title;
    private Date date;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Message(String date, int msgid, int replyid, int teamid, int uid, String msg, String title) {
        try {
            this.date = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            this.date = new Date();
        }
        this.msgid = msgid;
        this.replyid = replyid;
        this.teamid = teamid;
        this.uid = uid;
        this.msg = msg;
        this.title = title;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public int getReplyid() {
        return replyid;
    }

    public void setReplyid(int replyid) {
        this.replyid = replyid;
    }

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
