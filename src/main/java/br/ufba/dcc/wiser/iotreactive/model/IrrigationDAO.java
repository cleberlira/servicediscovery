/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iotreactive.model;


import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.json.Json;


/**
 *
 * @author cleberlira
 */

@Service
public class IrrigationDAO {
    
    
    private List<Irrigation> irrigations=new ArrayList<>();
	private final ObjectMapper mapper = Json.mapper;

       
        public IrrigationDAO( ) {
		irrigations.add(new Irrigation("minTempData","18"));
		irrigations.add(new Irrigation("maxTempData","36"));
		irrigations.add(new Irrigation("humdData","45"));
		irrigations.add(new Irrigation("lightData","78"));
       }
        
        
        public String getIrrigationBy(String id) throws JsonProcessingException {
		Optional<Irrigation> result=irrigations.stream().filter(s->s.getId().equals(id)).findFirst();
		if (result.isPresent())
			return mapper.writeValueAsString(result.get());
		else
			return mapper.writeValueAsString(new Irrigation());
	}
	public String getAllIrrigation() throws JsonProcessingException{
		return mapper.writeValueAsString(irrigations);
	}
        
        
        
        
    
}
