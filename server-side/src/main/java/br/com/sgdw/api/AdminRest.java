package br.com.sgdw.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import br.com.sgdw.dto.AtualizacaoManual;
import br.com.sgdw.dto.DatabaseConfig;
import br.com.sgdw.dto.NovaSenha;
import br.com.sgdw.dto.NovoDataset;
import br.com.sgdw.dto.NovoUsuario;
import br.com.sgdw.dto.PreservarDataset;
import br.com.sgdw.dto.TokenAutenticacao;
import br.com.sgdw.dto.UsuarioAutenticacao;
import br.com.sgdw.dto.UriValidacao;
import br.com.sgdw.service.AccessServ;
import br.com.sgdw.service.CreateServ;
import br.com.sgdw.service.DataSourceServ;
import br.com.sgdw.service.IdentifierServ;
import br.com.sgdw.service.PreservationServ;
import br.com.sgdw.service.UpdateServ;
import br.com.sgdw.service.UserServ;

@Api(value = "/admin")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/admin")
public class AdminRest {

	@Autowired
	UpdateServ updateServ;
	
	@Autowired
	AccessServ accessServ;
	
	@Autowired
	CreateServ createServ;
	
	@Autowired
	UserServ userServ;
	
	@Autowired
	AccessServ collectorServ;
	
	@Autowired
	DataSourceServ dataSourceServ;
	
	@Autowired
	IdentifierServ identifierServ;
	
	@Autowired
	PreservationServ preservationServ;
	
	@CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Adiciona novo conjunto de dados", nickname = "add_dataset", 
    		notes="Esta rota permite adicionar um novo conjunto de dados")
    @RequestMapping(method = RequestMethod.POST, path="/add_dataset")
	public void addDataset(@RequestBody NovoDataset novo, HttpServletResponse response)
	{
		if(this.userServ.checarToken(novo.getCodigo()))
		{
			this.createServ.createDataset(novo);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	@CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Adiciona novo Produtor (usuário)", nickname = "add_user", 
    		notes="Esta rota permite adicionar um novo usuário/produtor")
    @RequestMapping(method = RequestMethod.POST, path="/add_user")	
    public void addUser(@RequestBody NovoUsuario novo, HttpServletResponse response)
	{
		if(this.userServ.checarToken(novo.getCodigo()))
		{
			this.userServ.inserirUsuario(novo);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	@CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Alterar senha de acesso", nickname = "alter_password", 
    		notes="Esta rota permite alterar a senha de acesso de um login (produtor)")
    @RequestMapping(method = RequestMethod.POST, path="/alter_password")	
	public void alterPassword(@RequestBody NovaSenha nova, HttpServletResponse response)
	{
		if(this.userServ.checarToken(nova.getCodigo()))
		{
			this.userServ.alterarSenha(nova);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Efetuar login e gerar token", nickname = "login", 
    		notes="Esta rota permite efetuar login na ferramenta gerando um token de acesso")
    @RequestMapping(method = RequestMethod.POST, path="/login")	
	public @ResponseBody TokenAutenticacao login(@RequestBody UsuarioAutenticacao autenticacao, HttpServletResponse response)
	{
		TokenAutenticacao tokenAutenticacao = null;
		String tk = this.userServ.login(autenticacao.getUsuario(), autenticacao.getSenha());
		
		if(tk != null)
		{
			response.setStatus(HttpServletResponse.SC_OK);
			tokenAutenticacao = new TokenAutenticacao();
			tokenAutenticacao.setCodigo(tk);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return tokenAutenticacao;
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Verifica se o token é válido", nickname = "checar_token_usuario", 
    		notes="Esta rota permite verificar se o token é válido")
    @RequestMapping(method = RequestMethod.POST, path="/checar_token_usuario")	
	public @ResponseBody Boolean checarToken(@RequestBody TokenAutenticacao tk, HttpServletResponse response)
	{	
    	return this.userServ.checarToken(tk.getCodigo());    	
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Criar URI persistente", nickname = "criar_uri", 
    		notes="Esta rota permite criar uma URI persistente")
    @RequestMapping(method = RequestMethod.POST, path="/criar_uri")	
	public @ResponseBody String criarURI(@RequestBody UriValidacao uri, HttpServletResponse response){
		String uriStr = "";
		
		if(this.userServ.checarToken(uri.getCodigo()))
		{
			uriStr = this.identifierServ.criarIdentificador(uri.getUri());
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		return uriStr;
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Checar se a URI é válida", nickname = "checar_uri", 
    		notes="Esta rota permite checar se uma URI é válida")
    @RequestMapping(method = RequestMethod.POST, path="/checar_uri")	
	public @ResponseBody Boolean checarURI(@RequestBody UriValidacao uri, HttpServletResponse response){
		Boolean valido = false;
		
		if(this.userServ.checarToken(uri.getCodigo()))
		{
			valido = this.identifierServ.validarIdentificador(uri.getUri());
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		return valido;
	}
    
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Lista dos conjuntos de dados cadastrados (padrão produtor)", nickname = "list_datasets", 
    		notes="Esta rota permite listar os conjuntos de dados com campos adicionais apenas para o Produtor")
    @RequestMapping(method = RequestMethod.POST, path="/list_datasets")	
	public String listDatasets(@RequestBody TokenAutenticacao tk, HttpServletResponse response)
	{
		String datasets = null;
		
		if(this.userServ.checarToken(tk.getCodigo()))
		{
			datasets = this.collectorServ.listDatasetsAdmin();
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		return datasets;
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Realizar atualização manual de um conjunto de dados", nickname = "atualizar_dataset", 
    		notes="Esta rota permite atualizar um conjunto de dados")
    @RequestMapping(method = RequestMethod.POST, path="/atualizar_dataset")	
	public void editDataset(@RequestBody AtualizacaoManual atualizacaoManual, HttpServletResponse response)
	{
		if(this.userServ.checarToken(atualizacaoManual.getCodigo()))
		{
			System.out.println("Atualização Manual!");
			this.updateServ.atualizacaoManual(atualizacaoManual);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Adicionar nova fonte de dados", nickname = "add_database", 
    		notes="Esta rota permite adicionar uma nova fonte de dados")
    @RequestMapping(method = RequestMethod.POST, path="/add_database")	
	public void addDatabase(@RequestBody DatabaseConfig confg, HttpServletResponse response)
	{
		if(this.userServ.checarToken(confg.getCodigo()))
		{
			this.dataSourceServ.createDbConfig(confg);
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Testar fonte de dados", nickname = "test_database", 
    		notes="Esta rota permite testar se uma fonte de dados permite o acesso")
    @RequestMapping(method = RequestMethod.POST, path="/test_database")	
	public void testDatabase(@RequestBody DatabaseConfig confg, HttpServletResponse response)
	{
		if(this.userServ.checarToken(confg.getCodigo()))
		{
			this.dataSourceServ.testDbConfig(confg);
			
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}	
    
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Listar fontes de dados cadastradas", nickname = "list_databases", 
    		notes="Esta rota permite listar as fontes de dados cadastradas")
    @RequestMapping(method = RequestMethod.POST, path="/list_databases")	
	public String listDatabase(@RequestBody TokenAutenticacao tk, HttpServletResponse response)
	{
		List<Map<String, Object>> listDatabase = null;
		if(this.userServ.checarToken(tk.getCodigo()))
		{
			listDatabase = this.dataSourceServ.lisRepSGBDs();
		} 
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return new Gson().toJson(listDatabase);
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Listar tipos de fontes de dados permitidas", nickname = "list_databases_types", 
    		notes="Esta rota permite listar os tipos de fontes de dados permitidas")
    @RequestMapping(method = RequestMethod.POST, path="/list_databases_types")	
	public String listDatabaseTypes(@RequestBody TokenAutenticacao tk, HttpServletResponse response)
	{
		List<Map<String, Object>> listDatabaseType = null;
		if(this.userServ.checarToken(tk.getCodigo()))
		{
			listDatabaseType = this.dataSourceServ.listSGBDs();
		} 
		else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return new Gson().toJson(listDatabaseType);
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Listar formatos de dados implementados", nickname = "list_formats", 
    		notes="Esta rota permite listar os formatos de dados implementados")
    @RequestMapping(method = RequestMethod.POST, path="/list_formats")	
	public String listFormats(@RequestBody TokenAutenticacao tk, HttpServletResponse response)
	{
		List<Map<String, Object>> listFormats = null;
		if(this.userServ.checarToken(tk.getCodigo()))
		{
			listFormats = this.accessServ.listFormats();
		} 
		else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return new Gson().toJson(listFormats);
	}
	
    @CrossOrigin(methods = {RequestMethod.POST})
    @ApiOperation(value = "Realizar preservação de um conjunto de dados", nickname = "preservar_dataset", 
    		notes="Esta rota permite preservar um conjunto de dados")
    @RequestMapping(method = RequestMethod.POST, path="/preservar_dataset")	
	public void preservarDataset(@RequestBody PreservarDataset preservarDataset, HttpServletResponse response)
	{
		if(this.userServ.checarToken(preservarDataset.getCodigo()))
		{
			System.out.println("Preservação!");
			this.preservationServ.preservarDataset(preservarDataset);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
    
    @CrossOrigin(methods = {RequestMethod.GET})
	@ApiOperation(value = "Lista dos metadados de um conjunto de dados", nickname = "getMetadata", 
			notes="Esta rota permite listar os metadados de um conjunto de dados")
	@RequestMapping(method = RequestMethod.GET, path="/{datasetTitle}/about/{codigo}")		
	public String getMetadata(@PathVariable("datasetTitle") String datasetTitle,@PathVariable("codigo") String codigo, HttpServletResponse response)
	{
    	String resultado = "";
		if(this.userServ.checarToken(codigo))
		{
			resultado = new Gson().toJson(this.accessServ.getMetadata(datasetTitle));
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return resultado;
	}
	
}
