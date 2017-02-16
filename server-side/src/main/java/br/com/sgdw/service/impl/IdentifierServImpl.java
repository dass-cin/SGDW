package br.com.sgdw.service.impl;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.IdentifierServ;
import br.com.sgdw.util.constantes.CollectionConfVariables;
import br.com.sgdw.util.constantes.MongoVariables;

@Service
public class IdentifierServImpl implements IdentifierServ {
	
	@Autowired
	MongoRep mongoRep;
	
	public String criarIdentificador(String datasetTitle) {
		datasetTitle = Normalizer.normalize(datasetTitle, Normalizer.Form.NFD);
		datasetTitle = datasetTitle.replaceAll("[^\\p{ASCII}]", "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("."), "-");
		datasetTitle = datasetTitle.replaceAll("#","");
		datasetTitle = datasetTitle.replaceAll("\t \n \r", "");
		datasetTitle = datasetTitle.replaceAll("\u00A9","");
		datasetTitle = datasetTitle.replaceAll("[\"]", "");
		datasetTitle = datasetTitle.replaceAll("[/]", "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("["), "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("]"), "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("{"), "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("}"), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("~"), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("´"), "");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("="), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("`"), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("'"), "");		
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("¨"), "");	
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("&"), "-");
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("@"), "");		
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("%"), "");			
		datasetTitle = datasetTitle.replaceAll(Pattern.quote ("}"), "");			
		datasetTitle = datasetTitle.replaceAll(" ","-");
		
		while (!validarIdentificador(datasetTitle)){
			datasetTitle = datasetTitle + "-";
		}
		
		return datasetTitle;
	}
	
	public Boolean validarIdentificador(String uri) {
		
		Boolean valida = true;
		List<Map<String, Object>> dados = this.mongoRep.readCollection(MongoVariables.CONF_COLLECTION.valor);	
		if(dados != null)
		{
			for(Map<String, Object> dataset : dados)
			{
				if (dataset.get(CollectionConfVariables.COLLECTION_NAME.valor).equals(uri)) {
					valida = false;
				}
			}
		
		}
		return valida;		
		
	}
		
}
