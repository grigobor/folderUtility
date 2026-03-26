package com.atom.folderutility.main;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCException;
import com.atom.folderutility.functions.Functions;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import java.io.IOException;


import com.atom.folderutility.connection.*;

public class LoadElement extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
    	System.out.println("Load Element plugin selected");
    	
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
        if (filePath != null) {
        	System.out.println("File Selected: " + filePath);
        	try {
        		String [][] elements = Functions.processFileMass(filePath);
        		if (elements[0].length == 2) {
            		for (int i = 0; i < elements.length; i += 1) {
            			TCComponent itemRevision = Functions.findItemRevisonById(Connection.getSession(), elements[i][0], elements[i][1]);
            			if (itemRevision != null) {
            	        	targetFolder.add("contents", itemRevision); 
            	            System.out.println(itemRevision.toString() + " moved to " + targetFolder.toString());
            	        } else {
            	        	//Returns a search error depending on the object being searched for
            	        	System.out.println("Item:  " + elements[i][0] + "/" + elements[i][1] + " not found");
            	        }
            		}
        		} else if (elements[0].length == 1) {
        			for (int i = 0; i < elements.length; i += 1) {
            			TCComponent item = Functions.findItemById(Connection.getSession(), elements[i][0]);
            			if (item != null) {
            	        	targetFolder.add("contents", item); 
            	            System.out.println(item.toString() + " moved to " + targetFolder.toString());
            	        } else {
            	        	//Returns a search error depending on the object being searched for
            	        	System.out.println("Item: " + elements[i][0] + " not found");
            	        }
            		}
        		} else {
        			System.out.println("Invalid file format");
        		}

        	} catch (IOException | TCException e) {
				e.printStackTrace();
			}
        }
		return null;
    }
}