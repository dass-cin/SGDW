package br.com.sgdw.util.constantes;

public enum CollectionConfVariables {

	COLLECTION_NAME("collector_dataset"),
	COLLECTION_ID_NAME("id_origin"),
	COLLECTION_QUERY("query"),
	COLLECTION_NEXT_UPDATE("next_update"),
	COLLECTION_AUTOR("creator"),
	COLLECTION_UPDATE_FREQUENCY("frequency"),
	COLLECTION_LAST_VERSION("last_version"),
	COLLECTION_KEYWORDS("keywords"),
	COLLECTION_PUBLISHER("publisher"),
	COLLECTION_CONTACT_POINT("contact_point"),
	COLLECTION_PERIOD("period"),
	COLLECTION_SPATIAL_COVERAGE("spatial_coverage"),
	COLLECTION_THEME("theme"),
	COLLECTION_LANGUAGE("language"),
	COLLECTION_DATETIME_FORMATS("datetime_formats"),
	COLLECTION_DESCRIPTION("description"),
	COLLECTION_ID_DB("id_db"),
	COLLECTION_LICENSE("license"),
	COLLECTION_IDENTIFIER_URI("identifier_uri"),
	COLLECTION_TITLE("dataset_title");
	
	public String valor;
	
	private CollectionConfVariables(String valor) {
		this.valor = valor;
	}
}
