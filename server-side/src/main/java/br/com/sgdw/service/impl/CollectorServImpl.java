package br.com.sgdw.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Service;

import br.com.sgdw.dto.DbConfig;
import br.com.sgdw.service.CollectorServ;
import br.com.sgdw.util.HibernateUtil;

@SuppressWarnings("deprecation")
@Service
public class CollectorServImpl implements CollectorServ{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map<String, Object>> executarSql(String sql, int inicio, DbConfig cfg)
	{
		List<Map<String, Object>> dados = null;
		
		SessionFactory sessionFactory = this.initSession(cfg);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		try
		{
			SQLQuery query  = session.createSQLQuery(sql);
			query.setFirstResult(inicio);
			query.setMaxResults(500);
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			
			dados = query.list();
		}
		catch(HibernateException e)
		{
			System.out.println("Não foi possível executar a query: " + sql);
			e.printStackTrace();
		}
		
		return dados;
	}

	@Override
	public Boolean checarConexao(DbConfig cfg)
	{	
		SessionFactory sessionFactory = this.initSession(cfg);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Boolean resposta = false;
		try{
			resposta = true;
		}
		catch(HibernateException e){
			resposta = false;
		}
		return resposta;
	}
	
	@Override
	public Integer contarLinhas(String sql, DbConfig cfg)
	{
		Integer numeroDeLinhas = null;
		String sqlCount = this.montarCount(sql);
		
		SessionFactory sessionFactory = this.initSession(cfg);
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		try
		{
			numeroDeLinhas = ((Number) session.createSQLQuery(sqlCount).uniqueResult()).intValue();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		
		return numeroDeLinhas;
	}
	
	private String montarCount(String sql)
	{
		StringBuffer sqlCount = new StringBuffer();
		String[] words = sql.split(" ");
		Boolean achou = false;
		
		sqlCount.append("SELECT COUNT(*) ");
		
		for(String i:words)
		{
			if(i.equalsIgnoreCase("FROM"))
			{
				achou = true;
			}
			if(achou)
			{
				sqlCount.append(" "+i);
			}
		}
		
		return sqlCount.toString();
	}
	
	private SessionFactory initSession(DbConfig cfg)
	{
		SessionFactory sessionFactory;
		
		Properties hibernateConf = new Properties();
		String sgbd = cfg.getSgbdId();
		String url = cfg.getUrl();
		String username = cfg.getUser();
		String password = cfg.getPassword();
		
		hibernateConf.setProperty("hibernate.connection.driver_class", HibernateUtil.getDriver(sgbd));
		hibernateConf.setProperty("hibernate.dialect", HibernateUtil.getDialect(sgbd));
		hibernateConf.setProperty("hibernate.connection.url", url);
		hibernateConf.setProperty("hibernate.connection.username", username);
		hibernateConf.setProperty("hibernate.connection.password", password);
		
		Configuration conf = new Configuration();
		
		conf.setProperties(hibernateConf);
		
		ServiceRegistry registry = new StandardServiceRegistryBuilder()
								   .applySettings(conf.getProperties()).build();
		
		sessionFactory= conf.buildSessionFactory(registry);
		
		return sessionFactory;
	}
	
}
