package com.atom.folderutility.main;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCException;

public class ChangeId extends AbstractHandler{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        ISelection selection = window.getSelectionService().getSelection();

        if (selection instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) selection).getFirstElement();

            if (firstElement instanceof AIFComponentContext) {
            	AIFComponentContext context = (AIFComponentContext) firstElement;
            	TCComponent component = (TCComponent) context.getComponent();
            	if (component instanceof TCComponentItem) {
            		showEditDialog((TCComponentItem) component);
            	} else {
            		System.out.println("Selected object is not a TCComponentItem. Type: " + component.getTypeObject());
            	}
            } else {
            	System.out.println("Selected object is not a TC component");
            }
        } else {
        	System.out.println("Selected object is not structured");
        }
        return null;
    }

    private void showEditDialog(TCComponentItem item) {
        Display display = Display.getDefault();
        Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("Edit Item ID");
        shell.setSize(300, 150);
        shell.setLayout(new GridLayout(2, false));

        // "Current ID" label
        new Label(shell, SWT.NONE).setText("Current Item ID:");
        Text currentIdText = new Text(shell, SWT.BORDER);
        currentIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        currentIdText.setEditable(false);

        // Getting the current ID
        try {
        	currentIdText.setText(item.getProperty("item_id"));
        } catch (TCException e) {
            currentIdText.setText("Error");
        }

        // "New ID" label
        new Label(shell, SWT.NONE).setText("New Item ID:");
        Text newIdText = new Text(shell, SWT.BORDER);
        newIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Button "OK" and "Cancel"
        Button okButton = new Button(shell, SWT.PUSH);
        okButton.setText("OK");
        okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

        Button cancelButton = new Button(shell, SWT.PUSH);
        cancelButton.setText("Cancel");
        cancelButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));

        // Action on clicking "OK"
        okButton.addListener(SWT.Selection, e -> {
            String newId = newIdText.getText().trim();
            if (!newId.isEmpty()) {
                try {
                    item.setProperty("item_id", newId); // Setting a new ID
                    System.out.println("Item ID changed to: " + newId);
                } catch (TCException ex) {
                    System.err.println("Error changing item ID: " + ex.getMessage());
                }
            }
            shell.close();
        });

        // Action on "Cancel" click
        cancelButton.addListener(SWT.Selection, e -> shell.close());

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
	}
	
}