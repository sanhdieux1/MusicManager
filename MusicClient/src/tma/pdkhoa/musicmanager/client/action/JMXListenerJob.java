package tma.pdkhoa.musicmanager.client.action;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import tma.pdkhoa.musicmanager.client.connector.JMSListener;
import tma.pdkhoa.musicmanager.client.view.ListMusicView;

public class JMXListenerJob extends Job {
    private JMSListener jmsListener;
    public JMXListenerJob(String name) {
        super(name);
        jmsListener = JMSListener.getInstance();
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        while (true) {
            if (jmsListener.isConnected()) {
                String msg = jmsListener.topicReceiveMessage();
                if (msg.equals("REFRESH_DATA")) {
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            IWorkbench workbench = PlatformUI.getWorkbench();
                            IViewPart part = workbench.getActiveWorkbenchWindow().getActivePage().findView(ListMusicView.ID);
                            ListMusicView view = null;
                            if (part instanceof ListMusicView) {
                                view = (ListMusicView) part;
                            }
                            if (view != null)
                                view.refreshDataTable();
                        }
                    });
                }
            }else{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
