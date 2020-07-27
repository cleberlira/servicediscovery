/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iotreactive.model;



/**
 *
 * @author cleberlira
 */
public class Irrigation {
    private String id;
   private String type;
   
   
   public Irrigation(){
       	id="0";
	type="default";
   }
   
   
   public Irrigation(String id, String type){
       this.id = id;
       this.type = type;
   }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
       return "Irrigation [id=" + id + ", type=" + type + "]";

    }
}
