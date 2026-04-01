package com.atom.folderutility.main;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

import com.atom.folderutility.functions.*;
import com.atom.folderutility.connection.*;

public class ChangeAttrHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
        FileDialog fileDialog = new FileDialog(shell);
        String filePath = fileDialog.open();
        TCSession session = null;
		try {
			session = Connection.getSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
        if (filePath != null) {
        	try {
				String [][] attributesValues = Functions.processFileMass(filePath);
				if (attributesValues[0].length >= 4 & attributesValues[0].length % 2 == 0) {
					for (int i = 0; i < attributesValues.length; i += 1) {
						TCComponent item = Functions.findItemRevisonById(session, attributesValues[i][0], attributesValues[i][1]);
						if (item != null) {
				            for (int j = 2; j < attributesValues[i].length; j += 2) {
				                String attributeName = attributesValues[i][j];
				                String newValue = attributesValues[i][j+1];
				                try {
				                    item.setProperty(attributeName, newValue);
				                    System.out.println("Attribute " + attributeName + " of item " + attributesValues[i][0] + " changed to " + newValue);
				                } catch (TCException e) {
				                    System.err.println("Error updating attribute " + attributeName + " for item " + attributesValues[i][0] + ": " + e.getMessage());
				                }
				            }
				        } else {
				        	String errorMessage = "Item with ID " + attributesValues[i][0] + " not found.";
							MessageDialog.openError(shell, "Change Item Attributes Error", errorMessage);
				            System.out.println("Item with ID " + attributesValues[i][0] + " not found.");
				        }	
					}
				} else if (attributesValues[0].length >= 3 & attributesValues[0].length % 2 == 1) {
					for (int i = 0; i < attributesValues.length; i += 1) {
						TCComponent item = Functions.findItemById(session, attributesValues[i][0]);
						if (item != null) {
				            for (int j = 1; j < attributesValues[i].length; j += 2) {
				                String attributeName = attributesValues[i][j];
				                String newValue = attributesValues[i][j+1];
				                try {
				                    item.setProperty(attributeName, newValue);
				                    System.out.println("Attribute " + attributeName + " of item " + attributesValues[i][0] + " changed to " + newValue);
				                } catch (TCException e) {
				                    System.err.println("Error updating attribute " + attributeName + " for item " + attributesValues[i][0] + ": " + e.getMessage());
				                }
				            }
				        } else {
				            System.out.println("Item with ID " + attributesValues[i][0] + " not found.");
				        }	
					}
				} else {
					String errorMessage = "Invalid file format";
					MessageDialog.openError(shell, "Change Item Attributes Error", errorMessage);
					System.out.println("Invalid file format");
				}
			} catch (IOException | TCException e) {
				IStatus status = new Status(IStatus.ERROR, "CreateElements", "Error while changing elements attributes", e);
        		StatusManager.getManager().handle(status, StatusManager.SHOW | StatusManager.LOG);
				e.printStackTrace();
			}
		} 
        return null;
    }
}