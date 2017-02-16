package br.com.sgdw.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import br.com.sgdw.util.constantes.SystemPath;

public class MongoUtil {
	
	private static DB db;

	@SuppressWarnings({ "deprecation", "resource" })
	public static DB getDB()
	{
		
		try {
			
			if(db == null)
			{
				Properties confProp = new Properties();
				confProp.load(new FileInputStream(SoUtil.getRaiz()+"\\"+SystemPath.mongoConfFileName.valor));
				
				MongoClient mongo = new MongoClient(confProp.getProperty("url"));
				db = mongo.getDB("datacollector");
			}
			
			return db;
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return db;
	}
	
}
