package com.xigua.model;

import java.io.Serializable;

public class serviceVlanModel implements Serializable{

	/**
	 * xigua
	 */
	private static final long serialVersionUID = -8281088680433666834L;
	
	private String netVlan ;
	
	private String usrVlan;

	public String getNetVlan() {
		return netVlan;
	}

	public void setNetVlan(String netVlan) {
		this.netVlan = netVlan;
	}

	public String getUsrVlan() {
		return usrVlan;
	}

	public void setUsrVlan(String usrVlan) {
		this.usrVlan = usrVlan;
	}
	
}
