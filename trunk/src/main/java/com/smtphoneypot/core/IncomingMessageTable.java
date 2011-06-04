package com.smtphoneypot.core;

import java.sql.ResultSet;
import java.util.Vector;

public class IncomingMessageTable extends DatabaseTable
{
    private Vector<Message> messages;
    private final ValidateSQL valid;
    private String whereQuery;
    private String orderQuery;
    private static final IncomingMessageTable messageTable = new IncomingMessageTable();
    
    private IncomingMessageTable()
    {
        super();
        super
                            .setTableQuery("SmtpDataDump (id INTEGER PRIMARY KEY, sender TEXT, reciever TEXT, message TEXT, logTime DATETIME, timeSent TEXT, sendingServer TEXT, headerInfo TEXT, subject TEXT)");
        this.valid = new ValidateSQL();
        this.whereQuery = "";
        this.orderQuery = "";
    }
    
    public static IncomingMessageTable getInstance()
    {
        return IncomingMessageTable.messageTable;
    }
    
    public String getWhereQuery()
    {
        return this.whereQuery;
    }
    
    public void setWhereQuery(String col, String search)
    {
        // don't need to validate the col string as static, where the search is passed by the user.
        this.whereQuery = " WHERE " + col + " LIKE '%" + this.valid.whereClean(search)
                            + "%' ESCAPE \\ ";
    }
    
    public void searchAllData(String search)
    {
        // don't need to validate the col string as static, where the search is passed by the user.
        this.whereQuery = " WHERE id LIKE '%" + this.valid.whereClean(search) + "%' ";
        this.whereQuery += " OR sender LIKE '%" + this.valid.whereClean(search) + "%' ";
        this.whereQuery += " OR reciever LIKE '%" + this.valid.whereClean(search) + "%' ";
        this.whereQuery += " OR message LIKE '%" + this.valid.whereClean(search) + "%' ";
        this.whereQuery += " OR logTime LIKE '%" + this.valid.whereClean(search) + "%' ";
        this.whereQuery += " OR timeSent LIKE '%" + this.valid.whereClean(search) + "%' ";
        this.whereQuery += " OR sendingServer LIKE '%" + this.valid.whereClean(search) + "%' ";
        this.whereQuery += " OR headerInfo LIKE '%" + this.valid.whereClean(search) + "%' ";
        this.whereQuery += " OR subject LIKE '%" + this.valid.whereClean(search) + "%' ESCAPE '\\'";
    }
    
    public String getOrderQuery()
    {
        return this.orderQuery;
    }
    
    public void setOrderQuery(String orderQuery)
    {
        this.orderQuery = " ORDER BY " + orderQuery;
    }
    
    public void insertData(Message data) throws Exception
    {
        String from = this.valid.clean(data.getFrom());
        String to = this.valid.clean(data.getTo());
        String mess = this.valid.clean(data.getMessage());
        String timeSent = this.valid.clean(data.getTimeSent());
        String server = this.valid.clean(data.getSendingServer());
        String headerInfo = this.valid.clean(data.getHeaderInfo());
        String subject = this.valid.clean(data.getsubject());
        // for security run the SQL injection
        System.out.println("Trying to insert data");
        String query = "INSERT INTO SmtpDataDump VALUES (NULL, '" + from + "', '" + to + "', '"
                            + mess + "', DATETIME('NOW'), '" + timeSent + "', '" + server + "', '"
                            + headerInfo + "', '" + subject + "');";
        System.out.println(query);
        super.insertQuery(query);
    }
    
    public void getAllData() throws Exception
    {
        // get all of the languages from the language table
        ResultSet rs = null;
        if ((this.whereQuery.compareTo("") == 0) && (this.orderQuery.compareTo("") == 0))
        {
            rs = super.getRowData("SELECT * FROM SmtpDataDump" + this.whereQuery + this.orderQuery);
        }
        else if (this.orderQuery.compareTo("") == 0)
        {
            // the where string passed must have the column and string query
            rs = super.getRowData("SELECT * FROM SmtpDataDump" + this.whereQuery);
        }
        else if (this.whereQuery.compareTo("") == 0)
        {
            rs = super.getRowData("SELECT * FROM SmtpDataDump" + this.orderQuery);
        }
        else
        {
            rs = super.getRowData("SELECT * FROM SmtpDataDump");
        }
        // then unset the order and where query
        this.orderQuery = "";
        this.whereQuery = "";
        this.setData(rs);
    }
    
    @Override
    public void setData(ResultSet rs) throws Exception
    {
        this.messages = new Vector<Message>();
        // add of the possible languages to the language list
        while (rs.next())
        {
            // compile results
            Message tempMessage = new Message();
            tempMessage.setId(rs.getInt("id"));
            tempMessage.setFrom(rs.getString("sender"));
            tempMessage.setTo(rs.getString("reciever"));
            tempMessage.setMessage(rs.getString("message"));
            tempMessage.setLogTime(rs.getString("logTime"));
            tempMessage.setTimeSent(rs.getString("timeSent"));
            tempMessage.setSendingServer(rs.getString("sendingServer"));
            tempMessage.setHeaderInfo(rs.getString("headerInfo"));
            tempMessage.setsubject(rs.getString("subject"));
            // add the message to the list
            this.messages.add(tempMessage);
        }
    }
    
    public Vector<Message> getMessages()
    {
        return this.messages;
    }
    
    public void setMessages(Vector<Message> messages)
    {
        this.messages = messages;
    }
}
