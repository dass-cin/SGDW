package br.com.sgdw.util.constantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Formats {

	JSON("json"),
	XML("xml"),
	//RDF("rdf"),
	CSV("csv");
	
	public String valor;
	
	private Formats(String valor) {
		this.valor = valor;
	}

	public String getValor(){
		return this.valor;
	}
	
	public static Formats getFormat(String f)
	{
		switch(f)
		{
			case "json":
				return JSON;
				
			case "xml":
				return XML;
				
			case "csv":
				return CSV;
				
			//case "rdf":
			//	return RDF;
				
			default:
				return JSON;
		}
	}
	
	public static List<Map<String, Object>> getListaFormats()
	{
		List<Map<String, Object>> formats = new ArrayList<>();
		Map<String, Object> aux;
		
		for(Formats fmt : Formats.values())
		{
			aux = new HashMap<>();
			aux.put("Formato", fmt.getValor());
			formats.add(aux);
		}
		
		return formats;
	}
}
