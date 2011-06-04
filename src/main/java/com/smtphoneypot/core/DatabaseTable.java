package com.smtphoneypot.core;
import java.sql.ResultSet;

public abstract class DatabaseTable
{
    private Database database;
    private String tableQuery;
    
    /**
     * 
     * @author Chris Ramsden
     * @created 11 Jan 2010
     */
    public DatabaseTable()
    {
        //get the instance of the database
        this.database = Database.getInstance();
    }	
	
    /**
     * @author Chris Ramsden
     * Created By: Chris Ramsden
     * Created On: 16 Nov 2009
     * @return the tableQuery
     */
    public String getTableQuery()
    {
        return tableQuery;
    }

    
    /**
     * @param tableQuery the tableQuery to set
     */
    public void setTableQuery(String tableQuery)
    {
        this.tableQuery = tableQuery;
    }
    
    public void checkTableExsist() throws Exception
    {
        //pass the table and the structure
        //if the table does not exist then it will be created
        this.database.checkTable(this.tableQuery);
    }
    
    public void insertQuery(String query) throws Exception
    {
        //check that the table exists
        checkTableExsist();
        this.database.nonResultsQuery(query);
    }

    public ResultSet getRowData(String query) throws Exception
    {
        ResultSet rs = null;

        //check that the table to be used in the query does exist
        checkTableExsist();
        //run the query and return the result set
        rs = this.database.resultsQuery(query);
             
        return rs;
    }
    
    public void deleteRecords(String query) throws Exception
    {
        //check that the table exists
        checkTableExsist();
        this.database.nonResultsQuery(query);
    }

    public void updateRecords(String query) throws Exception
    {
        //check that the table exists
        checkTableExsist();
        this.database.nonResultsQuery(query);
    }
    
    public abstract void setData(ResultSet rs) throws Exception;

}
