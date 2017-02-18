package br.com.sgdw.service.impl;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.dto.PreservarDataset;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.AccessServ;
import br.com.sgdw.service.PreservationServ;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.MongoVariables;

@Service
public class PreservationServImpl implements PreservationServ {
	
	@Autowired
	MongoRep mongoRep;
	
	@Autowired
	AccessServ collectorServ;
	
	@Override
	public void preservarDataset(PreservarDataset preservacao) {
		Map<String, Object> configCollection = this.mongoRep.getById(preservacao.getDatasetUri(), MongoVariables.CONF_COLLECTION.valor, CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor);
		String collectionName = configCollection.get(CollectionConfVariables.COLLECTION_NAME.valor).toString();
		this.mongoRep.updateMetadata(collectionName, preservacao.getType(), preservacao.getDescription());
	}

	@Override
	public Boolean verificarPreservacao(String datasetTitle){
		Boolean preservado = false; 
		Map<String, Object> metadados = this.collectorServ.getMetadata(datasetTitle);
		for(Entry<String, Object> entry : metadados.entrySet())
		{
			if(entry.getKey().equals(CollectionConfVariables.COLLECTION_PRESERVE.valor))
			{
				if (!entry.getValue().equals(CollectionConfVariables.PRESERVACAO_DEFAULT.valor)) {
					preservado = true;
				}
				
			}
		}
		return preservado;
	}
	
	@Override
	public String motivoPreservacao(String datasetTitle){
		String motivo = ""; 
		Map<String, Object> metadados = this.collectorServ.getMetadata(datasetTitle);
		for(Entry<String, Object> entry : metadados.entrySet())
		{
			if(entry.getKey().equals(CollectionConfVariables.COLLECTION_PRESERVE_DESCRIPTION.valor))
			{
				motivo = entry.getValue().toString();				
			}
		}
		return motivo;
	}
}
