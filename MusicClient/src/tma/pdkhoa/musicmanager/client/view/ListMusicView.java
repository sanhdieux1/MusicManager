package tma.pdkhoa.musicmanager.client.view;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import tma.pdkhoa.musicmanager.action.DeleteMusicAction;
import tma.pdkhoa.musicmanager.action.MusicManagement;
import tma.pdkhoa.musicmanager.api.entity.MusicVO;
import tma.pdkhoa.musicmanager.client.other.AddMusicDialog;

public class ListMusicView extends ViewPart {
    private AddMusicDialog addMusicDialog;
    private MusicManagement musicManagement;
    private List<MusicVO> musicVODataList;
    public static final String ID = "views.listmusic";
    private Composite buttonComposite;
    private Composite composite;
    private Composite btnComposite;
    private Table m_table;
    private TableViewer m_tableViewer;

    public ListMusicView() {
        musicManagement = MusicManagement.getInstance();
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        buttonComposite = new Composite(parent, SWT.NONE);
        buttonComposite.setLayout(new GridLayout(1, false));
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        btnComposite = new Composite(buttonComposite, SWT.NONE);
        btnComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        btnComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

        Button btnRefresh = new Button(btnComposite, SWT.NONE);
        btnRefresh.setSize(51, 25);
        btnRefresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                refreshDataTable();
            }
        });
        btnRefresh.setText("Refresh");

        addMusicDialog = new AddMusicDialog(parent.getShell(), m_table, this);
        Button btnAdd = new Button(btnComposite, SWT.NONE);
        btnAdd.setSize(51, 25);
        btnAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addMusicDialog.open(false);
            }
        });
        btnAdd.setText("Add");

        composite = new Composite(buttonComposite, SWT.NONE);
        composite.setLayout(new FillLayout(SWT.HORIZONTAL));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        m_tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
        createContentProvider(m_tableViewer);
        createColumn(m_tableViewer);
        m_table = m_tableViewer.getTable();
        m_table.setLinesVisible(true);
        m_table.setHeaderVisible(true);
        createMenuTable(m_table);
        loadDataTable();
    }

    private void createMenuTable(Table table) {
        table.addMenuDetectListener(new MenuDetectListener() {
            @Override
            public void menuDetected(MenuDetectEvent e) {
                IStructuredSelection currentSelection = m_tableViewer.getStructuredSelection();
                Object element = currentSelection.getFirstElement();
                MusicVO musicVO = null;
                if (element instanceof MusicVO) {
                    musicVO = (MusicVO) element;
                }
                Menu contextMenu = new Menu(table);
                table.setMenu(contextMenu);
                MenuItem item = new MenuItem(contextMenu, SWT.None);
                item.setText("delete");
                if (musicVO != null) {
                    item.setEnabled(true);
                    item.addSelectionListener(new DeleteMusicAction(musicVO.getId()));
                } else {
                    item.setEnabled(false);
                }
            }
        });
    }

    private void createContentProvider(TableViewer tableViewer) {
        tableViewer.setContentProvider(new ArrayContentProvider());
    }

    private void createColumn(TableViewer tableViewer) {
        TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        TableColumn sttCol = tableViewerColumn.getColumn();
        sttCol.setWidth(100);
        sttCol.setText("ID");
        tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof MusicVO) {
                    MusicVO musicVO = (MusicVO) element;
                    return String.valueOf(musicVO.getId());
                }
                return "";
            }
        });

        TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
        TableColumn nameCol = tableViewerColumn_1.getColumn();
        nameCol.setWidth(100);
        nameCol.setText("Name");
        tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof MusicVO) {
                    MusicVO musicVO = (MusicVO) element;
                    return musicVO.getFilename();
                }
                return "";
            }
        });
        
        TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
        TableColumn sizeCol = tableViewerColumn_2.getColumn();
        sizeCol.setText("Size");
        sizeCol.setResizable(true);
        sizeCol.setWidth(200);
        sizeCol.pack();
        tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof MusicVO) {
                    MusicVO musicVO = (MusicVO) element;
                    return String.valueOf(musicVO.getSize());
                }
                return "";
            }
        });
        
    }

    private List<MusicVO> getMusicDataList() {
        musicVODataList = musicManagement.getListMusic();
        return musicVODataList;
    }

    @Override
    public void setFocus() {

    }

    public void loadDataTable() {
        @SuppressWarnings("unused")
        List<MusicVO> listMusics = getMusicDataList();
        m_tableViewer.setInput(listMusics);
    }

    public void refreshDataTable() {
        if (m_table.getItems().length > 0) {
            m_table.removeAll();
        }
        loadDataTable();
    }

    class ViewContentProvider implements ITreeContentProvider {
        private final Object[] EMTY = new Object[] {};

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        @Override
        public void dispose() {
        }

        @Override
        public Object[] getElements(Object inputElement) {
            if (musicVODataList == null) {
                return EMTY;
            } else {
                return musicVODataList.toArray();
            }
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            return EMTY;
        }

        @Override
        public Object getParent(Object element) {
            return element;
        }

        @Override
        public boolean hasChildren(Object element) {

            return (element instanceof MusicVO);
        }

    }
}
