package com.atom.folderutility.functions;

//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
			System.out.println("Строки преобразованы и добавлены в массив");
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
}