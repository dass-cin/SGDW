package br.com.sgdw.util;

import br.com.sgdw.util.constantes.DatabaseDialect;
import br.com.sgdw.util.constantes.DatabaseDriver;


public class HibernateUtil {
		
	public static String getDriver(String valor)
	{
		switch (valor) {
		case "1":
			return DatabaseDriver.POSTGRESQL.valor;
		
		case "2":
			return DatabaseDriver.MYSQL.valor;
		
		case "3":
			return DatabaseDriver.ORACLE.valor;	

		case "4":
			return DatabaseDriver.SQLSERVER.valor;	
			
		default:
			return null;
		}
	}
	
	public static String getDialect(String valor)
	{
		switch (valor) {
		case "1":
			return DatabaseDialect.POSTGRESQL.valor;
			
		case "2":
			return DatabaseDialect.MYSQL.valor;
			
		case "3":
			return DatabaseDialect.ORACLE.valor;
		
		case "4":
			return DatabaseDialect.SQLSERVER.valor;
			
		default:
			return null;
		}
	}
}
