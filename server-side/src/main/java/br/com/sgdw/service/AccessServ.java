package br.com.sgdw.service;

import java.util.List;
import java.util.Map;

import br.com.sgdw.dto.CollectorQuery;

public interface AccessServ {

	@Deprecated
	public String getCompleteCollection(String collectionName, String format);

	public String getById(String collectionName, String id, String format);

	public String getDatasetsNames();

	public String getByQuery(CollectorQuery query);

	public Map<String, Object> getMetadata(String collectionName);
	
	public String listDatasetsAdmin();

	public String getCompleteDataset(String datasetName, String format);
		
	List<Map<String, Object>> listFormats();
}
