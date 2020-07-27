/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iotreactive.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import br.ufba.dcc.wiser.iotreactive.model.IrrigationDAO;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
@Component


/**
 *
 * @author cleberlira
 */
public class Consumer extends AbstractVerticle {
    public static final String IRRIGATION_ALL="all";
	public static final String IRRIGATION_BY_ID="irrigation-id";
	public static final String IRRIGATION_REQ_PARAM_NAME="irrigationId";
	
	@Autowired
	private IrrigationDAO irrigationDAO = new IrrigationDAO();
	@Override
	public void start()throws Exception {
		super.start();
        vertx.eventBus()
                .<String>consumer(IRRIGATION_BY_ID)
                .handler(getIrrigationById(irrigationDAO));
        
        vertx.eventBus()
        .<String>consumer(IRRIGATION_ALL)
        .handler(getIrrigationAllHandler(irrigationDAO));
	}
	private Handler<Message<String>> getIrrigationById(IrrigationDAO irrigationDAO) {
		return msg->vertx.<String>executeBlocking(hf->{
			String irrigationId=msg.headers().get(IRRIGATION_REQ_PARAM_NAME);
			try {
				hf.complete(irrigationDAO.getIrrigationBy(irrigationId));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				hf.fail(e);
			}
		}, rh->{
			if (rh.succeeded()) {
			    msg.reply(rh.result());
			} else {
				msg.reply(rh.cause().toString());
			}
		});
	}
	
	private Handler<Message<String>> getIrrigationAllHandler(IrrigationDAO irrigationDAO) {
		return msg->vertx.<String>executeBlocking(hf->{
			try {
				hf.complete(irrigationDAO.getAllIrrigation());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				hf.fail(e);
			}
		}, rh->{
			if (rh.succeeded()) {
			    msg.reply(rh.result());
			} else {
				msg.reply(rh.cause().toString());
			}
		});
	}
}
