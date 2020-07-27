/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.dcc.wiser.iotreactive.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author cleberlira
 */
@Component
@ConfigurationProperties("vertx")
public class Configuration {

    private Zookeeper zookeeper;
    private Server server;

    public Zookeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(Zookeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public static class Zookeeper {

        private String bind;
        private String path;

        public String getBind() {
            return bind;
        }

        public void setBind(String bind) {
            this.bind = bind;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

    public static class Server {

        private Integer port;

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

    }

}
