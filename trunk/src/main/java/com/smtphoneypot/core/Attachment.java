package com.smtphoneypot.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Attachment
{
    public String contentType = "";
    public String filename = "";
    public String contentTransferEncoding = "";
    public String contentDisposition = "";
    public String file = "";
    public String location = "";
    
    public Attachment()
    {
        
    }
    
    public void printMe()
    {
        System.out.println(this.contentType);
        System.out.println(this.contentTransferEncoding);
        System.out.println(this.contentDisposition);
        System.out.println(this.filename);
        System.out.println(this.file);
        
    }
    
    public void writeTheFile()
    {
        FileWriter fstream;
        try
        {
            fstream = new FileWriter(this.location + this.filename + ".txt", true);
            
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(this.file);
            // Close the output stream
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * @author u0558719
     * @created 11 Mar 2010
     * @return the location
     */
    public String getLocation()
    {
        return this.location;
    }
    
    /**
     * @author u0558719
     * @created 11 Mar 2010
     * @param location
     *            the location to set
     */
    public void setLocation(String location)
    {
        this.location = location + "/";
    }
    
}
