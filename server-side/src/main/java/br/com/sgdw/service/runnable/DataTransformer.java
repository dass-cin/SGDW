package br.com.sgdw.service.runnable;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.scheduling.annotation.Async;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.util.SoUtil;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.MongoVariables;

public class DataTransformer implements Runnable{

	MongoRep mongoRep;
	
	private String datasetName;
	private String version;
	private String path;
	private Integer countLinhas;
	private Integer contador = 1;
	
	List<Map<String, Object>> dados;
	
	//Variáveis para XML
	private Element collection = new Element("XML");
	private Document doc;
	
	//Variáveis RDF
	private Model model;
	private String uriBase =  SoUtil.getUriBase()+"/find/";
	private String domain = SoUtil.getDomain();
	private String uriModel;
	
	//Variáveis JSON
	private List<JsonObject> listJson;
	
	//Variáveis CSV
	private Map<String, Object> firstElement;
	private StringBuilder csv;
	private String delimiter = ";";
	private String lineSeparator = "\n";
	
	public DataTransformer(String datasetName, MongoRep rep) {
		super();
		this.datasetName = datasetName;
		this.mongoRep = rep;
	}

	@Override
	@Async
	public void run() {
		
		init();
		
		Map<String, Object> conf = this.mongoRep.getById(this.datasetName, MongoVariables.CONF_COLLECTION.valor , CollectionConfVariables.COLLECTION_NAME.valor);
		
		this.version = conf.get(CollectionConfVariables.COLLECTION_LAST_VERSION.valor).toString();
		this.countLinhas = this.mongoRep.countData(this.datasetName, this.version);
		
		//this.loadRDF(); //Em desenvolvimento
		this.loadXML();
		this.loadJSON();
		this.loadCSV();
	}

	private void init()
	{
		File diretorio = new File(SoUtil.getRaiz()+"\\data\\"+this.datasetName);
		
		if(!diretorio.exists())
		{
			diretorio.mkdirs();
		}
		
		this.path = diretorio.getPath();
	}
	
	private void loadXML()
	{
		initXML();
		this.contador = 1;
		
		Element elemento = null;
		
		do
		{
			this.dados = this.mongoRep.getDataPage(this.datasetName, this.version, this.contador);
			
			for(Map<String, Object> map : this.dados)
			{
				elemento = new Element(this.datasetName);
			
				for(Entry<String, Object> entry : map.entrySet())
				{
					String[] coluna = entry.getKey().split(":");
					elemento.addContent(new Element(coluna[0]).setText(entry.getValue()+""));
				}
				
				this.doc.getRootElement().addContent(elemento);
			}
			
			this.contador +=100;
		}
		while(this.contador <= this.countLinhas);
	
		this.writeXML();
	}
	
	private void loadRDF()
	{
		initRDF();
		this.contador = 1;
		
		Resource resource;
		
		do
		{
			this.dados = this.mongoRep.getDataPage(this.datasetName, this.version, this.contador);
			
			for(Map<String, Object> map : this.dados)
			{
				resource = model.createResource(uriBase+this.datasetName+"/"+map.get("_id"));
				resource.addProperty(RDF.type, model.createProperty(this.uriModel));
				
				for(Entry<String, Object> entry : map.entrySet())
				{
					if(entry.getKey().contains(":"))
					{
						String[] str = entry.getKey().split(":");
						Property property = this.model.createProperty(this.uriBase+str[0]);
						resource.addProperty(property, this.model.createResource(this.uriBase+str[1]+"/"+entry.getValue()+"/"));
					}
					else
					{
						Property property =  this.model.createProperty(this.uriBase+entry.getKey());
					    resource.addProperty(property, entry.getValue().toString());	
					}
				}
			}
			
			this.contador += 100;
		}
		while(this.contador <= this.countLinhas);
	
		this.writeRDF();
	}
	
	private void loadJSON()
	{
		this.contador = 1;
		
		this.initJSON();
		JsonObject obj = null;
		
		do
		{
			this.dados = this.mongoRep.getDataPage(this.datasetName, this.version, this.contador);
			
			for(Map<String, Object> map : this.dados)
			{
				obj = new JsonObject();
				for(Entry<String, Object> entry : map.entrySet())
				{
					String[] coluna = entry.getKey().split(":");
					obj.addProperty(coluna[0], entry.getValue().toString());
				}
				this.listJson.add(obj);
			}
			
			this.contador += 100;
		}
		while(this.contador <= this.countLinhas);
		
		this.writeJSON();
	}
	
	private void loadCSV()
	{
		this.contador = 1;
		
		initCSV();
		
		do
		{
			this.dados = this.mongoRep.getDataPage(this.datasetName, this.version, this.contador);
			
			if(this.firstElement == null)
			{
				this.firstElement = dados.get(0);
				
				for(Entry<String, Object> entry : firstElement.entrySet())
				{
					String[] coluna = entry.getKey().split(":");
					this.csv.append(coluna[0]);
					this.csv.append(this.delimiter);
				}
				this.csv.append(this.lineSeparator);
			}
			
			for(Map<String, Object> map : this.dados)
			{
				for(Entry<String, Object> entry : map.entrySet())
				{
					this.csv.append(entry.getValue()+"");
					this.csv.append(this.delimiter);
				}
				this.csv.append(this.lineSeparator);
			}
			
			this.contador += 100;
		}
		while(this.contador <= this.countLinhas);
		
		this.writeCSV();
	}
	
	private void initXML()
	{
		if(this.doc == null)
		{
			this.doc = new Document(this.collection);
			this.doc.setRootElement(this.collection);
		}
	}
	
	private void initRDF()
	{
		if(this.model == null)
		{
			this.model = ModelFactory.createDefaultModel();
			this.uriModel = this.uriBase+this.datasetName+"/";
			
			this.model.setNsPrefix(this.domain, this.uriBase);
			this.model.setNsPrefix(this.datasetName, this.uriModel);
		}
	}
	
	private void initJSON()
	{
		if(this.listJson == null)
		{
			this.listJson = new ArrayList<>();
		}
	}
	
	private void initCSV()
	{
		if(this.csv == null)
		{
			this.csv = new StringBuilder();
		}
	}
	
	private void writeXML()
	{
		XMLOutputter xmlOutputter = new XMLOutputter(); 
		xmlOutputter.setFormat(Format.getPrettyFormat());
		
		try {
			FileUtils.writeStringToFile(new File(this.path+"\\"+this.datasetName+"_"+this.version+".xml"), xmlOutputter.outputString(this.doc));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.collection = null;
		this.doc = null;
		System.gc();
	}
	
	private void writeRDF()
	{
		String syntax = "RDF/XML-ABBREV"; // also try "N-TRIPLE" and "TURTLE"
		StringWriter out = new StringWriter();
		model.write(out, syntax);
		String result = out.toString();
		
		try
		{
			FileUtils.writeStringToFile(new File(this.path+"\\"+this.datasetName+"_"+this.version+".rdf"), result);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.model = null;
		System.gc();
	}
	
	private void writeJSON()
	{
		try
		{
			FileUtils.writeStringToFile(new File(this.path+"\\"+this.datasetName+"_"+this.version+".json"), new Gson().toJson(this.listJson));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.listJson = null;
		System.gc();
	}
	
	private void writeCSV()
	{
		try
		{
			FileUtils.writeStringToFile(new File(this.path+"\\"+this.datasetName+"_"+this.version+".csv"), this.csv.toString());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		this.csv = null;
		System.gc();
	}
}
