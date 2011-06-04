package com.smtphoneypot.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;
import com.smtphoneypot.ui.img.icon.Icon;

public class ExitAction extends Action
{
    private final ApplicationWindow window;
    
    public ExitAction(ApplicationWindow window)
    {
        super("Exit", Icon.DOOR_IN);
        this.window = window;
        this.setToolTipText("Exit SMTP0wned");
    }
    
    @Override
    public void run()
    {
        this.window.close();
    }
}
