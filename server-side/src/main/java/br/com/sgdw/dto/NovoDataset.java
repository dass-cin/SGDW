package br.com.sgdw.dto;

public class NovoDataset {
	
	private String datasetTitle;
	
	private String collectionIdColumnName;
	
	private String sqlQuery;
	
	private String codigo; //autenticacao
	
	private String updateFrequency;
	
	private String creator;
	
	private String idCollection; // nao obrigatorio para insersao
	
	private String keywords;
	
	private String publisher;
	
	private String contactPoint;
	
	private String period;
	
	private String spatialCoverage;
	
	private String theme;
	
	private String language;
	
	private String dateTimeFormats = "YYYYMMDDHHMMSS";
	
	private String description;
	
	private String idDatabase;
	
	private String identifierURI;
	
	private String license;
	
	public String getDatasetTitle() {
		return datasetTitle;
	}

	public void setDatasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(String contactPoint) {
		this.contactPoint = contactPoint;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getSpatialCoverage() {
		return spatialCoverage;
	}

	public void setSpatialCoverage(String spatialCoverage) {
		this.spatialCoverage = spatialCoverage;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDateTimeFormats() {
		return dateTimeFormats;
	}

	public void setDateTimeFormats(String dateTimeFormats) {
		this.dateTimeFormats = dateTimeFormats;
	}

	public NovoDataset() {
		
	}

	public String getCollectionIdColumnName() {
		return collectionIdColumnName;
	}

	public void setCollectionIdColumnName(String collectionIdColumnName) {
		this.collectionIdColumnName = collectionIdColumnName;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getUpdateFrequency() {
		return updateFrequency;
	}

	public void setUpdateFrequency(String updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

	public String getIdCollection() {
		return idCollection;
	}

	public void setIdCollection(String idCollection) {
		this.idCollection = idCollection;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdDatabase() {
		return idDatabase;
	}

	public void setIdDatabase(String idDatabase) {
		this.idDatabase = idDatabase;
	}

	public String getIdentifierURI() {
		return identifierURI;
	}

	public void setIdentifierURI(String identifierURI) {
		this.identifierURI = identifierURI;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	
	
}
