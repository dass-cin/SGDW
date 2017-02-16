package br.com.sgdw.service;

public interface IdentifierServ {
	
	public String criarIdentificador(String datasetTitle);
	
	public Boolean validarIdentificador(String uri);
	
}
