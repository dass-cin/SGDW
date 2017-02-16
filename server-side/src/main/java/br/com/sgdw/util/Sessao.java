package br.com.sgdw.util;

import java.util.Calendar;
import java.util.Date;

public class Sessao {

	private String usuario;
	
	private Date expires;
	
	public Sessao()
	{
		
	}

	public Sessao(String usuario)
	{
		this.usuario = usuario;
		this.expires = this.criarExpires();		
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}
	
	private Date criarExpires()
	{
		Calendar calentar = Calendar.getInstance();
		calentar.setTime(new Date());
		calentar.add(Calendar.HOUR_OF_DAY, 1);
		
		return calentar.getTime();
	}
	
	public static Boolean verificarExpires(Date expires)
	{
		Boolean resposta = false;
		
		if(expires.compareTo(new Date()) > 0)
		{
			resposta = true;
		}
		
		return resposta;
	}
}
