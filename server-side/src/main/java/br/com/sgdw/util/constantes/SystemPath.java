package br.com.sgdw.util.constantes;

public enum SystemPath {

	mongoConfPath("src\\main\\java\\br\\com\\collector\\resourses\\templates\\mongoconfig.properties"),
	uriConfPath("src\\main\\java\\br\\com\\collector\\resourses\\templates\\uri.properties"),
	mongoConfFileName("mongoconfig.properties"),
	uriConfFileName("uri.properties");
	
	public String valor;
	
	private SystemPath(String valor) {

		this.valor = valor;
	}
}
