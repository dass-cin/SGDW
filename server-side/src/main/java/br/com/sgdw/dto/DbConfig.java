package br.com.sgdw.dto;

import com.mongodb.DBObject;

public class DbConfig {

	private String _id;
	
	private String sgbdId;
	
	private String url;
	
	private String user;
	
	private String password;
	
	private String bdName;
	
	private String description;
	
	public DbConfig()
	{
		
	}
	
	public DbConfig(DBObject dbObject)
	{
		this.sgbdId = (String) dbObject.get("sgbdId");
		this.url = (String) dbObject.get("url");
		this.user = (String) dbObject.get("user");
		this.password = (String) dbObject.get("password");
	}
	
	public DbConfig(DatabaseConfig cfg)
	{
		this.sgbdId = cfg.getSgbdId();
		this.url = cfg.getUrl();
		this.user = cfg.getUser();
		this.password = cfg.getPassword();
		this.bdName = cfg.getDbName();
		this.description = cfg.getDescription();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSgbdId() {
		return sgbdId;
	}

	public void setSgbdId(String sgbdId) {
		this.sgbdId = sgbdId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBdName() {
		return bdName;
	}

	public void setBdName(String bdName) {
		this.bdName = bdName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
