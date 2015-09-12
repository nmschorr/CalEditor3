/* SFCALeditor.java by Nancy Schorr, 2015
 *
 * This program removes extra calendar events from an ics calendar file.
 * 
 * The data for the calendar is calculated with Solar Fire 8 on Windows XP.  
 * It is run through the Perl script, then the Word macros.  
 * Ideally that would all take place in one Java executable -
 * but that's a project for the future.

 * This little program is the final step in preparing the SFCAL.ics file 
 * for uploading in Google or another calendar system.
**/

// Note: can't use an iterator because the file isn't being read that way

package com.nmschorr.SFCAL_standard;

import java.io.File;
import java.io.IOException;
import java.util.*;
import static java.lang.System.out;
import org.apache.commons.io.FileUtils;
import com.nmschorr.SFCAL_standard.SectionNew;

/**
 * This class contains methods that remove extra calendar events from an ics calendar file.
 * @author Nancy M. Schorr
 * @version 1.1
 * 
 */

public class SFCALstandard extends SFCALstandardutil {
	static int G_VERBOSE=0;
	
	 
	public static void main(String[] args) {	
		String indirMAIN = getindir();
		String outDIR = getoutdir();
		String outDIRTMP = outDIR + "\\tempfiles";
		String[] arryOfInFiles = getflist(indirMAIN);	// create a list of names of those files	
		int fileInDirCNT=0;
		
		//int arraysize = arryOfInFiles.length;
	int arraysize = 1;

		while (fileInDirCNT < arraysize) {  
			String infileNM= arryOfInFiles[fileInDirCNT];
			String inFILEstr = indirMAIN +"\\" + infileNM;
			   
			out.println("-- starting over in main LOOP# " + fileInDirCNT+1 +" filename is: " + infileNM);
			
			String finFILEnmWdir = mkDateFileNM(inFILEstr, infileNM, outDIR);
			delFiles(finFILEnmWdir);  // delete the inFileName we made last time
			 
			String tOUTone = getTMPnmWdir(outDIRTMP,"-one");
			String tOUTtwo = getTMPnmWdir(outDIRTMP,"-two");;
				
			generalStringFixing( inFILEstr, tOUTone);
			
			SectionNew.sectionTask(tOUTone, tOUTtwo, finFILEnmWdir);
			
			out.println("- datefilename is: " + finFILEnmWdir+"--------End of Loop------------NEW filename is: "+finFILEnmWdir);		
			
			fileInDirCNT++;		
		}			
		out.println("Finished Program");
	}

	// new method: ----------------------------------------------------------------	
	static String getindir() {  // 1 for name, 2 for file
		String iDIRNM = "E:\\sfcalfiles\\standard";
		return iDIRNM;
		}
	 
	// new method: ----------------------------------------------------------------	
	static String getoutdir() {  // 1 for name, 2 for file
		String oDIRNM =  "C:\\SFCALOUT\\standard";
		return oDIRNM;
		}

	static String getTMPnmWdir(String tnm, String myIn) {  // 1 for name, 2 for file
		String sNAME = tnm + "\\SFCALtmp" + System.currentTimeMillis() +myIn +".ics";
		return sNAME;
		}

	static String[] getflist(String dnm) {  // 1 for name, 2 for file
		File filesDir = new File(dnm);  //READ the list of files in sfcalfiles/vds dir
 		String[] arryOfInFiles = filesDir.list();	// create a list of names of those files	
 		return arryOfInFiles;
		}

	public static void verboseOut(String theoutline) {
		if (G_VERBOSE==1) {
			out.println(theoutline);
		}
	}


	
// new method // --------------------------------------------------------------	 	
	static String fixDESCRIPTION_line( String  inSTRING) {
		CharSequence badLINEFchars = "\\n";
		String badLINEFstr = (String)badLINEFchars;
		String newstr = "";
		out.println("just entered gofixDES. oldstrg is: " +inSTRING );

		String tString = inSTRING.replaceAll("%0A","");  // get rid of CRs  - \n
		if (tString.contains(badLINEFchars))    // for newline only
			newstr = tString.replace(badLINEFstr, " - ");
		else newstr = tString;
		
		tString = continueReplacing(newstr);
	 		
		if (tString.startsWith(" "))   // spelling errors in extra lines of DESCRIPTION
			tString = newrepl(tString);
	
		return tString;
	}

	static String newrepl(String localSTR) {
		String oldVal;
		String newVal;
		String newstr=localSTR;
		HashMap<String, String> hMAP = makeSpellhm();
		for (String key : hMAP.keySet()) {
			oldVal= key;
			newVal= hMAP.get(key);
			out.println("\n\n" + "!!!----- value of hmap retrieval: " + oldVal + " " + newVal);
			if (localSTR.contains((CharSequence)oldVal)) {
			    newstr = localSTR.replace(oldVal, newVal);
				out.println("SPELLING ERROR!!!! ----------replaced string with new string... now fixed: " + newstr);
			}
		} //for
	return newstr;
	}
		
		
		
		
		
	static String continueReplacing(String fixmeSTR) {
		String newTempStr = fixmeSTR.replace("Transiting ","" );
		fixmeSTR= newTempStr.replace("Conjunction","Conjunct"); 
		newTempStr= fixmeSTR.replace("Opposition","Opposite"); 
		fixmeSTR = newTempStr.replace("Entering","Enters" );
		newTempStr = fixmeSTR.replace("DESCRIPTION:The ","DESCRIPTION:" );

		out.println("new string... now fixed: " + newTempStr+ "value of instr:  " + fixmeSTR+ "return this new value  " + newTempStr);
		return newTempStr;
	}

	
// new method // --------------------------------------------------------------	 	
	static String fixSUMMARYsigns(String oldstrg, boolean isDIRorRET) {
		Map<String, String> hm  =  makeNewhash();
		String tstring = oldstrg.replace("SUMMARY: ", "SUMMARY:");
		 
		String newstr = "empty";
		StringBuffer newbuf = new StringBuffer(tstring);

		List<String> signsARRAY = (Arrays.asList("Ari", "Tau","Gem", "Can", "Leo", 
			"Vir", "Lib","Sco", "Sag", "Cap", "Aqu", "Pis"));
		boolean signmatch =false;
	 		 		
		String firstthird = "";
		String secondthird = "";
		String lastthird = "";
		
		if (isDIRorRET==false) {
			lastthird = tstring.substring(22,25);
			firstthird = tstring.substring(14,17);
			if (signsARRAY.contains(lastthird))				
				signmatch = true;
		}
		out.println("in fixSUMMARYsigns. first:  " + firstthird+" 2nd  :  " + secondthird+" 3rd  :  " + lastthird);
//begin third column		    
		if (isDIRorRET==false) {
			String thirdrep = hm.get(lastthird);			int start = 22;
			int end = 25;
			newbuf.delete(start, end); 
			newbuf.insert(start,thirdrep);
			out.println("found this in hash:  " + lastthird+"new buf is: " + newbuf);		
		}		
//begin second column
		if  (isDIRorRET==false) {
			secondthird = tstring.substring(18,21);
			String secondrep = hm.get(secondthird);
			int start = 18;
			int end = 21;
			newbuf.delete(start, end); 
			if (signmatch) {   // change Conjunct a sign to Enters a sign
				newbuf.insert(start,"Enters");
			} else {
				newbuf.insert(start,secondrep);
			}
			out.println("value of signmatch:  " + signmatch+ "found this in hash:  " + secondrep+"new buf is: " + newbuf);
		}			
// begin first column		
		if (isDIRorRET==true ) {
			firstthird = tstring.substring(8,11);
			String longsign = hm.get(firstthird);
			newstr = tstring.replace(firstthird, longsign);
		}
		if (isDIRorRET==false ) {
			String firstrep = hm.get(firstthird);

			int start = 8;
			int end = 17;
			newbuf.delete(start, end); 
			newbuf.insert(8,firstrep);
			newstr =   newbuf.toString();
			out.println("found this in hash:  " + firstrep + "new buf is: " + newbuf);
			out.println("replaced string with new string... now fixed: " + newstr);
			out.println("value of newstr:  " + newstr+ "return this new value  " + newstr);
		}
		
		return newstr;
	} // gofixhash
	
	
	
// new method // --------------------------------------------------------------	 	
	static HashMap<String, String> makeSpellhm() {
		HashMap <String, String> spellhm  =  new HashMap<String, String>();
		spellhm.put("Stabilise","Stabilize");
		spellhm.put("Socialise","Socialize");
		spellhm.put("Entering","Enters");
		spellhm.put("organised","organized");
		spellhm.put("excelent","excellent");
		spellhm.put("realise","realize");
		spellhm.put("spiritualilty","spirituality");
		spellhm.put("wilfull","willful");
		spellhm.put("possibiities","possibilities");
		spellhm.put("fantasise","fantasize");
		return spellhm;
	}


	
// new method // --------------------------------------------------------------	 	
	static String mkDateFileNM(String oldFileWDir, String oldname, String newfiledir) {
		List<String> oldfileAR = new ArrayList<String>();
		String newDateNM = "";
		String theTst = "DTSTART";
		int whileCT = 0;
		String tmpSTR = "";
		String newDateStr = "";
		int indColon = 0;
		int tStart=0;
		int tEnd=0;
		
		try {
			oldfileAR =  FileUtils.readLines(new File(oldFileWDir));  //READ the list of files in sfcalfiles/vds dir
			
			while ( whileCT < 15) {
				tmpSTR = oldfileAR.get(whileCT);
				if ( tmpSTR.contains(theTst)) {
					indColon = tmpSTR.indexOf(":");
					tStart=indColon+1;
					tEnd=tStart+8;
					
			        newDateStr = tmpSTR.substring(tStart, tEnd);
			        newDateNM = newfiledir + "\\" + oldname + "." + newDateStr + ".ics";
			        break;
			  }
			  whileCT++;
			}
		} catch (IOException e) { 
			e.printStackTrace();	
		}	// catch

		return newDateNM; 
	}

}  // class
 