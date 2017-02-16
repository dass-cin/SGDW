package br.com.sgdw.util.constantes;

public enum Frequency {

	POR_HORA(0),
	DIARIO(1),
	SEMANAL(2),
	MENSAL(3),
	SEMESTRAL(4),
	ANUAL(5);
	
	public Integer valor;
	
	private Frequency(Integer valor) {
		this.valor = valor;
	}
	
	
	public static Frequency getFrequency(String f)
	{
		switch(f)
		{
			case "0":
				return Frequency.POR_HORA;
				
			case "1":
				return Frequency.DIARIO;
				
			case "2":
				return Frequency.SEMANAL;
				
			case "3":
				return Frequency.MENSAL;
				
			case "4":
				return Frequency.SEMESTRAL;
				
			case "5":
				return Frequency.ANUAL;
				
			default:
				return Frequency.MENSAL;
		}
	}
	
	public static String getFrequencyName(String f)
	{
		
		switch(f)
		{
			case "0":
				return "Every Hour";
			
			case "1":
				return "Daily";
				
			case "2":
				return "Weekly";
				
			case "3":
				return "Monthly";
				
			case "4":	
				return "Semester";
			
			case "5":
				return "Yearly";
				
			default:
				return null;
				
		}
	}
}
