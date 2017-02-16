package br.com.sgdw.util.constantes;

public enum MongoVariables {
	
	USUARIO_COLLECTION("collector_users"),
	CONF_COLLECTION("collector_conf"),
	TOKEN_COLLECTION("collector_token"),
	HISTORY_COLLECTION("_history"),
	ORIGIN_CONF_COLLECTION("collector_origin_conf"),
	
	
	ADMIN_LOGIN("admin"),
	ADMIN_PASSWORD("password"),
	ADMIN_NAME("Administrador"),
	
	COLLECTOR_ID("collector_id"),
	LAST_VERSION("last_version"),
	ID_COLLECTION("_id"),
	COLLECTOR_USERNAME("usuario"),
	COLLECTOR_PASSWORD("senha"),
	VERSION("version");
	
	public String valor;
	
	private MongoVariables(String valor)
	{
		this.valor = valor;
	}
}
