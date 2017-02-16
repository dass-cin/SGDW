package br.com.sgdw.dto;

public class DatabaseConfig {

	private String codigo;
	
	private String sgbdId;
	
	private String url;
	
	private String user;
	
	private String password;
	
	private String dbName;
	
	private String description;
	
	public DatabaseConfig ()
	{
		
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
