package br.com.sgdw.dto;

import java.util.List;
import java.util.Map;

public class NovaCollection {

	private String collectionName;
	
	private String idName;
	
	private List<Map<String, Object>> dados;
	
	private String codVersa; 
	
	public NovaCollection(String collectionName, String idName, List<Map<String, Object>> dados, String codVersa)
	{
		this.collectionName = collectionName;
		this.idName = idName;
		this.dados = dados;
		this.codVersa = codVersa;
	}
	
	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public List<Map<String, Object>> getDados() {
		return dados;
	}

	public void setDados(List<Map<String, Object>> dados) {
		this.dados = dados;
	}

	public String getCodVersa() {
		return codVersa;
	}

	public void setCodVersa(String codVersa) {
		this.codVersa = codVersa;
	}
	
}
