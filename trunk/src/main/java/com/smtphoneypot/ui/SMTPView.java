/**
 * @author Alex Wright
 * @created 9 Mar 2010
 * @file SMTPView.java
 */
package com.smtphoneypot.ui;

import java.util.Iterator;
import java.util.Vector;
import com.smtphoneypot.core.Attachment;
import com.smtphoneypot.core.GrabAttachment;
import com.smtphoneypot.core.IncomingMessageTable;
import com.smtphoneypot.core.Message;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.smtphoneypot.ui.action.ExitAction;
import com.smtphoneypot.ui.action.OpenDBAction;

public class SMTPView extends ApplicationWindow
{
    
    private SashForm mainForm;
    private Composite topForm;
    private ScrolledComposite bottomScroll;
    private Composite bottomForm;
    private Text searchInput;
    private Button searchButton;
    private Button clearButton;
    private Button grabAttachmentsButton;
    private Message message;
    private FilterTableViewer filterTableViewer;
    
    private Label idValueLabel;
    private Label fromValueLabel;
    private Label toValueLabel;
    private Label logTimeValueLabel;
    private Label sentTimeValueLabel;
    private Label messageValueLabel;
    private Label sendingServerValueLabel;
    private Label headerInfoValueLabel;
    private Label subjectValueLabel;
    
    public static void main(String[] args)
    {
        new SMTPView(null);
    }
    
    public SMTPView(Shell parentShell)
    {
        super(parentShell);
        this.addMenuBar();
        this.addToolBar(SWT.HORIZONTAL);
        this.addStatusLine();
        this.run();
    }
    
    public void run()
    {
        // Don't return from open() until window closes
        this.setBlockOnOpen(true);
        
        // Open the main window
        this.open();
        
        // Dispose the display
        Display.getCurrent().dispose();
    }
    
    @Override
    protected void configureShell(Shell shell)
    {
        super.configureShell(shell);
        shell.setText("SMTP0wned Analyser");
        shell.setMaximized(true);
    }
    
    @Override
    protected Control createContents(Composite parent)
    {
        this.setStatus("Ready!...");
        GridData gridData;
        
        this.mainForm = new SashForm(parent, SWT.VERTICAL | SWT.SMOOTH);
        this.mainForm.setSashWidth(5);
        this.mainForm.setLayout(new FillLayout());
        
        this.topForm = new Composite(this.mainForm, SWT.BORDER);
        this.topForm.setLayout(new GridLayout());
        Composite searchForm = new Composite(this.topForm, SWT.NONE);
        searchForm.setLayout(new GridLayout(3, false));
        gridData = new GridData();
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.grabExcessVerticalSpace = false;
        searchForm.setLayoutData(gridData);
        this.searchInput = new Text(searchForm, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.BEGINNING;
        gridData.grabExcessHorizontalSpace = false;
        gridData.widthHint = 250;
        this.searchInput.setLayoutData(gridData);
        this.searchButton = new Button(searchForm, SWT.PUSH);
        
        this.searchButton.setText("Search");
        
        this.searchButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                SMTPView.this.setStatus("Searching...");
                String searchString = SMTPView.this.searchInput.getText();
                try
                {
                    IncomingMessageTable.getInstance().searchAllData(searchString);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                SMTPView.this.filterTableViewer.refresh();
                SMTPView.this.setStatus("Search Complete!");
            }
        });
        
        this.clearButton = new Button(searchForm, SWT.PUSH);
        
        this.clearButton.setText("Clear");
        
        this.clearButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                SMTPView.this.searchInput.setText("");
                SMTPView.this.filterTableViewer.refresh();
            }
        });
        
        Composite tableForm = new Composite(this.topForm, SWT.NONE);
        tableForm.setLayout(new GridLayout());
        gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        tableForm.setLayoutData(gridData);
        this.filterTableViewer = new FilterTableViewer(tableForm, SWT.SINGLE | SWT.V_SCROLL
                            | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
        
        this.filterTableViewer.getTable().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                SMTPView.this.setStatus("Opening Message...");
                Message m = ((Message) SMTPView.this.filterTableViewer.getTable().getSelection()[0]
                                    .getData());
                SMTPView.this.message = m;
                SMTPView.this.idValueLabel.setText("" + m.getId());
                SMTPView.this.fromValueLabel.setText(m.getFrom());
                SMTPView.this.toValueLabel.setText(m.getTo());
                SMTPView.this.logTimeValueLabel.setText(m.getLogTime());
                SMTPView.this.sentTimeValueLabel.setText(m.getTimeSent());
                SMTPView.this.sendingServerValueLabel.setText(m.getSendingServer());
                SMTPView.this.subjectValueLabel.setText(m.getsubject());
                SMTPView.this.headerInfoValueLabel.setText(m.getHeaderInfo());
                SMTPView.this.headerInfoValueLabel.pack();
                SMTPView.this.messageValueLabel.setText(m.getMessage());
                SMTPView.this.messageValueLabel.pack();
                SMTPView.this.bottomForm.pack();
                SMTPView.this.setStatus("Message Opened!");
            }
        });
        
        this.bottomScroll = new ScrolledComposite(this.mainForm, SWT.BORDER | SWT.V_SCROLL);
        this.bottomForm = new Composite(this.bottomScroll, SWT.NONE);
        this.bottomScroll.setContent(this.bottomForm);
        this.bottomScroll.setExpandHorizontal(true);
        this.bottomScroll.setExpandVertical(false);
        this.bottomScroll.setAlwaysShowScrollBars(true);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 8;
        this.bottomForm.setLayout(gridLayout);
        
        Label idLabel = new Label(this.bottomForm, SWT.NONE);
        idLabel.setText("ID:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        gridData.grabExcessHorizontalSpace = false;
        idLabel.setLayoutData(gridData);
        
        this.idValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.idValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = false;
        this.idValueLabel.setLayoutData(gridData);
        
        Label logTimeLabel = new Label(this.bottomForm, SWT.NONE);
        logTimeLabel.setText("LOG TIME:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        gridData.grabExcessHorizontalSpace = false;
        logTimeLabel.setLayoutData(gridData);
        
        this.logTimeValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.logTimeValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        this.logTimeValueLabel.setLayoutData(gridData);
        
        Label sentTimeLabel = new Label(this.bottomForm, SWT.NONE);
        sentTimeLabel.setText("SENT TIME:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        gridData.grabExcessHorizontalSpace = false;
        sentTimeLabel.setLayoutData(gridData);
        
        this.sentTimeValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.sentTimeValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        gridData.grabExcessHorizontalSpace = true;
        this.sentTimeValueLabel.setLayoutData(gridData);
        
        Label fromLabel = new Label(this.bottomForm, SWT.NONE);
        fromLabel.setText("FROM:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = false;
        fromLabel.setLayoutData(gridData);
        
        this.fromValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.fromValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        gridData.grabExcessHorizontalSpace = true;
        this.fromValueLabel.setLayoutData(gridData);
        
        Label sendingServerLabel = new Label(this.bottomForm, SWT.NONE);
        sendingServerLabel.setText("SENDING SERVER:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = false;
        sendingServerLabel.setLayoutData(gridData);
        
        this.sendingServerValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.sendingServerValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        gridData.grabExcessHorizontalSpace = true;
        this.sendingServerValueLabel.setLayoutData(gridData);
        
        Label toLabel = new Label(this.bottomForm, SWT.NONE);
        toLabel.setText("TO:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = false;
        toLabel.setLayoutData(gridData);
        
        this.toValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.toValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        gridData.grabExcessHorizontalSpace = true;
        this.toValueLabel.setLayoutData(gridData);
        
        Label subjectLabel = new Label(this.bottomForm, SWT.NONE);
        subjectLabel.setText("SUBJECT:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = false;
        subjectLabel.setLayoutData(gridData);
        
        this.subjectValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.subjectValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        gridData.grabExcessHorizontalSpace = true;
        this.subjectValueLabel.setLayoutData(gridData);
        
        this.grabAttachmentsButton = new Button(this.bottomForm, SWT.PUSH);
        
        this.grabAttachmentsButton.setText("Download Attachments");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 8;
        gridData.grabExcessHorizontalSpace = true;
        this.grabAttachmentsButton.setLayoutData(gridData);
        
        this.grabAttachmentsButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                DirectoryDialog dialog = new DirectoryDialog(SMTPView.this.getShell(), SWT.NONE);
                final String directory = dialog.open();
                if (directory != null)
                {
                    SMTPView.this.setStatus("Getting Attachments...");
                    GrabAttachment grabber = new GrabAttachment();
                    
                    grabber.setLocation(directory);
                    grabber.setToProcess(SMTPView.this.message);
                    Vector<Attachment> attachments = grabber.parseData();
                    Iterator<Attachment> it2 = attachments.iterator();
                    
                    while (it2.hasNext())
                    {
                        Attachment a = it2.next();
                        a.printMe();
                        SMTPView.this.setStatus("Extracting Attachment...");
                        a.writeTheFile();
                        SMTPView.this.setStatus("Attachment Extracted!");
                    }
                    SMTPView.this.setStatus("Attachments Extracted Successfully!");
                }
            }
        });
        
        Label headerInfoLabel = new Label(this.bottomForm, SWT.NONE);
        headerInfoLabel.setText("HEADER INFO:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 8;
        gridData.grabExcessHorizontalSpace = true;
        headerInfoLabel.setLayoutData(gridData);
        
        this.headerInfoValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.headerInfoValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 8;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = false;
        this.headerInfoValueLabel.setLayoutData(gridData);
        
        Label messageLabel = new Label(this.bottomForm, SWT.NONE);
        messageLabel.setText("MESSAGE CONTENT:");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 8;
        gridData.grabExcessHorizontalSpace = true;
        messageLabel.setLayoutData(gridData);
        
        this.messageValueLabel = new Label(this.bottomForm, SWT.NONE);
        this.messageValueLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 200));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 8;
        gridData.grabExcessHorizontalSpace = true;
        this.messageValueLabel.setLayoutData(gridData);
        
        this.mainForm.setWeights(new int[] { 50, 50 });
        return this.mainForm;
    }
    
    @Override
    protected MenuManager createMenuManager()
    {
        MenuManager bar_menu = new MenuManager();
        
        MenuManager file_menu = new MenuManager("File");
        
        bar_menu.add(file_menu);
        file_menu.add(new OpenDBAction(this, this));
        file_menu.add(new ExitAction(this));
        
        return bar_menu;
    }
    
    @Override
    protected ToolBarManager createToolBarManager(int style)
    {
        ToolBarManager toolBarManager = new ToolBarManager(style);
        toolBarManager.add(new OpenDBAction(this, this));
        toolBarManager.add(new Separator());
        
        return toolBarManager;
    }
    
    @Override
    protected StatusLineManager createStatusLineManager()
    {
        return super.createStatusLineManager();
    }
    
    @Override
    public void setStatus(String status)
    {
        super.setStatus("SMTP0wned Version: 1.0 >>> " + status);
    }
    
    public void refresh()
    {
        this.filterTableViewer.refresh();
    }
}
