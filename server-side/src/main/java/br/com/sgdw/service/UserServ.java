package br.com.sgdw.service;

import br.com.sgdw.dto.NovaSenha;
import br.com.sgdw.dto.NovoUsuario;

public interface UserServ {
	
	public void inserirUsuario(NovoUsuario novoUsuario);

	public void alterarSenha(NovaSenha nova);

	public String login(String login, String senha);

	public Boolean checarToken(String codigo);

}
