package br.com.sgdw.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import br.com.sgdw.service.VersionServ;
import br.com.sgdw.service.runnable.DataTransformer;
import br.com.sgdw.dto.NovaVersao;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.util.SoUtil;
import br.com.sgdw.util.constantes.CollectionHistory;
import br.com.sgdw.util.constantes.Formats;
import br.com.sgdw.util.constantes.MongoVariables;

@Service
public class VersionServImpl implements VersionServ{

	@Autowired
	MongoRep mongoRep;
	
	@Override
	public String listVersionsDataset(String collectionName){
		
		List<Map<String, Object>> dados = this.mongoRep.readCollection(collectionName + MongoVariables.HISTORY_COLLECTION.valor);
		List<Map<String, Object>> listaDeVersoes = new ArrayList<>();
				
		if(dados != null)
		{
			for(Map<String, Object> versao : dados)
			{
				Map<String, Object> map = new HashMap<>();
				map.put(CollectionHistory.VERSION.valor, versao.get(CollectionHistory.VERSION.valor));
				map.put(CollectionHistory.DATE.valor, versao.get(CollectionHistory.DATE.valor));
				map.put(CollectionHistory.DESCRIPTION.valor, versao.get(CollectionHistory.DESCRIPTION.valor));
				
				listaDeVersoes.add(map);
			}
		}
		
		return new Gson().toJson(listaDeVersoes);
		
	}
	
	@Override
	public String getDatasetByVersion(String datasetName, String version, String format)
	{
		String dadosStr = null;
		
		String filename = datasetName + "_" + version;
		String path = SoUtil.getRaiz()+"\\data\\"+datasetName+"\\";
		String extencao = null;
		
		Formats formatEnum = Formats.getFormat(format);
		
		switch(formatEnum)
		{
			case JSON:
				extencao = ".json";
			break;	
				
			case XML:
				extencao = ".xml";
			break;	
			
			case CSV:
				extencao = ".csv";
			break;
			
			//case RDF:
			//	extencao = ".rdf";
			//break;
				
			default:
				extencao = ".json";
		}
		
		path += filename + extencao;
		
		try {
			File file = new File(path);
			dadosStr = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dadosStr;
	}
	
	
	@Override
	public Map<String, Object> getMetadataVersion(String collectionName, String version)
	{
		
		Map<String, Object> collection  = this.mongoRep.getById(version, collectionName + MongoVariables.HISTORY_COLLECTION.valor,  CollectionHistory.VERSION.valor);
		if(!collection.isEmpty() && collection != null)
		{
			collection.remove(CollectionHistory.ID.valor);		
			
		}
		return collection;
	}
	
	@Override
	public void insertNewVersion(NovaVersao newVersion){
		
		Map<String, Object> novoHistorico = null;
		
		novoHistorico = new HashMap<>();
		novoHistorico.put(CollectionHistory.DATE.valor, newVersion.getData());
		novoHistorico.put(CollectionHistory.DESCRIPTION.valor, newVersion.getDescription());
		novoHistorico.put(CollectionHistory.VERSION.valor, newVersion.getVersion());
		novoHistorico.put(CollectionHistory.MOTIVO.valor, newVersion.getMotivo());
				
		this.mongoRep.insert(novoHistorico, newVersion.getCollectionName()+MongoVariables.HISTORY_COLLECTION.valor);
		this.mongoRep.updateData(newVersion.getCollectionName(),newVersion.getNovaDataAtualizacao(), newVersion.getVersion(), newVersion.getQuery());
		
		new Thread(new DataTransformer(newVersion.getCollectionName(), this.mongoRep)).start();
		
	}
	
	
	
}
