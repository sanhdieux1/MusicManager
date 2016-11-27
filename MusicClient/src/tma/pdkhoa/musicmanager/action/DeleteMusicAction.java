package tma.pdkhoa.musicmanager.action;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DeleteMusicAction extends SelectionAdapter {

    private int m_id;
    public DeleteMusicAction(int id) {
        m_id = id;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        super.widgetSelected(e);
        MusicManagement.getInstance().deleteMusic(m_id);
    }
    
}
