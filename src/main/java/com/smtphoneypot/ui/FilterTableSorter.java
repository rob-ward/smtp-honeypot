package com.smtphoneypot.ui;

import com.smtphoneypot.core.Message;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class FilterTableSorter extends ViewerSorter
{
    private int propertyIndex;
    private static final int DESCENDING = 1;
    
    private int direction = FilterTableSorter.DESCENDING;
    
    public FilterTableSorter()
    {
        this.propertyIndex = 0;
        this.direction = FilterTableSorter.DESCENDING;
    }
    
    public void setColumn(int column)
    {
        if (column == this.propertyIndex)
        {
            // Same column as last sort; toggle the direction
            this.direction = 1 - this.direction;
        }
        else
        {
            // New column; do an ascending sort
            this.propertyIndex = column;
            this.direction = FilterTableSorter.DESCENDING;
        }
    }
    
    @Override
    public int compare(Viewer viewer, Object e1, Object e2)
    {
        Message m1 = (Message) e1;
        Message m2 = (Message) e2;
        int rc = 0;
        switch (this.propertyIndex)
        {
            case 0:
                rc = ((Integer) m1.getId()).compareTo(m2.getId());
                break;
            case 1:
                rc = m1.getFrom().compareTo(m2.getFrom());
                break;
            case 2:
                rc = m1.getTo().compareTo(m2.getTo());
                break;
            case 3:
                rc = m1.getLogTime().compareTo(m2.getLogTime());
                break;
            case 4:
                rc = m1.getTimeSent().compareTo(m2.getTimeSent());
                break;
            case 5:
                rc = m1.getSendingServer().compareTo(m2.getSendingServer());
                break;
            case 6:
                rc = m1.getHeaderInfo().compareTo(m2.getHeaderInfo());
                break;
            case 7:
                rc = m1.getsubject().compareTo(m2.getsubject());
                break;
            default:
                rc = 0;
        }
        // If descending order, flip the direction
        if (this.direction == FilterTableSorter.DESCENDING)
        {
            rc = -rc;
        }
        return rc;
    }
}
