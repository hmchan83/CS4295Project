package com.cs4295.team.util;

public class Userinfo {
	
	private int uid;
	private String username;
	private String name;
	private String tel;

	public Userinfo(int uid, String username,String name,String tel) {
		super();
		this.uid = uid;
		this.username = username;
		this.name = name;
		this.tel = tel;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
