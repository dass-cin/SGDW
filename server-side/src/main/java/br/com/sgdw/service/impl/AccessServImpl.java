package br.com.sgdw.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.util.Nonbinding;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.sgdw.dto.CollectorQuery;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.AccessServ;
import br.com.sgdw.util.SoUtil;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.Formats;
import br.com.sgdw.util.constantes.Frequency;
import br.com.sgdw.util.constantes.MongoVariables;

import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AccessServImpl implements AccessServ{

	@Autowired
	MongoRep mongoRep;
	
	@Deprecated
	@Override
	public String getCompleteCollection(String collectionName, String format)
	{
		Formats formatEnum = Formats.getFormat(format);
		
		String retorno = null;
		
		List<Map<String, Object>> dados = this.mongoRep.readCollection(collectionName);
		
		if(!dados.isEmpty() && dados != null)
		{
			switch(formatEnum)
			{
				case JSON:
					retorno = getJson(dados);
				break;	
					
				case XML:
					retorno = getXml(dados, collectionName);
				break;	
				
				case CSV:
					retorno = getCsv(dados);
				break;
				
				//case RDF:
				//	retorno = getRdf(dados, collectionName);
				//break;
					
				default:
					retorno = getJson(dados);
			}
		}
		return retorno;
	}
	
	@Override
	public String getCompleteDataset(String datasetName, String format)
	{
		Formats formatEnum = null;
		String dadosStr = null;
		String lastVersion = null;
		String fileName = null;
		String extencao = null;
		String path = null;
		
		Map<String, Object> conf = this.mongoRep.getById(datasetName, MongoVariables.CONF_COLLECTION.valor, CollectionConfVariables.COLLECTION_NAME.valor);
		
		if(conf != null)
		{
			lastVersion = conf.get(CollectionConfVariables.COLLECTION_LAST_VERSION.valor).toString();
			fileName = datasetName+"_"+lastVersion;
			formatEnum = Formats.getFormat(format);
			
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
			fileName += extencao;
		}
		
		path = SoUtil.getRaiz()+"\\data\\"+datasetName+"\\"+fileName;
		
		try {
			File file = new File(path);
			dadosStr = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			dadosStr = null;
		}
		
		return dadosStr;
	}
	
	@Override
	public String listDatasetsAdmin()
	{
		return new Gson().toJson(this.mongoRep.readCollection(MongoVariables.CONF_COLLECTION.valor));
	}
	
	@Override
	public String getById(String collectionName, String id, String format)
	{
		Formats formatEnum = Formats.getFormat(format);
		
		String retorno = null;
		
		Map<String, Object> dado = null;
		
		if(!id.contains("_"))
		{
			String version = this.mongoRep.getById(collectionName, MongoVariables.CONF_COLLECTION.valor, CollectionConfVariables.COLLECTION_NAME.valor)
							 .get(CollectionConfVariables.COLLECTION_LAST_VERSION.valor).toString();
			dado = this.mongoRep.getById(id+"_"+version, collectionName,  MongoVariables.ID_COLLECTION.valor);
		}
		else
		{
			dado = this.mongoRep.getById(id, collectionName, MongoVariables.ID_COLLECTION.valor);
		}
				
		if(dado != null)
		{
			//dado.remove(MongoVariables.LAST_VERSION.valor);
			//dado.remove(MongoVariables.VERSION.valor);
			
			List<Map<String, Object>> listaDados = new ArrayList<>();
			listaDados.add(dado);
			
			switch (formatEnum) {
				
				case JSON:
					retorno = getJson(listaDados);
				break;
				
				case XML:
					retorno = getXml(listaDados, collectionName);	
				break;	
				
				case CSV:
					retorno = getCsv(listaDados);
				break;
				
				//case RDF:
				//	retorno = getRdf(listaDados, collectionName);
				//break;	
					
				default:
					retorno = getJson(listaDados);
			}
		}
		return retorno;
	}
	
	@Deprecated
	@Override
	public String getByQuery(CollectorQuery query)
	{	
		String retorno = null;
		
		Map<String, Object> parametros = query.getWhere();
		System.out.println(query.getWhere());
		Formats formatEnum = Formats.getFormat(query.getFormat());
		
		/*if(query.getVersion() == null)
		{
			parametros.put(MongoVariables.VERSION.valor,this.mongoRep.getById(query.getDatasetName(), MongoVariables.CONF_COLLECTION.valor, CollectionConfVariables.COLLECTION_NAME.valor)
							 .get(CollectionConfVariables.COLLECTION_LAST_VERSION.valor).toString());
		}
		else
		{
			parametros.put(MongoVariables.VERSION.valor, query.getVersion());
		}
		*/
		
		List<Map<String, Object>> dados = this.mongoRep.getByQuery(parametros, MongoVariables.CONF_COLLECTION.valor);
		System.out.println(dados);
		if(!dados.isEmpty() && dados != null)
		{
			switch(formatEnum)
			{
				case JSON:
					retorno = getJson(dados);
				break;	
					
				case XML:
					retorno = getXml(dados, query.getDatasetName());
				break;	
				
				case CSV:
					retorno = getCsv(dados);
				break;
				
				//case RDF:
				//	retorno = getRdf(dados, query.getDatasetName());
				//break;
					
				default:
					retorno = getJson(dados);
			}
		}
		
		return retorno;
	}
	
	@Override
	public String getDatasetsNames()
	{
		List<Map<String, Object>> dados = this.mongoRep.readCollection(MongoVariables.CONF_COLLECTION.valor);
		List<Map<String, Object>> listaDeDatasets = new ArrayList<>();
		String preservado;
		
		if(dados != null)
		{
			for(Map<String, Object> dataset : dados)
			{	
				preservado = dataset.get(CollectionConfVariables.COLLECTION_PRESERVE.valor).toString();
				if (preservado.equals(CollectionConfVariables.PRESERVACAO_DEFAULT.valor)) {//remove os preservados
					Map<String, Object> map = new HashMap<>();
					map.put(CollectionConfVariables.COLLECTION_NAME.valor, dataset.get(CollectionConfVariables.COLLECTION_NAME.valor));
					map.put(CollectionConfVariables.COLLECTION_TITLE.valor, dataset.get(CollectionConfVariables.COLLECTION_TITLE.valor));
					map.put(CollectionConfVariables.COLLECTION_DESCRIPTION.valor, dataset.get(CollectionConfVariables.COLLECTION_DESCRIPTION.valor));
					map.put(CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor, dataset.get(CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor));
					map.put(CollectionConfVariables.COLLECTION_THEME.valor, dataset.get(CollectionConfVariables.COLLECTION_THEME.valor));
					map.put(CollectionConfVariables.COLLECTION_KEYWORDS.valor, dataset.get(CollectionConfVariables.COLLECTION_KEYWORDS.valor));
					map.put(CollectionConfVariables.COLLECTION_PUBLISHER.valor, dataset.get(CollectionConfVariables.COLLECTION_PUBLISHER.valor));
					listaDeDatasets.add(map);
				}
			}
		}
		
		return new Gson().toJson(listaDeDatasets);
	}
	
	@Override
	public Map<String, Object> getMetadata(String collectionName)
	{
		
		Map<String, Object> collection  = this.mongoRep.getById(collectionName, MongoVariables.CONF_COLLECTION.valor,  CollectionConfVariables.COLLECTION_NAME.valor);
		
		if(!collection.isEmpty() && collection != null)
		{
			collection.remove(CollectionConfVariables.COLLECTION_QUERY.valor);
			collection.remove(CollectionConfVariables.COLLECTION_ID_DB.valor);
			collection.remove(CollectionConfVariables.COLLECTION_ID_NAME.valor);
			collection.remove(MongoVariables.ID_COLLECTION.valor);
			collection.remove(CollectionConfVariables.COLLECTION_NAME.valor);
			String preservacao = Frequency.getFrequencyName(collection.get(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor).toString());
			if (preservacao == CollectionConfVariables.PRESERVACAO_DEFAULT.valor){
				collection.remove(CollectionConfVariables.COLLECTION_PRESERVE_DESCRIPTION.valor);
				collection.remove(CollectionConfVariables.COLLECTION_PRESERVE.valor);			
			}
						
			String frequencia = Frequency.getFrequencyName(collection.get(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor).toString());
			collection.remove(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor);
			collection.put(CollectionConfVariables.COLLECTION_UPDATE_FREQUENCY.valor, frequencia);
			
			String uri = collection.get(CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor).toString();
			collection.remove(CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor);
			collection.put(CollectionConfVariables.COLLECTION_IDENTIFIER_URI.valor, makeUrl() + "/find/" + uri);
			
			
		}
		
		return collection;
	}
	
	@Override
	public List<Map<String, Object>> listFormats()
	{
		return Formats.getListaFormats();
	}
	
    public String makeUrl() {        
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        return servletRequest.toString();
    }
	
	private String getJson(List<Map<String, Object>> dados)
	{	
		List<JsonObject> listaJson = new ArrayList<>();
		JsonObject obj = null;
		
		for(Map<String, Object> map : dados)
		{
			obj = new JsonObject();
			for(Entry<String, Object> entry : map.entrySet())
			{
				String[] coluna = entry.getKey().split(":");
				obj.addProperty(coluna[0], entry.getValue().toString());
			}
			listaJson.add(obj);
		}
		
		return new Gson().toJson(listaJson);
	}
	
	private String getXml(List<Map<String, Object>> dados, String collectionName)
	{
		Element collection = new Element("XML");
		Document doc = new Document(collection);
		doc.setRootElement(collection);
		
		for(Map<String, Object> map : dados)
		{
			Element elemento = new Element(collectionName);
			
			for(Entry<String, Object> entry : map.entrySet())
			{
				String[] coluna = entry.getKey().split(":");
				elemento.addContent(new Element(coluna[0]).setText(entry.getValue()+""));
			}
			doc.getRootElement().addContent(elemento);
		}
		
		XMLOutputter xmlOutputter = new XMLOutputter();
		xmlOutputter.setFormat(Format.getPrettyFormat());
		
		String retorno = xmlOutputter.outputString(doc);
		
		return retorno;
	}
	
	private String getCsv(List<Map<String, Object>> dados)
	{
		String delimiter = ";";
		String lineSeparator = "\n";
		
		StringBuilder csv = new StringBuilder();
		
		Map<String, Object> firstElement = dados.get(0);
		
		for(Entry<String, Object> entry : firstElement.entrySet())
		{
			String[] coluna = entry.getKey().split(":");
			csv.append(coluna[0]);
			csv.append(delimiter);
		}
		
		csv.append(lineSeparator);
		
		for(Map<String, Object> map : dados)
		{
			for(Entry<String, Object> entry : map.entrySet())
			{
				csv.append(entry.getValue()+"");
				csv.append(delimiter);
			}
			csv.append(lineSeparator);
		}
		return csv.toString();
		
	}
	
	@Nonbinding
	private String getRdf(List<Map<String, Object>> dados, String collectionName)
	{
		Model model = ModelFactory.createDefaultModel();
		
		String uriBase =  SoUtil.getUriBase()+"/find/";
		String domain = SoUtil.getDomain();
		
		String uriModel = uriBase+collectionName+"/";
		
		Resource resource;
		
		
		model.setNsPrefix(domain, uriBase);
		model.setNsPrefix(collectionName, uriModel);
		
		for(Map<String, Object> map : dados)
		{
			resource = model.createResource(uriBase+collectionName+"/"+map.get("_id"));
			
			resource.addProperty(RDF.type, model.createProperty(uriModel));
		
			for(Entry<String, Object> entry : map.entrySet())
			{
				if(entry.getKey().contains(":"))
				{
					String[] str = entry.getKey().split(":");
					Property property = model.createProperty(uriBase+str[0]);
					resource.addProperty(property, model.createResource(uriBase+str[1]+"/"+entry.getValue()+"/"));
				}
				else
				{
					Property property =  model.createProperty(uriBase+entry.getKey());
				    resource.addProperty(property, entry.getValue().toString());	
				}  
			}
		}
		
		String syntax = "RDF/XML-ABBREV"; // also try "N-TRIPLE" and "TURTLE"
		StringWriter out = new StringWriter();
		model.write(out, syntax);
		String result = out.toString();
		
		//model.write(System.out);
		
		return result;
	}
}


