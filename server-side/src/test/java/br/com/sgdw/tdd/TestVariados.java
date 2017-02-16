package br.com.sgdw.tdd;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import br.com.sgdw.dto.CollectorQuery;
import br.com.sgdw.repository.mongo.MongoRep;
import br.com.sgdw.repository.mongo.impl.MongoRepImpl;
import br.com.sgdw.service.CollectorServ;
import br.com.sgdw.service.impl.CollectorServImpl;
import br.com.sgdw.service.impl.IdentifierServImpl;
import br.com.sgdw.service.impl.UpdateServImpl;
import br.com.sgdw.util.HibernateUtil;
import br.com.sgdw.util.MongoUtil;
import br.com.sgdw.util.constantes.Frequency;
import br.com.sgdw.service.IdentifierServ;


public class TestVariados {

	//@Test
	public void testarCount()
	{
		String sql = "SELECT P.SEXO AS \"column_sexo\",P.ESTADO_CIVIL AS \"column_estado_civil\" FROM pessoa P where rownum < 1000";
		
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
		
		System.out.println(sqlCount.toString());
	}
	
	//@Test
	public void testarIdentificador(){
		IdentifierServImpl uri = new IdentifierServImpl();
		System.out.println(uri.criarIdentificador("Atores de Pernambuco"));
		System.out.println(uri.criarIdentificador("Título de Conjunto de dados#ãã"));
		System.out.println(uri.criarIdentificador("Título de Conjunto de dadosééé"));		
		System.out.println(uri.criarIdentificador("Estou testando,; errei a digitação"));
		System.out.println(uri.criarIdentificador("Seria uma URI válida com #?"));
		System.out.println(uri.criarIdentificador("Quais caracteres não $# é possível : ter [] nas URIs'"));
		System.out.println(uri.criarIdentificador("/testando"));
		System.out.println(uri.criarIdentificador("2 23 23 42 42 "));
		System.out.println(uri.criarIdentificador("[A é Í o u ç ã Ã Ç"));		
		System.out.println(uri.criarIdentificador("¨::;;;(()))==++ªº|"));	
		System.out.println(uri.criarIdentificador("Gisele Bündchen da Conceição e Silva foi batizada assim em homenagem à sua conterrânea de Horizontina, RS."));
		
	}
	
	//@Test
	public void testarMongo()
	{
		DB db = MongoUtil.getDB();
		
		DBCollection col = db.getCollection("conf");
		
		DBCursor cursor = col.find();
		
		while(cursor.hasNext())
		{
			System.out.println(cursor.next());
		}
	}
		
	//@Test
	public void testarConsulta()
	{
		MongoRep rep = new MongoRepImpl();
		
		List<Map<String, Object>> out = rep.readCollection("usuario");
		
		System.out.println(new Gson().toJson(out));
	}
	
	//@Test
	public void testarConsultaUnica()
	{
		MongoRep rep = new MongoRepImpl();
		
		Map<String, Object> out = rep.getById("1", "usuario", "identificador");
		
		System.out.println(new Gson().toJson(out));
	}
	
	//@Test
	public void testarMap()
	{
		CollectorQuery q = new CollectorQuery();
		
		q.setDatasetName("filipe");
		
		Map<String, Object> f = new HashMap<>();
		
		f.put("idade", 25);
		f.put("sexo", "M");
		
		q.setParametros(f);
		
		System.out.println(new Gson().toJson(q));
	}
	
}
