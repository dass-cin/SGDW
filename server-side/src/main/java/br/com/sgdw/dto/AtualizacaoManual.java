package br.com.sgdw.dto;

public class AtualizacaoManual{

	private String datasetUri;
	
	private String newQuery; //nao obrigatorio
	
	private String description;
	
	private String newId;
	
	private String codigo;
	
	private String motivo;
	
	public AtualizacaoManual()
	{
		
	}
	
	public String getDatasetUri() {
		return datasetUri;
	}

	public void setDatasetUri(String datasetUri) {
		this.datasetUri = datasetUri;
	}
	
	public String getNewQuery() {
		return newQuery;
	}

	public void setNewQuery(String newQuery) {
		this.newQuery = newQuery;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNewId() {
		return newId;
	}

	public void setNewId(String newId) {
		this.newId = newId;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}	
	
}
