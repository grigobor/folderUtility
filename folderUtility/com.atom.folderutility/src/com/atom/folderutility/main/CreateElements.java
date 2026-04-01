package com.atom.folderutility.main;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.*;
import com.atom.folderutility.connection.*;
import com.atom.folderutility.functions.*;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.statushandlers.StatusManager;

public class CreateElements extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
    	
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		
    	//Getting the selected item from the active window
    	ISelection selection = HandlerUtil.getCurrentSelection(event); 
    	
    	//Checking that the selected item is a structural element
    	if (!(selection instanceof IStructuredSelection)) { 
			return null;
		}
    	
    	//Only the first of the selected items is taken if multiple are selected
		Object selectedObject = ((IStructuredSelection) selection).getFirstElement(); 
		
		//Checks that the selected element is a TCComponent
		if (!(selectedObject instanceof AIFComponentContext)) {
			String errorMessage = "The selected item is not TCComponent";
			MessageDialog.openError(shell, "Create Item error", errorMessage);
			System.out.println("The selected item is not TCComponent");
			return null;
		}
		
		/**Unpacking the selected item to determine the component type,
		 * as the selected item is wrapped inside an AIFComponentContext
		 */
		AIFComponentContext object = (AIFComponentContext) selectedObject;
		TCComponent selectedComponent = (TCComponent) object.getComponent();

        TCComponentFolder targetFolder = null;  // Initialize targetFolder
        if (selectedComponent instanceof TCComponentFolder) {
            targetFolder = (TCComponentFolder) selectedComponent;
        } else {
            System.out.println("The selected item is not folder.");
            String errorMessage = "The selected item is not folder.";
			MessageDialog.openError(shell, "Create Item error", errorMessage);
            return null;
        }
        //Opening the file picker to upload items
        //Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
        FileDialog fileDialog = new FileDialog(shell);
        String filePath = fileDialog.open();
        TCComponent newItem;

        if (filePath != null) { // if file path not empty, then do what needed
        	try {
        		String [][] elements = Functions.processFileMass(filePath);
        		TCSession session = Connection.getSession();
        		for (int i = 0; i < elements.length; i+= 1) { // cycle for working with every row of file
        			TCComponent findItem = Functions.findItemById(Connection.getSession(), elements[i][0]); // checking if object already in Teamcenter base
        			if (findItem == null) { // condition that start only when there is no object with this ID
        				TCComponentItemType itemTypeComponent = (TCComponentItemType)session.getTypeComponent(elements[i][2]); // getting object type. This is necessary for correct object creation via TC API
        	    		newItem = itemTypeComponent.create(elements[i][0], elements[i][1], elements[i][2], elements[i][3], "", new TCComponent()); // creating new object with following attributes: ID, revision number, type, name 
        	    		targetFolder.add("contents", newItem); // adding object to folder, that was selected
        	    		TCComponent newItemRevision = Functions.findItemRevisonById(Connection.getSession(), elements[i][0], elements[i][1]); // finding revision of item, that was created
        	    		for (int j = 4; j < elements[i].length; j += 2) { // for revision newItemRevision setting attribute value
        	    			System.out.println("Seting attribute to" + elements[i][0] + "/" + elements[i][1]);
        	    			String attributeName = elements[i][j];
			                String newValue = elements[i][j+1];
			                try {
			                	newItemRevision.setProperty(attributeName, newValue);
			                    System.out.println("Attribute " + attributeName + " of item " + elements[i][0] + " changed to " + newValue);
			                } catch (TCException e) {
			                	String errorMessage = "Error updating attribute " + attributeName + " for item " + elements[i][0] + ": " + e.getMessage();
		        				MessageDialog.openError(shell, "Create Item error", errorMessage);
			                    System.err.println("Error updating attribute " + attributeName + " for item " + elements[i][0] + ": " + e.getMessage());
			                }
        	    		}
        			}
        			else {
        				String errorMessage = "Item " + findItem + " already exist in Teamcenter.";
        				MessageDialog.openError(shell, "Create Item error", errorMessage);
        				System.out.println("Item " + findItem + " already exist in Teamcenter.");
        			}
        		}
        	} catch (IOException | TCException e) {
        		IStatus status = new Status(IStatus.ERROR, "CreateElements", "Error while creating elements and setting attributes.", e);
        		StatusManager.getManager().handle(status, StatusManager.SHOW | StatusManager.LOG);
        		System.out.println("Error while creating elements and setting attributes.");
				e.printStackTrace();
			}
        } else {
        	String errorMessage = "File not selected";
			MessageDialog.openError(shell, "Create Item error", errorMessage);
        	System.out.println("File not selected");
        }
		return null;
    }
}