/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iotreactive.app;

import br.ufba.dcc.wiser.iotreactive.discovery.Consumer;
import br.ufba.dcc.wiser.iotreactive.discovery.Publisher;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import br.ufba.dcc.wiser.iotreactive.discovery.Publisher;
import br.ufba.dcc.wiser.iotreactive.discovery.Consumer;

/**
 *
 * @author cleberlira
 */


public class App extends AbstractVerticle{
    
    
    @Override
	public void start(){
          deployVerticles();
		
            
        }
    
    @Autowired
	private Consumer consumer;
	@Autowired
	private Publisher publish;
//	public static void main(String ...args) {
//		SpringApplication.run(App.class, args);
//	}
	@PostConstruct
	public void deployVerticles() {
            
                vertx.deployVerticle(Publisher.class.getName());
                vertx.deployVerticle(Consumer.class.getName()) ;   
          
            
		
//	}
    
        }
}
