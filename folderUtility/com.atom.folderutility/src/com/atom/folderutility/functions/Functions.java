package com.atom.folderutility.functions;

//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentQuery;
import com.teamcenter.rac.kernel.TCComponentQueryType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class Functions{
	
	public static String [][] processFileMass(String filePath) throws IOException, TCException {
		List<String[]> attributes = new ArrayList<>();
		try (BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
			String line;
			//String [][] attributes = null;
			//int i = 0;
			while ((line = buffReader.readLine()) != null) {
				String [] values = line.split("\t");
				attributes.add(values);
				//for (int j = 0; j < values.length; j += 1) {
					//attributes[i][j] = values[j];
				//}
				//i += 1;
			}
			System.out.println("The strings have been converted and added to the array.");
			return attributes.toArray(new String[0][]);
		} catch (IOException e) {
			e.printStackTrace();
        	return null;
        }
	}
    
    public static TCComponent findItemRevisonById(TCSession session, String itemId, String revisionNumber) throws TCException {
    	TCComponentQueryType qType = (TCComponentQueryType) session.getTypeComponent("ImanQuery");
    	TCComponentQuery tceItemQuery = (TCComponentQuery) qType.find("AdminItemRevisionSearch");
    	if (tceItemQuery == null) {
            throw new TCException("Query 'AdminItemRevisionSearch' not found!");
        }
    	
    	String entry[] = {"AdminItemID", "AdminRevision"};
    	String value[] = {itemId, revisionNumber};

    	TCComponent[] result = tceItemQuery.execute(entry, value);
    	
    	if (result != null && result.length == 1) {
    		return result[0];
    	} if (result != null && result.length > 1) {
    		throw new TCException("More than one Item with id " + itemId + " exist in the database");
    	} else {
	    	System.out.println("No Item with id " + itemId + " exist in the database");
	    	return null;
    	}
    }
    
    public static TCComponent findItemById(TCSession session, String itemId) throws TCException {
    	TCComponentQueryType qType = (TCComponentQueryType) session.getTypeComponent("ImanQuery");
    	TCComponentQuery tceItemQuery = (TCComponentQuery) qType.find("AdminItemSearch");
    	if (tceItemQuery == null) {
            throw new TCException("Query 'AdminItemSearch' not found!");
        }
    	
    	String entry[] = {"AdminItemID"};
    	String value[] = {itemId};
    	
    	TCComponent[] result = tceItemQuery.execute(entry, value);
    	
    	if (result != null && result.length == 1) {
    		return result[0];
    	} if (result != null && result.length > 1) {
    		throw new TCException("More than one Item with id " + itemId + " exist in the database");
    	} else {
        	System.out.println("No Item with id " + itemId + " exist in the database");
	    	return null;
    	}
    }
    
    public void showErrorDialog(Display display, String errorText) {
    	//Display display = Display.getDefault();
        Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("Message");
        shell.setLayout(new GridLayout(1, false));
        //shell.setSize(300, 150);
        
        //Error text
        //new Label(shell, SWT.NONE).setText(errorText);
        Label label = new Label(shell, SWT.NONE);
        label.setText(errorText);
        label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        
        //Button "OK"
        Button okButton = new Button(shell, SWT.PUSH);
        okButton.setText("OK");
        okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        
        //Action on clicking "OK"
        okButton.addListener(SWT.Selection, e -> shell.close());
        
        shell.pack();
        shell.open();
        
        while (!shell.isDisposed()) {
        	if (!display.readAndDispatch()) {
        		display.sleep();
        	}
        }
    }
}