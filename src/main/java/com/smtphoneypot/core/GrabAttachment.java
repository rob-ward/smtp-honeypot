package com.smtphoneypot.core;

import java.util.Iterator;
import java.util.Vector;

public class GrabAttachment
{
    private Message toProcess;
    private String location;
    
    // private String toProcess;
    public GrabAttachment()
    {
        
    }
    
    public Message getToProcess()
    {
        return this.toProcess;
    }
    
    public void setToProcess(Message toProcess)
    {
        this.toProcess = toProcess;
    }
    
    public Vector<Attachment> parseData()
    {
        Vector<Attachment> attachments = new Vector<Attachment>();
        String boundry = "";
        String[] parts = this.toProcess.getMessage().split("\n");
        
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].startsWith(" boundary=\"------------"))
            {
                boundry = parts[i].substring(11, parts[i].length() - 2);
                break;
            }
        }
        parts = this.toProcess.getMessage().split(boundry);
        for (int i = 3; i < parts.length - 1; i++)
        {
            boolean ctype = false;
            boolean ctransfer = false;
            boolean cdisposition = false;
            boolean filename = false;
            Attachment a = new Attachment();
            String[] subparts = parts[i].split("\n");
            for (int ii = 0; ii < subparts.length; ii++)
            {
                if (ctype && ctransfer && cdisposition && filename)
                {
                    a.file = a.file + subparts[ii];
                    // must be file
                }
                else
                {
                    if (subparts[ii].startsWith("Content-Type: "))
                    {
                        a.contentType = subparts[ii].substring(14, subparts[ii].length() - 2);
                        ctype = true;
                        System.out.println("HERE1");
                    }
                    if (subparts[ii].startsWith("Content-Transfer-Encoding: "))
                    {
                        a.contentTransferEncoding = subparts[ii].substring(26, subparts[ii]
                                            .length() - 1);
                        ctransfer = true;
                        System.out.println("HERE2");
                    }
                    if (subparts[ii].startsWith("Content-Disposition: "))
                    {
                        a.contentDisposition = subparts[ii]
                                            .substring(20, subparts[ii].length() - 2);
                        cdisposition = true;
                        System.out.println("HERE3");
                    }
                    if (subparts[ii].startsWith(" filename=\""))
                    {
                        a.filename = subparts[ii].substring(11, subparts[ii].length() - 2);
                        filename = true;
                        
                        System.out.println("HERE4");
                    }
                }
            }
            a.file = a.file.substring(0, a.file.length() - 2);
            a.setLocation(this.location);
            attachments.addElement(a);
        }
        return attachments;
        
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
        this.location = location;
    }
    
    public static void main(String[] args)
    {
        GrabAttachment grab = new GrabAttachment();
        // grab.setToProcess(toProcess);
        
        try
        {
            IncomingMessageTable.getInstance().getAllData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("HERE");
        Vector<Message> messages = IncomingMessageTable.getInstance().getMessages();
        Iterator<Message> it = messages.iterator();
        while (it.hasNext())
        {
            Message message = it.next();
            if (message.getId() == 22)
            {
                System.out.println("HERE");
                grab.setToProcess(message);
            }
        }
        Vector<Attachment> attachments = grab.parseData();
        Iterator<Attachment> it2 = attachments.iterator();
        System.out.println("HERE");
        while (it2.hasNext())
        {
            System.out.println("HERE");
            Attachment a = it2.next();
            a.printMe();
            a.writeTheFile();
        }
    }
}
