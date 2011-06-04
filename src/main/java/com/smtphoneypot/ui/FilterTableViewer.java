package com.smtphoneypot.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

public class FilterTableViewer extends TableViewer
{
    
    FilterTableContentProvider contentProvider = new FilterTableContentProvider();
    FilterTableLabelProvider labelProvider = new FilterTableLabelProvider();
    FilterTableSorter sorter = new FilterTableSorter();
    
    public FilterTableViewer(Composite parent, int style)
    {
        super(parent, style);
        this.createColumns(this);
        this.setContentProvider(this.contentProvider);
        this.setLabelProvider(this.labelProvider);
        this.setSorter(this.sorter);
        this.setInput("none");
    }
    
    private void createColumns(TableViewer viewer)
    {
        String[] titles = { "ID", "From", "To", "Log Time", "Time Sent", "Sending Server",
                "Header Info", "Subject" };
        int[] bounds = { 100, 100, 100, 100, 100, 100, 100, 100 };
        
        for (int i = 0; i < titles.length; i++)
        {
            final int index = i;
            TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
            final TableColumn column = viewerColumn.getColumn();
            column.setText(titles[i]);
            column.setWidth(bounds[i]);
            column.setResizable(true);
            column.setMoveable(true);
            column.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    FilterTableViewer.this.sorter.setColumn(index);
                    int dir = FilterTableViewer.this.getTable().getSortDirection();
                    if (FilterTableViewer.this.getTable().getSortColumn() == column)
                    {
                        dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
                    }
                    else
                    {
                        
                        dir = SWT.DOWN;
                    }
                    FilterTableViewer.this.getTable().setSortDirection(dir);
                    FilterTableViewer.this.getTable().setSortColumn(column);
                    FilterTableViewer.this.refresh();
                }
            });
        }
        
        viewer.getTable().setLinesVisible(true);
        viewer.getTable().setHeaderVisible(true);
        
        viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
    }
    
}
