package br.com.sgdw.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.sgdw.dto.DbConfig;
import br.com.sgdw.dto.NovaCollection;
import br.com.sgdw.dto.NovoDataset;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.CollectorServ;
import br.com.sgdw.service.CreateServ;
import br.com.sgdw.service.UpdateServ;
import br.com.sgdw.service.runnable.DataTransformer;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.CollectionHistory;
import br.com.sgdw.util.constantes.Frequency;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SystemMsg;

@Service
public class CreateServImpl implements CreateServ{
	
	@Autowired
	MongoRep mongoRep;
	
	@Autowired
	CollectorServ originRep;
	
	@Autowired
	UpdateServ updateServ;
	
	@Override
	public void createDataset(NovoDataset novo)
	{
		
		NovaCollection newCollection;
		DbConfig cfg = this.mongoRep.getDbConfig(novo.getIdDatabase());
		
		Integer numeroDeLinhas = this.originRep.contarLinhas(novo.getSqlQuery(), cfg);
		
		if(numeroDeLinhas > 0)
		{
			Integer itemInicial = 0;	
			List<Map<String, Object>> dados = null;
			String codVersa = this.getCodVersao();
		
			do
			{
				dados = this.originRep.executarSql(novo.getSqlQuery(), itemInicial, cfg);
				newCollection = new NovaCollection(novo.getIdentifierURI(), novo.getCollectionIdColumnName(), dados, codVersa);
				this.insertCollection(newCollection);
				
				itemInicial += 500;
			}
			while(itemInicial <= numeroDeLinhas+1);
			
			Map<String, Object> novaCollectionMap = new HashMap<>();
			novaCollectionMap.put(MongoVariables.ID_COLLECTION.valor, generateId(novo.getIdentifierURI()));
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_QUERY.valor, novo.getSqlQuery());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_NAME.valor, novo.getIdentifierURI());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_ID_NAME.valor, novo.getCollectionIdColumnName());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor, novo.getUpdateFrequency());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_AUTOR.valor, novo.getCreator());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_NEXT_UPDATE.valor, this.updateServ.getAtualizacaoData(Frequency.getFrequency(novo.getUpdateFrequency())));
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_LAST_VERSION.valor, codVersa);
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_KEYWORDS.valor, novo.getKeywords());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_PUBLISHER.valor, novo.getPublisher());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_CONTACT_POINT.valor, novo.getContactPoint());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_PERIOD.valor, novo.getPeriod());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_SPATIAL_COVERAGE.valor, novo.getSpatialCoverage());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_THEME.valor, novo.getTheme());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_LANGUAGE.valor, novo.getLanguage());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_DATETIME_FORMATS.valor, novo.getDateTimeFormats());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_DESCRIPTION.valor, novo.getDescription());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_ID_DB.valor, novo.getIdDatabase());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor, novo.getIdentifierURI());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_LICENSE.valor, novo.getLicense());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_TITLE.valor, novo.getDatasetTitle());
			novaCollectionMap.put(CollectionConfVariables.COLLECTION_PRESERVE.valor, CollectionConfVariables.PRESERVACAO_DEFAULT.valor);
			
			Map<String, Object> novoHistorico = new HashMap<>();
			novoHistorico.put(CollectionHistory.DATE.valor, new Date().toString());
			novoHistorico.put(CollectionHistory.DESCRIPTION.valor, SystemMsg.HISTORY_FIRST_INSERT.valor);
			novoHistorico.put(CollectionHistory.VERSION.valor, codVersa);
			
			this.mongoRep.insert(novoHistorico, novo.getIdentifierURI()+MongoVariables.HISTORY_COLLECTION.valor);
			this.mongoRep.insert(novaCollectionMap, MongoVariables.CONF_COLLECTION.valor);
		}
		
		new Thread(new DataTransformer(novo.getIdentifierURI(), this.mongoRep)).start();
	}
		
	@Override
	public void insertCollection(NovaCollection newCollection)
	{
		System.out.println("Atualizando "+newCollection.getCollectionName()+"...");
				
		//variaveis auxiliares
		String versaAux = null;
		
		for(Map<String, Object> j : newCollection.getDados())
		{			
			versaAux = j.get(newCollection.getIdName()).toString()+"_"+newCollection.getCodVersa();
			
			j.put(MongoVariables.ID_COLLECTION.valor, versaAux);
			j.remove(newCollection.getIdName());
			j.put("version", newCollection.getCodVersa());
			//j.put("last_version", true);
			
			this.mongoRep.insert(j, newCollection.getCollectionName());
		}
	}
	
	private String getCodVersao()
	{
		Date date = new Date();
		
		String codVersao = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		
		return codVersao;
	}
		
	private String generateId(String collectionName)
	{
		Object salt = null;
		MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();
		String encodePassword = digestPasswordEncoder.encodePassword(collectionName, salt);

		return encodePassword;
	}

	private static MessageDigestPasswordEncoder getInstanceMessageDisterPassword() 
	{
		MessageDigestPasswordEncoder digestPasswordEncoder = new MessageDigestPasswordEncoder("MD5");

		return digestPasswordEncoder;
	}
}
