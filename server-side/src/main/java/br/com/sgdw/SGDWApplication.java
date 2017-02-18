package br.com.sgdw;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

import br.com.sgdw.util.SoUtil;

@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableSwagger 
@ComponentScan(basePackages = {"br.com.sgdw"}) 
public class SGDWApplication extends SpringBootServletInitializer  {
	
    @Autowired
    private SpringSwaggerConfig swaggerConfig; 
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder SGDWApplication) {
        return SGDWApplication.sources(SGDWApplication.class);
    }
    
    @Bean
    public SwaggerSpringMvcPlugin groupOnePlugin() {
       return new SwaggerSpringMvcPlugin(swaggerConfig)
           .apiInfo(apiInfo())
           .includePatterns("/admin.*?","/open.*?")
           .apiVersion("1.0")
           .swaggerGroup("sgdw");
    }
     
    private ApiInfo apiInfo() {
    	return new ApiInfo( 
             "SGDW",
             "Documentação gerada com o uso do Swagger. Dúvidas? Entrar em contato: https://github.com/dass-cin/sgdw",
             "Uso aberto, sob Licença GPL v2",
             "", //email
             "GPL v2",
             "https://github.com/dass-cin/SGDW/blob/master/LICENSE"  
       );
    }
    
	public static void main(String[] args)
	{
		String raiz = SoUtil.getRaiz();
			
		File diretorio = new File(raiz);
		
		if(!diretorio.exists())
		{
			diretorio.mkdir();
			SoUtil.copyProperties(raiz);
			
			System.out.println("Arquivos de configuracoes criados!");
			System.out.println("Por favor, acesse "+raiz+" e configure a ferramenta!");
		}
		else
		{
			//SpringApplication.run(SGDWApplication.class, args);
			new SpringApplicationBuilder(SGDWApplication.class).web(true).run(args);
		}
		
	}
}
