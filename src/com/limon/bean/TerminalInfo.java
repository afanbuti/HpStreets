package com.limon.bean;

import java.io.Serializable;

public class TerminalInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1702690658430590262L;
	public String Uid;
	public String Line1number;
	public String Buildmodel;
	public String Versionsdk;
	public String Versionrelease;

	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}

	public String getLine1number() {
		return Line1number;
	}

	public void setLine1number(String line1number) {
		Line1number = line1number;
	}

	public String getBuildmodel() {
		return Buildmodel;
	}

	public void setBuildmodel(String buildmodel) {
		Buildmodel = buildmodel;
	}

	public String getVersionsdk() {
		return Versionsdk;
	}

	public void setVersionsdk(String versionsdk) {
		Versionsdk = versionsdk;
	}

	public String getVersionrelease() {
		return Versionrelease;
	}

	public void setVersionrelease(String versionrelease) {
		Versionrelease = versionrelease;
	}
}
