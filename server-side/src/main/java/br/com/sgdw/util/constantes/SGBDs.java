package br.com.sgdw.util.constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum SGBDs {

	POSTGRESQL("1"),
	MYSQL("2"),
	ORACLE("3"),
	SQLSERVER("4");

	public String valor;
	
	private SGBDs(String valor)
	{
		this.valor = valor;
	}
	
	public static String getLabel(int n)
	{
		switch(n)
		{
			case 1:
				return "PostgreSQL";
				
			case 2:
				return "MySQL";
				
			case 3:
				return "Oracle";
				
			case 4:
				return "SQLServer";
				
			default:
				return null;
		}
	}
		
	public static List<Map<String, Object>> getListaSGBDs()
	{
		List<Map<String, Object>> sgbds = new ArrayList<>();
		int tamanho = SGBDs.values().length;
		Map<String, Object> aux;
		
		for(int i = 1; i <= tamanho; i++)
		{
			aux = new HashMap<>();
			aux.put("SGBD", getLabel(i));
			aux.put("id", i);
			
			sgbds.add(aux);
		}
		
		return sgbds;
	}
}
