package br.com.sgdw.repository.mongo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import br.com.sgdw.dto.DbConfig;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.util.MongoUtil;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.CollectorUserVariables;
import br.com.sgdw.util.constantes.MongoVariables;
import br.com.sgdw.util.constantes.SystemMsg;

@Repository
public class MongoRepImpl implements MongoRep{

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> readCollection(String collectionName)
	{
		DBCollection collection = getCollection(collectionName);
		System.out.println(collection.toString());
		List<Map<String, Object>> retorno = new ArrayList<>();
		
		DBObject query = BasicDBObjectBuilder.start().add(MongoVariables.LAST_VERSION.valor, "true").get();
		
		if(collection != null)
		{
			DBCursor cursor;
			
			if((collectionName.equals(MongoVariables.CONF_COLLECTION.valor))||
					(collectionName.equals(MongoVariables.ORIGIN_CONF_COLLECTION.valor))||
					(collectionName.contains(MongoVariables.HISTORY_COLLECTION.valor)))
			{
				cursor = collection.find();
			}
			else
			{
				cursor = collection.find(query);
			}
			
			while(cursor.hasNext())
			{
				DBObject obj = cursor.next();
				retorno.add(obj.toMap());
			}
		}
		
		return retorno;
	}

	private DBCollection getCollection(String collectionName)
	{
		DBCollection collection = null;

		try
		{
			DB db = MongoUtil.getDB();

			collection = db.getCollection(collectionName);
		}
		catch(Exception e)
		{
			System.out.println(SystemMsg.ERRO_CONEXAO_MONGO.valor);
		}

		return collection;
	}

	@Override
	public void insert(Map<String, Object> objeto, String collectionName)
	{
		DBCollection collection = this.getCollection(collectionName);
		BasicDBObjectBuilder docBuilder = null;

		try
		{		
				docBuilder = BasicDBObjectBuilder.start();

				for(Entry<String, Object> entry : objeto.entrySet())
				{
					Object valor = entry.getValue()+"";
					docBuilder.append(entry.getKey(), valor);
				}
				collection.insert(docBuilder.get());

		}
		catch(Exception e)
		{
			System.out.println(SystemMsg.ERRO_CONEXAO_MONGO.valor);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getById(String value, String collectionName, String keyName)
	{
		Map<String, Object> retorno = null;
		
		try
		{
			
			DBCollection collection = getCollection(collectionName);
			
			BasicDBObject query = new BasicDBObject();
			query.put(keyName, value);
			DBCursor cursor = collection.find(query);
			
			DBObject obj = cursor.next();
			retorno = obj.toMap();
		}
		catch(NoSuchElementException e)
		{
			System.out.println(SystemMsg.NAO_ENCONTRADO_MONGO.valor);
		}

		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getByQuery(Map<String, Object> parametros, String collectionName)
	{
		List<Map<String, Object>> retorno = new ArrayList<>();
		
		try
		{
			DBCollection collection = getCollection(collectionName);
			BasicDBObject query = new BasicDBObject();
			
			for(Entry<String, Object> entry : parametros.entrySet())
			{
				query.put(entry.getKey(), java.util.regex.Pattern.compile(entry.getValue().toString()));
			}
			
			DBCursor cursor = collection.find(query);
			
			while(cursor.hasNext())
			{
				DBObject obj = cursor.next();
				retorno.add(obj.toMap());
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println(SystemMsg.NAO_ENCONTRADO_MONGO.valor);
		}
		
		return retorno;

	}
	
	@Override
	public Integer countData(String collectionName, String version)
	{
		Integer contagem = null;
		
		try
		{
			DBCollection collection = getCollection(collectionName);
			
			BasicDBObject query = new BasicDBObject();
			
			if(version != null)
				query.put(MongoVariables.VERSION.valor, version);
			
			contagem = (int) collection.count(query);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return contagem;
	}
		
	@SuppressWarnings({ "unchecked" })
	@Override
	public void setOldVersion(String collectionName)
	{
		DBCollection collection = getCollection(collectionName);
		DBCursor cursor = collection.find();
		
		while(cursor.hasNext())
		{
			DBObject oldVersion = cursor.next();
			Map<String, Object> aux = oldVersion.toMap();
			
			BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
			DBObject query = null;
			 
			for(Entry<String, Object> entry : aux.entrySet())
			{
				if(entry.getKey().equals(MongoVariables.LAST_VERSION.valor))
				{
					builder.append(entry.getKey(), "false");
				}
				else
				{
					if(!entry.getKey().equals(MongoVariables.ID_COLLECTION.valor))
					{
						builder.append(entry.getKey(), entry.getValue()+"");
					}
					else
					{
						query = BasicDBObjectBuilder.start().add(MongoVariables.ID_COLLECTION.valor, entry.getValue()+"").get();
					}
				}
				
			}
			
			collection.update((DBObject) query, builder.get());
			
		}

	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void updatePassword(String newPassword, String login)
	{
		DBCollection collection = getCollection(CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor);
		
		BasicDBObject query = new BasicDBObject();
		query.put(CollectorUserVariables.COLLECTOR_LOGIN.valor, login);
		DBCursor cursor = collection.find(query);
		
		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
		
		Map<String, Object> obj = cursor.next().toMap();
		
		for(Entry<String, Object> entry : obj.entrySet())
		{
			if(entry.getKey().equals(CollectorUserVariables.COLLECTOR_PASSWORD.valor))
			{
				builder.append(entry.getKey(), newPassword);
			}
			else
			{
				builder.append(entry.getKey(), entry.getValue());
			}
		}
		
		collection.update(query, builder.get());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateData(String collecionName, String newDate, String lastVersion, String newQuery)
	{
		DBCollection collection = getCollection(MongoVariables.CONF_COLLECTION.valor);

		BasicDBObject query = new BasicDBObject();
		query.put(CollectionConfVariables.COLLECTION_NAME.valor, collecionName);
		DBCursor cursor = collection.find(query);
		
		while(cursor.hasNext())
		{
			BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
			
			Map<String, Object> map = cursor.next().toMap();
			
			for(Entry<String, Object> entry : map.entrySet())
			{
				if(entry.getKey().equals(CollectionConfVariables.COLLECTION_NEXT_UPDATE.valor))
				{
					builder.append(CollectionConfVariables.COLLECTION_NEXT_UPDATE.valor, newDate);
				}
				else
				{
					if(entry.getKey().equals(CollectionConfVariables.COLLECTION_LAST_VERSION.valor))
					{
						builder.append(CollectionConfVariables.COLLECTION_LAST_VERSION.valor, lastVersion);
					}
					else
					{
						if(entry.getKey().equals(CollectionConfVariables.COLLECTION_QUERY.valor))
						{
							if(newQuery != null)
							{
								builder.append(CollectionConfVariables.COLLECTION_QUERY.valor, newQuery);
							}			
						}
						else
						{
							builder.append(entry.getKey(), entry.getValue());
						}
						
					}
				}
			}
			
			collection.update(query, builder.get());
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDataPage(String datasetName, String version, Integer initial)
	{
		List<Map<String, Object>> dados = new ArrayList<>();
		
		DBCollection collection = getCollection(datasetName);
		
		BasicDBObject query = new BasicDBObject();
		query.put(MongoVariables.VERSION.valor, version);
		
		DBCursor cursor;
		
		try
		{
			cursor = collection.find(query).skip(initial).limit(100);
			
			while(cursor.hasNext())
			{
				dados.add(cursor.next().toMap());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return dados;
	}
	
	@Override
	public void insertObj(Object obj, String collectionName)
	{
		DBCollection collection = getCollection(collectionName);
		
		BasicDBObject document = (BasicDBObject) JSON.parse(new Gson().toJson(obj));
		
		collection.insert(document);
	}
	
	@Override
	public DbConfig getDbConfig(String id)
	{
		DbConfig config = null;
		
		DBCollection collection = getCollection(MongoVariables.ORIGIN_CONF_COLLECTION.valor);
		
		BasicDBObject query = new BasicDBObject();
		query.put(MongoVariables.ID_COLLECTION.valor, id);
		DBCursor cursor = collection.find(query);
		
		DBObject obj = cursor.next();
		
		config = new DbConfig(obj);
		
		return config;
	}
}




