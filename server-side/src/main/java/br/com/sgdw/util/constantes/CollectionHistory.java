package br.com.sgdw.util.constantes;

public enum CollectionHistory {
	
	ID("_id"),
	DATASET_NAME("dataset_name"),
	VERSION("version"),
	DATE("date"),
	DESCRIPTION("description"),
	MOTIVO("motivo");
	
	public String valor;
	
	private CollectionHistory(String valor) {
		this.valor = valor;
	}
	
}
