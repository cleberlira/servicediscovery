/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iotreactive.model.client;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import java.util.concurrent.TimeUnit;
import io.vertx.servicediscovery.types.HttpEndpoint;

/**
 *
 * @author cleberlira
 */
public class Client {
    
    public static void main (String args[]){
        Client c = new Client();
        
        try{ 
        c.irrigationByIdFromServiceDiscovery();
        }catch( InterruptedException x){
            System.out.println("erro");
            
        }
    }
   
    public void irrigationByIdFromServiceDiscovery() throws InterruptedException {
		final Vertx vertx = Vertx.vertx();
		 ServiceDiscovery zookeeperDiscovery= ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
	                .setBackendConfiguration(
	                    new JsonObject()
	                        .put("connection", "192.168.0.12:2181")
	                        .put("ephemeral", true)
	                        .put("guaranteed", true)
	                        .put("basePath", "/services/api-irrigation")
	                ));
                 
		 HttpEndpoint.getClient(zookeeperDiscovery, new JsonObject().put("name", "irrigations-all"), ar -> {
			  if (ar.succeeded()) {
			    HttpClient client = ar.result();
			    if (ar.succeeded()) {
			    	System.out.println("Success");
			    }
			    // You need to path the complete path
			    client.getNow("/irrigations/all", response -> {

			      System.out.println("Status Code===="+response.statusCode());
			      response.bodyHandler(bh->{
			    	  System.out.println("Dados "+new String(bh.getBytes()));
			    	  ServiceDiscovery.releaseServiceObject(zookeeperDiscovery, client);
			      });
			      

			    });
			  }
			});
		 TimeUnit.SECONDS.sleep(3);
	}
    
    
}
