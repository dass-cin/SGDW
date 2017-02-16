package br.com.sgdw.service;

import java.text.ParseException;

import br.com.sgdw.dto.AtualizacaoManual;
import br.com.sgdw.util.constantes.Frequency;

public interface UpdateServ {
	
	public void atualizacaoManual(AtualizacaoManual atualizacao);

	public void atualizarDatasetsAuto() throws ParseException;	
	
	public String getAtualizacaoData(Frequency frequency);
}
