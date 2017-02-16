package br.com.sgdw.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.dto.DatabaseConfig;
import br.com.sgdw.dto.DbConfig;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.CollectorServ;
import br.com.sgdw.service.DataSourceServ;
import br.com.sgdw.util.constantes.CollectionOriginConf;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SGBDs;

@Service
public class DataSourceServImpl implements DataSourceServ {

	@Autowired
	MongoRep mongoRep;
	
	@Autowired
	CollectorServ originRep;
	
	@Override
	public void createDbConfig(DatabaseConfig cfg)
	{
		DbConfig newConfig = new DbConfig(cfg);
		newConfig.set_id(this.getNextSequence(MongoVariables.ORIGIN_CONF_COLLECTION.valor).toString());
		
		this.mongoRep.insertObj(newConfig, MongoVariables.ORIGIN_CONF_COLLECTION.valor);
	}
	
	@Override
	public Boolean testDbConfig(DatabaseConfig cfg)
	{
		DbConfig newConfig = new DbConfig(cfg);
		return this.originRep.checarConexao(newConfig);
	}
	

	@Override
	public List<Map<String, Object>> listSGBDs()
	{
		return SGBDs.getListaSGBDs();
	}	
	
	@Override
	public List<Map<String, Object>> lisRepSGBDs()
	{
		List<Map<String, Object>> dados = this.mongoRep.readCollection(MongoVariables.ORIGIN_CONF_COLLECTION.valor);
		List<Map<String, Object>> listaDeSGBDs = new ArrayList<>();
		
		if(dados != null)
		{
			for(Map<String, Object> sgbd : dados)
			{
				Map<String, Object> map = new HashMap<>();
				map.put(CollectionOriginConf.ID.valor, sgbd.get(CollectionOriginConf.ID.valor));
				map.put(CollectionOriginConf.SGBDID.valor, CollectionOriginConf.getLabel(sgbd.get(CollectionOriginConf.SGBDID.valor)));
				map.put(CollectionOriginConf.URL.valor, sgbd.get(CollectionOriginConf.URL.valor));
				listaDeSGBDs.add(map);
			}
		}
		System.out.println(listaDeSGBDs);
		return listaDeSGBDs;
	}
	
	private Integer getNextSequence(String collectionName)
	{
		Integer sequence = this.mongoRep.countData(collectionName, null) + 1;
		
		return sequence;
	}
}
