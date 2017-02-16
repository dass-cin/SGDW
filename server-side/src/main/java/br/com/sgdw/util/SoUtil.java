package br.com.sgdw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import br.com.sgdw.util.constantes.SystemPath;

public class SoUtil {

	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String raiz = null;
		
	private static String uriBase;
	private static String domain;
	
	public static String getRaiz()
	{
		if(OS.indexOf("windows")>=0)
		{
			raiz = "C:\\";
		}
		else
		{
			if(OS.indexOf("linux") >=0)
			{
				//to do...
			}
		}
		return raiz+"DataCollector";
	}
	
	public static void copyProperties(String raiz)
	{
		try {
			FileUtils.copyFile(new File(SystemPath.mongoConfPath.valor), new File(raiz+"\\mongoconfig.properties"));
			FileUtils.copyFile(new File(SystemPath.uriConfPath.valor), new File("\\uri.properties"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static String getUriBase()
	{
		if(uriBase == null)
		{
			initUri();
		}
		
		return uriBase;
	}
	
	public static String getDomain()
	{
		if(domain == null)
		{
			initUri();
		}
		
		return domain;
	}
	
	private static void initUri()
	{
		Properties fileConf = new Properties();
		
		try {
			fileConf.load(new FileInputStream(SoUtil.getRaiz()+"\\"+SystemPath.uriConfFileName.valor));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		uriBase = fileConf.getProperty("uriBase");
		domain = fileConf.getProperty("domain");
	}
	
}
