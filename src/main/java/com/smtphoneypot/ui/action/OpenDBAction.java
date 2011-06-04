/**
 * @author Alex Wright
 * @created 9 Mar 2010
 * @file OpenDBAction.java
 */
package com.smtphoneypot.ui.action;

import com.smtphoneypot.core.Database;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import com.smtphoneypot.ui.SMTPView;
import com.smtphoneypot.ui.img.icon.Icon;

/**
 * @author Alex Wright
 */
public class OpenDBAction extends Action
{
    private final ApplicationWindow window;
    private final SMTPView view;
    
    public OpenDBAction(ApplicationWindow window, SMTPView view)
    {
        super("Open Database...", Icon.FOLDER);
        this.setToolTipText("Open a database containing SMTP data.");
        this.window = window;
        this.view = view;
    }
    
    @Override
    public void run()
    {
        
        FileDialog dialog = new FileDialog(this.window.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.db", "*" });
        final String file = dialog.open();
        if (file != null)
        {
            this.view.setStatus("Opening Database...");
            Database.getInstance().connectToDatabase(file);
            this.view.refresh();
            this.view.setStatus("Database Opened Successfully!");
        }
    }
}
