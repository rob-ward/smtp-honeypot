package com.smtphoneypot.ui;

import com.smtphoneypot.core.Message;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class FilterTableLabelProvider implements ITableLabelProvider
{
    
    public Image getColumnImage(Object arg0, int arg1)
    {
        return null;
    }
    
    public String getColumnText(Object element, int columnIndex)
    {
        String result = "";
        Message message = (Message) element;
        switch (columnIndex)
        {
            case 0:
                result = "" + message.getId();
                break;
            case 1:
                result = message.getFrom();
                break;
            case 2:
                result = message.getTo();
                break;
            case 3:
                result = message.getLogTime();
                break;
            case 4:
                result = message.getTimeSent();
                break;
            case 5:
                result = message.getSendingServer();
                break;
            case 6:
                result = message.getHeaderInfo();
                break;
            case 7:
                result = message.getsubject();
                break;
            default:
                break;
        }
        return result;
    }
    
    public void addListener(ILabelProviderListener arg0)
    {
        
    }
    
    public void dispose()
    {
        
    }
    
    public void removeListener(ILabelProviderListener arg0)
    {
        
    }
    
    public boolean isLabelProperty(Object arg0, String arg1)
    {
        return false;
    }
    
}
