package tma.pdkhoa.musicmanager.client.view;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;

public class GridLayoutComplex extends ViewPart {

    public static final String ID = "tma.pdkhoa.musicmanager.client.view.GridLayoutComplex"; //$NON-NLS-1$
    private Button one;
    private Button two;
    private GridData data_1;
    private GridData data_2;
    private GridData data_3;
    private GridData data_4;

    public GridLayoutComplex() {
    }

    /**
     * Create contents of the view part.
     * 
     * @param parent
     */
    @Override
    public void createPartControl(Composite shell) {
        shell.setLayout(new GridLayout(3, true));

        one = new Button(shell, SWT.NONE);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 200;
        one.setLayoutData(data);
        one.setText("one");
        Composite composite = new Composite(shell, SWT.NONE);
        data_3 = new GridData(GridData.FILL_BOTH);
        data_3.horizontalSpan = 2;
        composite.setLayoutData(data_3);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 15;
        composite.setLayout(layout);
     // Create button "two"
        two = new Button(composite, SWT.NONE);
        GridData gd_two = new GridData(GridData.FILL_BOTH);
        gd_two.grabExcessVerticalSpace = false;
        gd_two.verticalAlignment = SWT.CENTER;
        two.setLayoutData(gd_two);
        two.setText("two");

        // Create button "three"
        data_2 = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
        data_2.horizontalAlignment = SWT.CENTER;
        data_2.verticalAlignment = SWT.CENTER;
        Button three = new Button(composite, SWT.PUSH);
        three.setText("three");
        three.setLayoutData(data_2);

        // Create button "four"
        data_4 = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        Button four = new Button(composite, SWT.PUSH);
        four.setText("four");
        four.setLayoutData(data_4);
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);
        new Label(shell, SWT.NONE);

        // Create the long button across the bottom
        data_1 = new GridData();
        data_1.horizontalAlignment = GridData.FILL;
        data_1.horizontalSpan = 3;
        data_1.heightHint = 150;
        Button five = new Button(shell, SWT.PUSH);
        five.setText("five");
        five.setLayoutData(data_1);

        createActions();
        initializeToolBar();
        initializeMenu();
    }

    public void dispose() {
        super.dispose();
    }

    /**
     * Create the actions.
     */
    private void createActions() {
        // Create the actions
    }

    /**
     * Initialize the toolbar.
     */
    private void initializeToolBar() {
        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
    }

    /**
     * Initialize the menu.
     */
    private void initializeMenu() {
        IMenuManager manager = getViewSite().getActionBars().getMenuManager();
    }

    @Override
    public void setFocus() {
        // Set the focus
    }
}
