package br.com.sgdw.service;

import java.util.List;
import java.util.Map;

import br.com.sgdw.dto.DbConfig;

public interface CollectorServ {
		
	public List<Map<String, Object>> executarSql(String query, int inicio, DbConfig cfg);

	public Integer contarLinhas(String sql, DbConfig cfg);
	
	public Boolean checarConexao(DbConfig cfg);
}
