package com.smtphoneypot.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database
{
    private Statement stat;
    private Connection conn;
    private String dbLocation;
    private static final Database frameInstance = new Database();
    private boolean connected;
    
    /**
     * Private constructor prevents instantiation from other classes
     */
    private Database()
    {
        this.connected = false;
    }
    
    public static Database getInstance()
    {
        return Database.frameInstance;
    }
    
    public boolean connectToDatabase(String name)
    {
        try
        {
            this.dbLocation = name;
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + this.dbLocation);
            // commit as a transaction
            this.conn.setAutoCommit(true);
            this.connected = true;
        }
        catch (Exception e)
        {
            System.out.println("Failed to connect to the database " + this.dbLocation);
        }
        
        return this.connected;
    }
    
    public boolean isConnected()
    {
        return this.connected;
    }
    
    public void disconnectFromDatabase() throws SQLException
    {
        this.conn.close();
    }
    
    public void nonResultsQuery(String s) throws SQLException
    {
        // creates a sql statement to run the UPDATE/DELETE/INSERT query
        this.stat = this.conn.createStatement();
        this.stat.executeUpdate(s);
    }
    
    public ResultSet resultsQuery(String s) throws SQLException
    {
        this.stat = this.conn.createStatement();
        // get a result set for the query
        ResultSet rs = this.stat.executeQuery(s);
        return rs;
    }
    
    public Boolean checkDatabaseExsists()
    {
        return true;
    }
    
    public void checkTable(String s) throws SQLException, Exception
    {
        // check if the table exists
        // if it does not then create it
        // creates a sql statement to run the UPDATE/DELETE/INSERT query
        if (s.trim().compareTo("") != 0)
        {
            this.stat = this.conn.createStatement();
            this.stat.executeUpdate("CREATE TABLE IF NOT EXISTS " + s + ";");
        }
        else
        {
            throw new Exception("The query string, that was passed was empty");
        }
    }
    
    public String getDbLocation()
    {
        return this.dbLocation;
    }
    
    public void setDbLocation(String dbLocation)
    {
        this.dbLocation = dbLocation;
    }
}
