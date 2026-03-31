package com.atom.folderutility.main;

import java.io.IOException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.*;
import com.atom.folderutility.connection.*;
import com.atom.folderutility.functions.*;

public class CreateElements extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
    	System.out.println("Create Elements plugin selected");
    	
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
            return null;
        }
        //Opening the file picker to upload items
        Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
        System.out.println("Window open");
        FileDialog fileDialog = new FileDialog(shell);
        String filePath = fileDialog.open();
        TCComponent newItem;
        if (filePath != null) {
        	System.out.println("File Selected: " + filePath);
        	try {
        		String [][] elements = Functions.processFileMass(filePath);
        		TCSession session = Connection.getSession();
        		for (int i = 0; i < elements.length; i+= 1) {
        			TCComponent findItem = Functions.findItemById(Connection.getSession(), elements[i][0]);
        			if (findItem == null) {
        				TCComponentItemType itemTypeComponent = (TCComponentItemType)session.getTypeComponent(elements[i][2]);
        	    		newItem = itemTypeComponent.create(elements[i][0], elements[i][1], elements[i][2], elements[i][3], "", new TCComponent());       				
        	    		targetFolder.add("contents", newItem);
        	    		for (int j = 4; j < elements[i].length; j += 2) {
        	    			System.out.println("Seting attribute to" + elements[i][0]);
        	    			String attributeName = elements[i][j];
			                String newValue = elements[i][j+1];
			                try {
			                	newItem.setProperty(attributeName, newValue);
			                    System.out.println("Attribute " + attributeName + " of item " + elements[i][0] + " changed to " + newValue);
			                } catch (TCException e) {
			                    System.err.println("Error updating attribute " + attributeName + " for item " + elements[i][0] + ": " + e.getMessage());
			                }
        	    		}
        			}
        			else {
        				System.out.println("Item " + findItem + " already exist in Teamcenter.");
        			}
        		}
        	} catch (IOException | TCException e) {
				e.printStackTrace();
			}
        }
		return null;
    }
}