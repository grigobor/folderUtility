package com.atom.folderutility.connection;

import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCSession;


public class Connection{
	public static TCSession getSession() {
		        try {
		        	return (TCSession) AIFUtility.getDefaultSession();
		        } catch (Exception e){
		        	e.printStackTrace();
		        	return null;
		        }
		    }
}