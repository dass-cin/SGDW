package br.com.sgdw.service;

import java.util.List;
import java.util.Map;

import br.com.sgdw.dto.DatabaseConfig;

public interface DataSourceServ {

	public void createDbConfig(DatabaseConfig cfg);

	public Boolean testDbConfig(DatabaseConfig cfg);

	List<Map<String, Object>> listSGBDs();
	
	List<Map<String, Object>> lisRepSGBDs();
}
