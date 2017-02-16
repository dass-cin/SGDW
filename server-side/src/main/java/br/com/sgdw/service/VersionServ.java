package br.com.sgdw.service;

import java.util.Map;

import br.com.sgdw.dto.NovaVersao;

public interface VersionServ {

	public String listVersionsDataset(String collectionName);
	
	public String getDatasetByVersion(String datasetName, String version, String format);

	public Map<String, Object> getMetadataVersion(String collectionName, String version);
	
	public void insertNewVersion(NovaVersao newVersion);
}
