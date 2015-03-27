package com.cs4295.team.util;

import java.util.Date;

public class Teaminfo {
	private int teamid;
	private String teamname;
	private String desrc;
	private Date create;
	
	public Teaminfo(int teamid, String teamname, String desrc, String create) {
		super();
		this.teamid = teamid;
		this.teamname = teamname;
		this.desrc = desrc;
	}
	
	public Teaminfo(int teamid, String teamname, String desrc, Date create) {
		super();
		this.teamid = teamid;
		this.teamname = teamname;
		this.desrc = desrc;
		this.create = create;
	}
	public int getTeamid() {
		return teamid;
	}
	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}
	public String getTeamname() {
		return teamname;
	}
	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}
	public String getDesrc() {
		return desrc;
	}
	public void setDesrc(String desrc) {
		this.desrc = desrc;
	}
	public Date getCreate() {
		return create;
	}
	public void setCreate(Date create) {
		this.create = create;
	}


}
