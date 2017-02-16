package br.com.sgdw.util.constantes;

public enum CollectionOriginConf {

	SGBDID("sgbdId"),
	ID("_id"),
	URL("url");
	
	public String valor;
	
	private CollectionOriginConf(String valor) {
		this.valor = valor;
	}
	
	public static String getLabel(Object n)
	{
		switch(n.toString())
		{
			case "1":
				return "PostgreSQL";
				
			case "2":
				return "MySQL";
				
			case "3":
				return "Oracle";
				
			case "4":
				return "SQLServer";
				
			default:
				return null;
		}
	}
	
}
