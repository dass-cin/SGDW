package br.com.sgdw.dto;

public class NovaSenha {
	
	private String usuarioAlvo;
	
	private String novaSenha;
	
	private String codigo;
	
	public NovaSenha()
	{
		
	}

	public String getUsuarioAlvo() {
		return usuarioAlvo;
	}

	public void setUsuarioAlvo(String usuarioAlvo) {
		this.usuarioAlvo = usuarioAlvo;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	
}
