package com.smtphoneypot.core;
public class Message
{
    private int id = 0;
    private String from = "";
    private String to = "";
    private String logTime = "";
    private String timeSent = "";
    private String message = "";
    private String sendingServer = "";
    private String headerInfo = "";
    private String subject = "";
    
    public Message()
    {
        
    }
    
    public Message(String from, String to, String logTime, String message, String server, String timeSent)
    {
        super();
        this.from = from;
        this.to = to;
        this.logTime = logTime;
        this.message = message;
        this.sendingServer = server;
    }
    
    public Message(int id, String from, String to, String logTime, String message, String server, String timeSent)
    {
        super();
        this.id = id;
        this.from = from;
        this.to = to;
        this.logTime = logTime;
        this.message = message;
        this.sendingServer = server;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getFrom()
    {
        return this.from;
    }
    
    public void setFrom(String from)
    {
        this.from = from;
    }
    
    public String getTo()
    {
        return this.to;
    }
    
    public void setTo(String to)
    {
        this.to = to;
    }
    
    public String getLogTime()
    {
        return this.logTime;
    }
    
    public void setLogTime(String time)
    {
        this.logTime = time;
    }
    
    public String getMessage()
    {
        return this.message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getSendingServer()
    {
        return this.sendingServer;
    }
    
    public void setSendingServer(String sendingServer)
    {
        this.sendingServer = sendingServer;
    }
    
    public String getsubject()
    {
        return this.subject;
    }
    
    public void setHeaderInfo(String s)
    {
        this.headerInfo = s;
    }
    
    public void setsubject(String s)
    {
        this.subject = s;
    }
    
    public String getHeaderInfo()
    {
        return this.headerInfo;
    }

	public void setTimeSent(String timeSent)
	{
		this.timeSent = timeSent;
	}

	public String getTimeSent() 
	{
		return timeSent;
	}
    
}
