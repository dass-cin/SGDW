package br.com.sgdw.service;
import br.com.sgdw.dto.NovaCollection;
import br.com.sgdw.dto.NovoDataset;

public interface CreateServ {

	public void createDataset(NovoDataset novo);
	
	public void insertCollection(NovaCollection newCollection);
}
