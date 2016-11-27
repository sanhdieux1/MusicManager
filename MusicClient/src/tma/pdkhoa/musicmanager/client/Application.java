package tma.pdkhoa.musicmanager.client;

import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import tma.pdkhoa.musicmanager.client.action.JMXListenerJob;
import tma.pdkhoa.musicmanager.client.connector.JMSListener;
import tma.pdkhoa.musicmanager.client.ultils.PropertiesMessage;
import tma.pdkhoa.musicmanager.client.view.ListMusicView;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {
    private Job job = null;
    

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
     * IApplicationContext)
     */
    public Object start(IApplicationContext context) {

        // create JMS listen
        createJMSListener();

        Display display = PlatformUI.createDisplay();
        ApplicationWorkbenchAdvisor myAdvisor;
        try {
            myAdvisor = new ApplicationWorkbenchAdvisor();
            int returnCode = PlatformUI.createAndRunWorkbench(display, myAdvisor);

            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            }
            return IApplication.EXIT_OK;
        } finally {
            display.dispose();
        }
    }

    private void createJMSListener() {
        
        job = new JMXListenerJob(PropertiesMessage.getMessage("jmx.started"));
        job.schedule();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
    public void stop() {
        if (!PlatformUI.isWorkbenchRunning())
            return;
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (job != null)
            job.cancel();
        // final IWorkbench workbench = PlatformUI.getWorkbench();
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {
            public void run() {
                if (!display.isDisposed())
                    workbench.close();
            }
        });
    }

}
