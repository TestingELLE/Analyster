
package com.elle.analyster.database;

/**
 * Server
 * This class is to create a server object.
 * @author cigreja
 */
public class Server {
    
    private String name;
    private String url;

    public Server() {
        name = "";
        url = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
