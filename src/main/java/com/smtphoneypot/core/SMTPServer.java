package com.smtphoneypot.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SMTPServer
{
    private int serverPort = 0;
    private ServerSocket serverSock = null;
    
    public SMTPServer(int port)
    {
        this.serverPort = port;
        
        try
        {
            this.serverSock = new ServerSocket(this.serverPort);
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
        }
    }
    
    public void activateConnection(InputStream sockInput, OutputStream sockOutput, Message message)
    {
        boolean data = false;
        boolean headerend = false;
        while (true)
        {
            byte[] buf = new byte[1024];
            int bytes_read = 0;
            try
            {
                bytes_read = sockInput.read(buf, 0, buf.length);
                if (bytes_read < 0)
                {
                    System.err.println("Tried to read from socket, read() returned < 0,  "
                                        + "Closing socket." + buf[0]);
                    return;
                }
                
                byte[] buf2 = new byte[buf.length];
                
                buf2 = buf;
                String in = new String();
                String buf3 = new String(buf2);
                for (int i = 0; i < buf2.length; i++)
                {
                    int j = i + 1;
                    if (buf2[i] != 0)
                    {
                        in = new String(in + buf3.substring(i, j));
                    }
                }
                
                // remove all new line characters
                String[] parts = in.split("\n");
                System.out.println(in.length() + "---|" + in + "|--");
                // use compare to after the string has been stripped
                if (in.startsWith("DATA"))
                {
                    message.setMessage(message.getMessage() + in);
                    // Start recording
                    data = true;
                    headerend = true;
                    System.out.println("IN DATA");
                    String startcomms = new String("354 End data with <CR><LF>.<CR><LF>   \r\n ");
                    buf = startcomms.getBytes();
                    
                    sockOutput.write(buf, 0, buf.length);
                    sockOutput.flush();
                    
                }
                else if (data)
                {
                    
                    message.setMessage(message.getMessage() + in);
                    if (in.contains("\r\n.\r\n"))
                    {
                        // stop recording
                        data = false;
                        System.out.println("IN DOT");
                        String startcomms = new String("250 Ok   \r\n ");
                        buf = startcomms.getBytes();
                        
                        sockOutput.write(buf, 0, buf.length);
                        sockOutput.flush();
                    }
                    
                    for (int i = 0; i < parts.length; i++)
                    {
                        String stringPart = parts[i];
                        if (stringPart.startsWith("Date:"))
                        {
                            message.setTimeSent(message.getTimeSent() + stringPart);
                        }
                        if (stringPart.startsWith("Message-ID: "))
                        {
                            String temp = stringPart;
                            temp.replaceAll("Message-ID:", "");
                            message.setSendingServer(temp);
                        }
                        if (stringPart.startsWith("Subject:"))
                        {
                            message.setsubject(stringPart);
                        }
                    }
                    
                }
                else if (in.startsWith("QUIT"))
                {
                    String startcomms = new String("221 Bye   \r\n ");
                    buf = startcomms.getBytes();
                    
                    sockOutput.write(buf, 0, buf.length);
                    sockOutput.flush();
                    
                    // store the data in the database
                    IncomingMessageTable.getInstance().insertData(message);
                    
                    return;
                    
                }
                else
                {
                    if (!headerend)
                    {
                        
                        message.setHeaderInfo(message.getHeaderInfo() + in);
                        for (int i = 0; i < parts.length; i++)
                        {
                            String stringPart = parts[i];
                            if (stringPart.startsWith("MAIL FROM:"))
                            {
                                message.setFrom(message.getFrom() + stringPart);
                            }
                            if (stringPart.startsWith("RCPT TO:"))
                            {
                                message.setTo(message.getTo() + stringPart);
                            }
                        }
                    }
                    String startcomms = new String("250 OK   \r\n ");
                    buf = startcomms.getBytes();
                    
                    sockOutput.write(buf, 0, buf.length);
                    sockOutput.flush();
                    
                }
                
            }
            catch (Exception e)
            {
                System.err.println("Exception reading from/writing to socket, e=" + e);
                e.printStackTrace(System.err);
                return;
            }
        }
    }
    
    public void startServer()
    {
        
        Socket sock = null;
        InputStream sockInput = null;
        OutputStream sockOutput = null;
        while (true)
        {
            Message message = new Message();
            // set up the server listening on the socket.
            try
            {
                sock = this.serverSock.accept();
                System.out.println("Have accepted new socket.");
                byte[] buf = new byte[1024];
                
                sockInput = sock.getInputStream();
                sockOutput = sock.getOutputStream();
                System.out.println(new String(buf));
                
                String startcomms = new String("");
                
                startcomms = new String("220 localhost ESMTP Postfix \r\n");
                buf = startcomms.getBytes();
                sockOutput.write(buf, 0, buf.length);
                sockOutput.flush();
                System.out.println("TEST");
                
                {
                    sockInput.read(buf, 0, buf.length);
                    
                    startcomms = new String("250 OK  \r\n");
                    buf = startcomms.getBytes();
                    sockOutput.write(buf, 0, buf.length);
                    sockOutput.flush();
                    
                }
                
            }
            catch (IOException e)
            {
                e.printStackTrace(System.err);
            }
            
            this.activateConnection(sockInput, sockOutput, message);
            
            try
            {
                sock.close();
            }
            catch (Exception e)
            {
                System.out.println("Exception while closing socket.");
                e.printStackTrace(System.err);
            }
        }
    }
    
    public static void main(String argv[])
    {
        
        int port = 25;
        if (argv.length == 1)
        {
            port = Integer.parseInt(argv[0]);
        }
        
        if (Database.getInstance().isConnected() == true)
        {
            // ERROR
            System.exit(2);
        }
        else
        {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy---HH-mm-ss");
            Calendar cal = Calendar.getInstance();
            String dbname = dateFormat.format(cal.getTime()) + ".db";
            System.out.println("Creating database called:" + dbname);
            Database.getInstance().connectToDatabase(dbname);
        }
        
        System.out.println();
        System.out.println("Started on port:" + port);
        SMTPServer server = new SMTPServer(port);
        server.startServer();
    }
    
}
