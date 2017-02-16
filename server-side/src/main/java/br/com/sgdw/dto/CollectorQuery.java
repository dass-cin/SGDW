package br.com.sgdw.dto;

import java.util.Map;

public class CollectorQuery {

	private String datasetName;

	private String format;
	
	private String version;
	
	private Map<String, Object> where;
	
	public CollectorQuery()
	{
		
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public Map<String, Object> getWhere() {
		return where;
	}

	public void setParametros(Map<String, Object> where) {
		this.where = where;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
