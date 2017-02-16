package br.com.sgdw.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.sgdw.dto.NovaSenha;
import br.com.sgdw.dto.NovoUsuario;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.service.UserServ;
import br.com.sgdw.util.Sessao;
import br.com.sgdw.util.constantes.CollectorUserVariables;
import br.com.sgdw.util.constantes.MongoVariables;

@Service
public class UserServImpl implements UserServ{

	@Autowired
	MongoRep mongoRep;
	
	private Map<String, Sessao> sessao;
	
	@Override
	public Boolean checarToken(String codigo)
	{
		this.verificarSessao();
		
		Boolean resposta = false;		
		Sessao sessao = this.sessao.get(codigo);
		
		if(sessao != null)
		{
			if(Sessao.verificarExpires(sessao.getExpires()))
			{
				this.sessao.put(codigo, new Sessao(sessao.getUsuario()));
				resposta = true;
			}
			else
			{
				this.sessao.remove(codigo);
			}
		}
		
		return resposta;
	}

	@Override
	public void inserirUsuario(NovoUsuario novoUsuario)
	{
		Map<String, Object> usuario = new HashMap<>();

		String senhaCriptografada = generateHash(novoUsuario.getNovaSenha());

		usuario.put(CollectorUserVariables.COLLECTOR_LOGIN.valor, novoUsuario.getNovoUsuario());
		usuario.put(CollectorUserVariables.COLLECTOR_PASSWORD.valor, senhaCriptografada);
		usuario.put(CollectorUserVariables.COLLECTOR_USERNAME.valor, novoUsuario.getNovoNome());

		this.mongoRep.insert(usuario, CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor);
	}

	@Override
	public void alterarSenha(NovaSenha nova)
	{
		Map<String, Object> usuario = this.mongoRep.getById(nova.getUsuarioAlvo(), MongoVariables.USUARIO_COLLECTION.valor, CollectorUserVariables.COLLECTOR_LOGIN.valor);

		if(usuario != null)
		{
			String novaSenha = generateHash(nova.getNovaSenha());

			this.mongoRep.updatePassword(novaSenha, nova.getNovaSenha());
		}
	}

	@Override
	public String login(String login, String senha)
	{
		Map<String, Object> u = this.mongoRep.getById(login, CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor, CollectorUserVariables.COLLECTOR_LOGIN.valor);

		if(login.equals(MongoVariables.ADMIN_LOGIN.valor) && senha.equals(MongoVariables.ADMIN_PASSWORD.valor) && u == null)
		{
			this.generateAdmin();
			u = this.mongoRep.getById(login, CollectorUserVariables.COLLECTOR_COLLECTION_NAME.valor, CollectorUserVariables.COLLECTOR_LOGIN.valor);
		}


		String codigo = null;
		String loginVerdadeiro = null;
		String senhaVerdadeira = null;
		String nomeUsuario = null;

		if(!u.isEmpty() && u != null)
		{
			loginVerdadeiro = u.get(CollectorUserVariables.COLLECTOR_LOGIN.valor).toString();
			senhaVerdadeira = u.get(CollectorUserVariables.COLLECTOR_PASSWORD.valor).toString();
			nomeUsuario = u.get(CollectorUserVariables.COLLECTOR_USERNAME.valor).toString();

			if(login.equals(loginVerdadeiro) && generateHash(senha).equals(senhaVerdadeira))
			{	
				codigo = generateToken();
				
				this.verificarSessao();
				this.adicionarSessao(codigo, nomeUsuario);	
			}
		}

		return codigo;


	}
	
	private String generateToken()
	{
		String numero = Math.random()+"";
		
		return generateHash(numero);
	}

	private void generateAdmin()
	{
		Map<String, Object> usuario = this.mongoRep.getById(MongoVariables.ADMIN_LOGIN.valor, 
				MongoVariables.USUARIO_COLLECTION.valor, 
				MongoVariables.COLLECTOR_USERNAME.valor);

		if(usuario == null)
		{
			NovoUsuario novoUsuario = new NovoUsuario();
			novoUsuario.setNovoNome(MongoVariables.ADMIN_NAME.valor);
			novoUsuario.setNovoUsuario(MongoVariables.ADMIN_LOGIN.valor);
			novoUsuario.setNovaSenha(MongoVariables.ADMIN_PASSWORD.valor);

			this.inserirUsuario(novoUsuario);
		}
	}

	private String generateHash(String password)
	{
		Object salt = null;
		MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();
		String encodePassword = digestPasswordEncoder.encodePassword(password, salt);

		return encodePassword;
	}

	private static MessageDigestPasswordEncoder getInstanceMessageDisterPassword() 
	{
		MessageDigestPasswordEncoder digestPasswordEncoder = new MessageDigestPasswordEncoder("MD5");

		return digestPasswordEncoder;
	}
	
	private void verificarSessao()
	{
		if(this.sessao == null)
		{
			this.sessao = new HashMap<>();
		}
	}
	
	private void adicionarSessao(String token, String usuario)
	{
		Sessao sessao = new Sessao(usuario);
		
		this.sessao.put(token, sessao);
	}

}
