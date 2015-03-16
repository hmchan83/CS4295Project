package com.cs4295.team.util;

import java.util.ArrayList;

import android.util.Log;

public class Sharedinfo {
	private static Sharedinfo instance = new Sharedinfo();
	private ArrayList<Teaminfo> teams;
	private Userinfo user;

	public ArrayList<Teaminfo> getTeams() {
		return teams;
	}

	public void setTeams(ArrayList<Teaminfo> teams) {
		Log.d("SHARE","SET TEAMS");
		this.teams = teams;
	}

	public static Sharedinfo getInstance() {
		return instance;
	}

	public static void setInstance(Sharedinfo instance) {
		Sharedinfo.instance = instance;
	}

	public Userinfo getUser() {
		return user;
	}

	public void setUser(Userinfo user) {
		this.user = user;
	}
}
