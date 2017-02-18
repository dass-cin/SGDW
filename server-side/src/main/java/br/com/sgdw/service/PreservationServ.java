package br.com.sgdw.service;

import br.com.sgdw.dto.PreservarDataset;

public interface PreservationServ {

	public void preservarDataset(PreservarDataset preservacao);
	
	public Boolean verificarPreservacao(String datasetTitle);
	
	public String motivoPreservacao(String datasetTitle);
}
