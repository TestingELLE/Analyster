/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.analyster.database;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author cigreja
 */
public class DBConnection {
    
    public static Statement statement;
    private static final String SERVERS_FILENAME = "servers.xml";
    
    public static void connect(String selectedServer, String selectedDB, String userName, String userPassword) throws SQLException{
        
        String url = "";
        Connection connection;
        ArrayList<Server> servers = readServers();
        
        // load url for server
        for(Server server: servers){
            if(server.getName().equals(selectedServer))
                url += server.getUrl();
        }
        
        url += selectedDB;

        // connect to server
        connection = DriverManager.getConnection(url, userName, userPassword);
        statement = connection.createStatement();
        System.out.println("Connection successfully");
             
    }
    

    // read customer data from xml file
    public static ArrayList<Server> readServers()
    {
        ArrayList<Server> servers = new ArrayList<>();
        Server server = null;

        // create an XMLInputFactory object
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        try{
            InputStream inputStream = DBConnection.class.getResourceAsStream(SERVERS_FILENAME);
            InputStreamReader fileReader = new InputStreamReader(inputStream);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(fileReader);
            
            //Read XML here
            while(reader.hasNext()){
                int eventType = reader.getEventType();
                switch(eventType){
                    case XMLStreamConstants.START_ELEMENT:
                        String elementName = reader.getLocalName();
                        if(elementName.equals("server")){
                            server = new Server();
                        }
                        else if(elementName.equals("name")){
                            String name = reader.getElementText();
                            server.setName(name);
                        }
                        else if(elementName.equals("url")){
                            String url = reader.getElementText();
                            server.setUrl(url);
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        elementName = reader.getLocalName();
                        if(elementName.equals("server")){
                            servers.add(server);
                        }
                        break;
                    default:
                        break;
                }
                reader.next();
            }
        }catch(XMLStreamException e){
            System.out.println(e);
        }
        return servers;
    }

    // write customer data to xml file
    public static void writeServers(ArrayList<Server> servers)
    {
        // create the XMLOutputFactory object
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        try{
            //create XMLStreamWriter object
            FileWriter fileWriter = new FileWriter(SERVERS_FILENAME);
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(fileWriter);
            
            // write the customers to the file
            writer.writeStartDocument("1.0");
            writer.writeStartElement("servers");
            for (Server server : servers){
                writer.writeStartElement("server");
                writer.writeStartElement("name");
                writer.writeCharacters(server.getName());
                writer.writeEndElement();
                writer.writeStartElement("url");
                writer.writeCharacters(server.getUrl());
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.flush();
            writer.close();
        }catch(IOException | XMLStreamException e){
            System.out.println(e);
        }
    }
}
