/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iotreactive.discovery;

import static br.ufba.dcc.wiser.iotreactive.discovery.Consumer.IRRIGATION_REQ_PARAM_NAME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufba.dcc.wiser.iotreactive.configuration.Configuration;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.rxjava.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

/**
 *
 * @author cleberlira
 */

@Component
public class Publisher extends AbstractVerticle {

   // private static final Logger LOG = Logger.getLogger(Publisher.class);
    
    
    
    	@Autowired
	private Configuration yamlConfiguration;
	@Override
	public void start()throws Exception {
	  super.start();
	yamlConfiguration = new Configuration();	
	  Router router = Router.router(vertx);
	  router.get("/irrigations/:irrigationId/crops")
	            .handler(this::getIrrigationById);
	        router.get("/irrigations/all")
            .handler(this::getAllIrrigations);
	        
	        vertx.createHttpServer()
	               .requestHandler(router::accept)
	               .listen(config().getInteger("http.port",9999));
	        publishIrrigationByIdToZookeeperRepo();
	        publishAllIrrigationsToZookeeperRepo();
	}
    
    

        private void getIrrigationById(RoutingContext rc) {
		DeliveryOptions options = new DeliveryOptions();
		options.addHeader(IRRIGATION_REQ_PARAM_NAME ,rc.request().getParam(IRRIGATION_REQ_PARAM_NAME));
		
		vertx.eventBus().<String>send(Consumer.IRRIGATION_BY_ID, "",options,response->{
		if (response.succeeded()) {
				rc.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(200)
                .end(response.result()
                .body());
			} else {
				rc.response()
                .setStatusCode(500)
                .end();
			}
		});
	}
        
        
        
  private void getAllIrrigations(RoutingContext rc) {
		
		vertx.eventBus().<String>send(Consumer.IRRIGATION_ALL, "",response->{
			if (response.succeeded()) {
				rc.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(200)
                .end(response.result()
                .body());
			} else {
				rc.response()
                .setStatusCode(500)
                .end();
			}
		});
	}
        
        
    private void publishIrrigationByIdToZookeeperRepo() {
        //creating record , to store details in zookeeper
        Record record = HttpEndpoint.createRecord("irrigation-api-service", "localhost",
                9999, "/irrigation");
        //creating service discovery for zookeeper
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
                .setBackendConfiguration(
                        buildZookeeperConnection()
                ));
        discovery.publish(record, r -> {
            if (r.succeeded()) {

                System.out.println("successfully published to zookeeper>>>>> " + r.result().toJson());

            } else {
                r.cause().printStackTrace();
            }
        });
    }
    
    
    
    private JsonObject buildZookeeperConnection() {
        return new JsonObject()
                .put("connection", "127.0.0.1:2181")
                .put("ephemeral", true)
                .put("guaranteed", true)
                .put("basePath", "/services/api-irrigation");
    }
    
    
  private void publishAllIrrigationsToZookeeperRepo() {
	  ServiceDiscovery discovery= ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions()
              .setBackendConfiguration(
            		  buildZookeeperConnection()
              ));
      Record record=HttpEndpoint.createRecord("irrigations-all", "localhost",
    		  9999,"/irrigations");
      discovery.publish(record, r->{
      	if(r.succeeded()) {
      		System.out.println("successfully published to zookeeper>>>>> "+r.result().toJson());
      	} else {
      		r.cause().printStackTrace();
      	}
      });
  }

}
