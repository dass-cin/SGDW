package br.com.sgdw.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import br.com.sgdw.dto.CollectorQuery;
import br.com.sgdw.dto.TokenAutenticacao;
import br.com.sgdw.service.AccessServ;
import br.com.sgdw.service.UpdateServ;
import br.com.sgdw.service.VersionServ;
import br.com.sgdw.util.constantes.Formats;

@Api(value = "/open")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/open")
public class OpenRest {
	
	@Autowired
	UpdateServ updateServ;
	
	@Autowired
	AccessServ collectorServ;
	
	@Autowired
	VersionServ versionServ;
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Retornar dados de um conjunto de dados", nickname = "buscar", 
			notes="Esta rota permite retornar dados de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}")
	public String buscarTudo(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response)
	{	
		    getResponse(Formats.JSON.valor, response, datasetTitle);
			return this.collectorServ.getCompleteDataset(datasetTitle, Formats.JSON.valor);
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Listar versões de um conjunto de dados", nickname = "buscarVersoes", 
			notes="Esta rota permite realizar listar as versões de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/list_versions")
	public String buscarVersoes(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response)
	{	
		    getResponse(Formats.JSON.valor, response, datasetTitle);
		    return this.versionServ.listVersionsDataset(datasetTitle);
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Filtrar dados de um conjunto de dados", nickname = "buscarPorId", 
			notes="Esta rota permite filtrar os dados de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/{id}")
	public String buscarPorId(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("id") String id, HttpServletResponse response)
	{	
		    getResponse(Formats.JSON.valor, response, datasetTitle);
			return this.collectorServ.getById(datasetTitle, id, Formats.JSON.valor);	
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Formatar conjunto de dados em uma distribuição (formato) especificada", nickname = "buscarTudoComFormato", 
			notes="Esta rota permite formatar conjunto de dados em um formato especificado")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/format/{formato}")
	public String buscarTudoComFormato(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("formato") String formato, HttpServletResponse response)
	{	
			//String dados = collectorServ.getCompleteCollection(datasetTitle, formato);
			String dados = this.collectorServ.getCompleteDataset(datasetTitle, formato);
			if (dados == null) {

				dados = this.collectorServ.getCompleteCollection(datasetTitle, formato);
			}
			if(dados != null)
			{
				getResponse(formato, response, datasetTitle);
			} 
			return dados;
	}

	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Retornar dados de uma versão em um formato específico", nickname = "buscarPorVersao", 
			notes="Esta rota permite retornar dados de uma versão em um formato específico")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/version/{versionId}/format/{format}")
	public String buscarPorVersao(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("versionId") String versionId, @PathVariable("format") String formato, HttpServletResponse response)
	{
		String dados = this.versionServ.getDatasetByVersion(datasetTitle, versionId, formato);
		
		if(dados != null)
		{
			this.getResponse(formato, response, datasetTitle);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		return dados;
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Download de uma versão do conjunto de dados em um formato específico", nickname = "buscarPorVersaoDownload", 
			notes="Esta rota permite fazer download de uma versão do conjunto de dados em um formato específico")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/version/{versionId}/format/{format}/download")
	public String buscarPorVersaoDownload(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("versionId") String versionId, @PathVariable("format") String formato, HttpServletResponse response)
	{
		String dados = this.versionServ.getDatasetByVersion(datasetTitle, versionId, formato);
		
		if(dados != null)
		{
			getResponseDownload(formato, response, datasetTitle);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		return dados;
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Download de parte dos dados em um formato específico", nickname = "buscarPorIdFormatoDownload", 
			notes="Esta rota permite fazer download de parte dos dados em um formato específico")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/{id}/format/{formato}/download")
	public String buscarPorIdFormatoDownload(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("id") String id, @PathVariable("formato") String formato, HttpServletResponse response)
	{	
			String dados = this.collectorServ.getById(datasetTitle, id, formato);
			
			if(dados != null)
			{
				getResponseDownload(formato, response, datasetTitle);
			}			
			return dados;	
	}	
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Download do conjunto de dados em um formato específico", nickname = "buscarTudoComFormatoDownload", 
			notes="Esta rota permite fazer download dos dados em um formato específico")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/format/{formato}/download")	
	public String buscarTudoComFormatoDownload(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("formato") String formato, HttpServletResponse response)
	{	
			//String dados = collectorServ.getCompleteCollection(datasetTitle, formato);
			String dados = this.collectorServ.getCompleteDataset(datasetTitle, formato);
			
			if(dados != null)
			{
				getResponseDownload(formato, response, datasetTitle);
			}
			return dados;
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos conjuntos de dados (público)", nickname = "listDatasetsP", 
			notes="Esta rota permite listar os conjuntos de dados")
	@RequestMapping(method = RequestMethod.GET, path="/list_datasets")		
	public String listDatasets(HttpServletResponse response)
	{
		getResponse("json", response, null);
		
		return this.collectorServ.getDatasetsNames();
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos metadados de um conjunto de dados", nickname = "getMetadata", 
			notes="Esta rota permite listar os metadados de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/about")		
	public String getMetadata(@PathVariable("datasetTitle") String datasetTitle, HttpServletResponse response)
	{
		getResponse("json", response, null);
		
		return new Gson().toJson(this.collectorServ.getMetadata(datasetTitle));
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos metadados de uma versão do conjuntos de dados", nickname = "getMetadataVersion", 
			notes="Esta rota permite listar os metadados de uma versão do conjuntos de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/about/{version}")		
	public String getMetadataVersion(@PathVariable("datasetTitle") String datasetTitle, @PathVariable("version") String version, HttpServletResponse response)
	{
		getResponse("json", response, null);
		
		return new Gson().toJson(this.versionServ.getMetadataVersion(datasetTitle, version));
	}
	
	
	@CrossOrigin(methods = {RequestMethod.POST})
	@ApiOperation(value = "Buscar conjuntos de dados", nickname = "execute_query", 
			notes="Esta rota permite buscar os conjuntos de dados")
	@RequestMapping(method = RequestMethod.POST, path="/execute_query")			
	public String executeQuery(@RequestBody CollectorQuery query,  HttpServletResponse response)
	{
		getResponse(query.getFormat(), response, query.getDatasetName());
		
		return this.collectorServ.getByQuery(query);
	}
	
	@CrossOrigin(methods = {RequestMethod.GET})
    @ApiOperation(value = "Listar formatos de dados implementados", nickname = "list_formats", 
    		notes="Esta rota permite listar os formatos de dados implementados")
    @RequestMapping(method = RequestMethod.GET, path="/list_formats")	
	public List<Map<String, Object>> listFormats(HttpServletResponse response)
	{
		return this.collectorServ.listFormats();
	}
	
	private void getResponseDownload(String format, HttpServletResponse response, String collection)
	{
		response.setHeader("Content-Disposition", "attachment; filename=\""+collection+"."+format+"\"");
	}
	
	private void getResponse(String format, HttpServletResponse response, String collection)
	{
		Formats formatEnum = Formats.getFormat(format);
		
		switch(formatEnum)
		{
			//case RDF:
			//	response.setContentType(MediaType.APPLICATION_XML_VALUE);
			//break;
			
			case JSON:
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			break;
			
			case CSV:
				response.setHeader("Content-Disposition", "attachment; filename=\""+collection+".csv\"");
			break;
			
			case XML:
				response.setContentType(MediaType.APPLICATION_XML_VALUE);
			break;
			
			default:
				response.setContentType(MediaType.APPLICATION_XML_VALUE);
			break;	
			
			
		}

	}
}
