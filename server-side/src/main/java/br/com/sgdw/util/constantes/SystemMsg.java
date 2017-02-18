package br.com.sgdw.util.constantes;

public enum SystemMsg {

	HISTORY_FIRST_INSERT("Primeira inserção"),
	HISTORY_AUTO_UPDATE("Atualização automática dos dados"),
	HISTORY_MANUAL_UPDATE("Atualizaçãoo manual (não programada) dos dados"),
	
	ARQUIVO_BANCO_NAO_ENCONTRADO("O arquivo de configuração do banco de origem não foi encontrado"),
	ERRO_DESCONHECIDO("Um erro desconhecido ocorreu"),
	ERRO_NULLPOINTER_CONF("Um erro ocorreu na conexão com o SGBD, verifique o arquivo de configuração do banco de origem está preenchido corretamente"),
	ERRO_CONEXAO_MONGO("Um erro ocorroreu na conexão com o MongoDB"),
	ERRO_HIBERNATE("Um erro ocorreu na consulta sql, verifique se a sintaxe está correta"),
	NAO_ENCONTRADO_MONGO("Este elemento não foi encontrado");
	
	public String valor;
	
	private SystemMsg(String valor) {
		
		this.valor = valor;
	}
}
