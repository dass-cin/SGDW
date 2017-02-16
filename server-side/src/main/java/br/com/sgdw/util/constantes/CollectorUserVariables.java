package br.com.sgdw.util.constantes;

public enum CollectorUserVariables {

	COLLECTOR_COLLECTION_NAME("collector_users"),
	COLLECTOR_LOGIN("usuario"),
	COLLECTOR_PASSWORD("senha"),
	COLLECTOR_USERNAME("nome");
	
	public String valor;
	
	private CollectorUserVariables(String valor) {
		this.valor = valor;
	}
}
