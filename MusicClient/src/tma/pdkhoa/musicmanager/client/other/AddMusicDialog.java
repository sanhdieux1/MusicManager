package tma.pdkhoa.musicmanager.client.other;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import tma.pdkhoa.musicmanager.action.MusicManagement;
import tma.pdkhoa.musicmanager.api.entity.MusicVO;
import tma.pdkhoa.musicmanager.client.view.ListMusicView;

public class AddMusicDialog extends Dialog {
    // ??? why are these protected variable
    private Object result;
    private Shell shlAbout;
    private Text txMusicName;
    private Table table;
    private Spinner spSize;
    private boolean isEdit;
    private ListMusicView listMusicView;
    private MusicManagement musicManagement;

    public AddMusicDialog(Shell parent, Table table, ListMusicView listMusicView) {
        super(parent);
        this.table = table;
        this.listMusicView = listMusicView;
        musicManagement = MusicManagement.getInstance();
    }

    private void setValue() {
        TableItem item = table.getItem(table.getSelectionIndex());
        txMusicName.setText(item.getText(1));
        Double size = Double.parseDouble(item.getText(2));

        Double convertSize = size * Math.pow(10, spSize.getDigits());

        int selectSize = convertSize.intValue();

        spSize.setSelection(selectSize);
    }

    public void setListMusicView(ListMusicView listMusicView) {
        this.listMusicView = listMusicView;
    }

    public Object open(boolean edit) {
        this.isEdit = edit;
        shlAbout = new Shell(getParent(), SWT.BORDER | SWT.CLOSE | SWT.PRIMARY_MODAL);
        shlAbout.setText("Add Music");
        shlAbout.setSize(380, 145);
        shlAbout.setLayout(new RowLayout(SWT.VERTICAL));

        Composite composite = new Composite(shlAbout, SWT.NONE);
        composite.setLayoutData(new RowData(400, SWT.DEFAULT));
        RowLayout rl_composite = new RowLayout(SWT.HORIZONTAL);
        rl_composite.center = true;
        composite.setLayout(rl_composite);

        Label label_2 = new Label(composite, SWT.NONE);
        label_2.setLayoutData(new RowData(100, SWT.DEFAULT));
        label_2.setText("Music name");

        Label label_3 = new Label(composite, SWT.NONE);
        label_3.setText(":");

        txMusicName = new Text(composite, SWT.BORDER);
        txMusicName.setLayoutData(new RowData(180, SWT.DEFAULT));

        Composite composite_1 = new Composite(shlAbout, SWT.NONE);
        RowLayout rl_composite_1 = new RowLayout(SWT.HORIZONTAL);
        rl_composite_1.center = true;
        composite_1.setLayout(rl_composite_1);

        Label lblNewLabel = new Label(composite_1, SWT.NONE);
        lblNewLabel.setLayoutData(new RowData(100, SWT.DEFAULT));
        lblNewLabel.setText("Size");

        Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
        lblNewLabel_1.setText(":");

        spSize = new Spinner(composite_1, SWT.BORDER);
        spSize.setMaximum(999999);
        spSize.setDigits(2);
        spSize.setLayoutData(new RowData(50, SWT.DEFAULT));

        Label lblNewLabel_2 = new Label(composite_1, SWT.NONE);
        lblNewLabel_2.setText("Mb");

        Composite composite_2 = new Composite(shlAbout, SWT.NONE);
        Button btnOk = new Button(composite_2, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int selection = spSize.getSelection();
                int digits = spSize.getDigits();
                double size = (double) (selection / Math.pow(10, digits));
                System.out.println(size);
                if (isEdit) {
                    TableItem[] items = table.getSelection();
                    if (items.length > 0) {
                        int id = Integer.parseInt(items[0].getText(0));
                        MusicVO musicVO = new MusicVO();
                        musicVO.setId(id);
                        musicVO.setFilename(txMusicName.getText());
                        musicVO.setSize(size);
                        // call service insert music
                        musicManagement.editMusic(musicVO);
                        listMusicView.refreshDataTable();
                    }
                } else {
                    // create a music
                    MusicVO musicVO = new MusicVO();
                    musicVO.setFilename(txMusicName.getText());
                    musicVO.setSize(size);
                    musicManagement.addMusic(musicVO);
                    listMusicView.refreshDataTable();
                }
                // close dialog
                shlAbout.close();
            }
        });
        btnOk.setBounds(0, 0, 75, 25);
        btnOk.setText("OK");
        if (isEdit) {
            setValue();
        }
        shlAbout.open();
        return result;

    }
}
