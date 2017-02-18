package br.com.sgdw.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.dto.AtualizacaoManual;
import br.com.sgdw.dto.DbConfig;
import br.com.sgdw.dto.NovaCollection;
import br.com.sgdw.dto.NovaVersao;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.CollectorServ;
import br.com.sgdw.service.CreateServ;
import br.com.sgdw.service.UpdateServ;
import br.com.sgdw.service.VersionServ;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.Frequency;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SystemMsg;

@Service
public class UpdateServImpl implements UpdateServ{

	@Autowired
	MongoRep mongoRep;
	
	@Autowired
	CollectorServ originRep;
	
	@Autowired
	CreateServ createServ;
	
	@Autowired
	VersionServ versionServ;

	@Override
	public void atualizarDatasetsAuto() throws ParseException
	{
		List<Map<String, Object>> conf = this.mongoRep.readCollection(MongoVariables.CONF_COLLECTION.valor);
		String collectionName = null;
		String sql = null;
		String idName = null;
		String idDatabase = null;
		DbConfig dbConfig;
		String codVersa = this.getCodVersao();
		NovaCollection newCollection;
		String novaDataAtualizacao;
		String preservacao;
		
		Integer numLinhas = null;
		Integer count = 0;
		
		
		System.out.println("Atualizando dados...");
		
		for(Map<String, Object> i : conf)
		{	
			preservacao = i.get(CollectionConfVariables.COLLECTION_PRESERVE.valor).toString();
			if (preservacao.equals(CollectionConfVariables.PRESERVACAO_DEFAULT.valor)) {
				if(verificarDataAtualizacao(i.get(CollectionConfVariables.COLLECTION_NEXT_UPDATE.valor).toString()))
				{
					sql = i.get(CollectionConfVariables.COLLECTION_QUERY.valor).toString();
					collectionName = i.get(CollectionConfVariables.COLLECTION_NAME.valor).toString();
					idName = i.get(CollectionConfVariables.COLLECTION_ID_NAME.valor).toString();
					idDatabase = i.get(CollectionConfVariables.COLLECTION_ID_DB.valor).toString();
					
					dbConfig = this.mongoRep.getDbConfig(idDatabase);
					this.mongoRep.setOldVersion(collectionName);
					
					numLinhas = this.originRep.contarLinhas(sql, dbConfig);
						
					do
					{
						List<Map<String, Object>> dadosOrigem = this.originRep.executarSql(sql, count, dbConfig);
						newCollection = new NovaCollection(collectionName, idName, dadosOrigem, codVersa);				
						this.createServ.insertCollection(newCollection);
						count += 500;
					}
					while(count < numLinhas);
					
					//Nova versão
					novaDataAtualizacao = this.getAtualizacaoData(Frequency.getFrequency(i.get(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor).toString()));
					NovaVersao newVersion = new NovaVersao(collectionName, new Date().toString(), SystemMsg.HISTORY_AUTO_UPDATE.valor, codVersa, novaDataAtualizacao, sql, SystemMsg.HISTORY_AUTO_UPDATE.valor); 
					this.versionServ.insertNewVersion(newVersion);
	
				}
			}
		}
	}
		
	@Override
	public void atualizacaoManual(AtualizacaoManual atualizacao)
	{
		String query = null;
		String idCollection = null;
		String codVersa = this.getCodVersao();
		String collectionName = null;
		DbConfig dbConfig;
		String idDatabase = null;
		Integer numLinhas = null;
		String novaDataAtualizacao;
		Integer count = 0;
		NovaCollection newCollection;
		String motivo;
		String preservacao;
		
		Map<String, Object> configCollection = this.mongoRep.getById(atualizacao.getDatasetUri(), MongoVariables.CONF_COLLECTION.valor, CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor);
		preservacao = configCollection.get(CollectionConfVariables.COLLECTION_PRESERVE.valor).toString();
		if (preservacao.equals(CollectionConfVariables.PRESERVACAO_DEFAULT.valor)) {
			
			collectionName = configCollection.get(CollectionConfVariables.COLLECTION_NAME.valor).toString();
		
		query = configCollection.get(CollectionConfVariables.COLLECTION_QUERY.valor).toString();
		idCollection = configCollection.get(CollectionConfVariables.COLLECTION_ID_NAME.valor).toString();
		idDatabase = configCollection.get(CollectionConfVariables.COLLECTION_ID_DB.valor).toString();
		dbConfig = this.mongoRep.getDbConfig(idDatabase);						
		numLinhas = this.originRep.contarLinhas(query, dbConfig);
		motivo = atualizacao.getMotivo();		
		System.out.println("MOTIVO:"+motivo);
		do
		{
			List<Map<String, Object>> dadosOrigem = this.originRep.executarSql(query, count, dbConfig);
			newCollection = new NovaCollection(collectionName, idCollection, dadosOrigem, codVersa);				
			this.createServ.insertCollection(newCollection);
			count += 500;
		}
		while(count < numLinhas);
		
		//Nova versão
		novaDataAtualizacao = this.getAtualizacaoData(Frequency.getFrequency(configCollection.get(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor).toString()));
		NovaVersao newVersion = new NovaVersao(collectionName, new Date().toString(), SystemMsg.HISTORY_MANUAL_UPDATE.valor, codVersa, novaDataAtualizacao, query, motivo); 
		this.versionServ.insertNewVersion(newVersion);
		}
		
	}
	
	
	private String getCodVersao()
	{
		Date date = new Date();
		
		String codVersao = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		
		return codVersao;
	}
	
	private Boolean verificarDataAtualizacao(String dataStr) throws ParseException
	{
		Boolean resposta = false;
		Date dataAtual = new Date();
		SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy h", Locale.US);
		
		String dataAtualStr = format.format(dataAtual);
		Date dataCollection = (Date)format.parse(dataStr);
		dataAtual = (Date)format.parse(dataAtualStr);
		System.out.println(dataCollection + "|v:" + dataAtual);		
		if(dataCollection.compareTo(dataAtual) <=0)
		{
			resposta = true;
		}
		
		return resposta;
	}
	
	@Override
	public String getAtualizacaoData(Frequency frequency)
	{
		SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy h", Locale.US);
		Calendar calentar = Calendar.getInstance();
		calentar.setTime(new Date());
		
		switch (frequency) {
		case POR_HORA:
			calentar.set(Calendar.HOUR_OF_DAY, calentar.get(Calendar.HOUR_OF_DAY)+1);
		break;
		
		case DIARIO:
			calentar.set(Calendar.DAY_OF_MONTH, calentar.get(Calendar.DAY_OF_MONTH)+1);
		break;	
			
		case SEMANAL:
			calentar.set(Calendar.WEEK_OF_MONTH, calentar.get(Calendar.WEEK_OF_MONTH)+1);
		break;	
			
		case MENSAL:
			calentar.set(Calendar.MONTH, calentar.get(Calendar.MONTH)+1);
		break;
		
		case SEMESTRAL:
			calentar.set(Calendar.MONTH, calentar.get(Calendar.MONTH)+6);
		break;
			
		case ANUAL:
			calentar.set(Calendar.YEAR, calentar.get(Calendar.YEAR)+1);
		break;
		
		default:
			break;
		}
		
		return format.format(calentar.getTime());
	}
	
}
