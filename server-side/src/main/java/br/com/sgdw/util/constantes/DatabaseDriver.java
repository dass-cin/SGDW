package br.com.sgdw.util.constantes;

public enum DatabaseDriver {

	POSTGRESQL("org.postgresql.Driver"),
	MYSQL("com.mysql.jdbc.Driver"),
	ORACLE("oracle.jdbc.OracleDriver"),
	SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	
	public String valor;
	
	private DatabaseDriver(String valor) {
		this.valor = valor;
	}
	
}
