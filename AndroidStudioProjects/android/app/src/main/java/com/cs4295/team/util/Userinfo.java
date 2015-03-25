package com.cs4295.team.util;

public class Userinfo {
	
	private int uid;
	private String username;

	public Userinfo(int uid, String username) {
		super();
		this.uid = uid;
		this.username = username;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
