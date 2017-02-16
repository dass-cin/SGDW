package br.com.sgdw.repository.mongo;

import java.util.List;
import java.util.Map;

import br.com.sgdw.dto.DbConfig;

public interface MongoRep {

	public List<Map<String, Object>> readCollection(String collectionName);
	
	public void insert(Map<String, Object> objeto, String collectionName);
	
	public Map<String, Object> getById(String value, String collectionName, String key);
	
	public void setOldVersion(String collectionName);

	public void updatePassword(String newPassword, String login);

	public void updateData(String collecionName, String newDate, String lastVersion, String newQuery);

	public List<Map<String, Object>> getByQuery(Map<String, Object> parametros, String collectionName);

	Integer countData(String collectionName, String version);

	List<Map<String, Object>> getDataPage(String datasetName, String version, Integer initial);

	void insertObj(Object obj, String collectionName);

	DbConfig getDbConfig(String id);

}
