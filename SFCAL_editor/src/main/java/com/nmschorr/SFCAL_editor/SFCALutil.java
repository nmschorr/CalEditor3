/**
 * This class contains methods that remove extra calendar events from an ics calendar file.
 * @author Nancy M. Schorr
 * @version 1.1
 * 
 */
package com.nmschorr.SFCAL_editor;


import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import static com.nmschorr.SFCAL_editor.SFCALeditor.*;
import static java.lang.System.out;
	

public class SFCALutil {
	final static String LFEED = System.getProperty("line.separator");
	
	static void generalStringFixing(String SFCALtempOneFilename, String infile) {   
		List<String> newLINEARRY = new ArrayList<String>();
		String newDTENDline = "";
		String utilLINE1 = "";
		String utilLINE2 = "";
		String curSTR = "";
		File SFCALtempONE = new File(SFCALtempOneFilename);
		File oldFILE = new File(infile);
		boolean addDTEND=false;

		try {
			List<String> oldARRY =  FileUtils.readLines(oldFILE);
			out.println("--------------- %%%%%%%##### total lines: " +  oldARRY.size());
			// get ics header lines first four header lines of ics inFile 
			int arrySize = oldARRY.size();
			int mockCT = 0;
			int realINLINEct = 0;

			while (realINLINEct < arrySize)  {
				utilLINE1 = "";
				utilLINE2 = "";
				curSTR = "";
				curSTR = oldARRY.get(realINLINEct);
				out.println("mock line count: " + mockCT);
				addDTEND=false;
			
				if (curSTR.length() > 0 )
				{
					verboseOut("current line:"+curSTR);
					utilLINE2= StringUtils.chomp(curSTR);
					utilLINE1= checkForSignQuote(utilLINE2);

					if (utilLINE1.startsWith("SUMMARY:")) {
						utilLINE2 = replaceSigns(utilLINE1);
					} else utilLINE2 = utilLINE1;

					if (utilLINE2.contains("Moon goes void")) {
						utilLINE1 = "SUMMARY:Moon void of course";
					}
					else { 	utilLINE1 = utilLINE2; }

					if ( utilLINE1.substring(0,6).equals("DTSTAR") )   {   // add DTEND line, chg  start line ending to 5Z to add 5 secs	 			
						newDTENDline = createDTEND(utilLINE1);
						utilLINE2 = fixDTSTART(utilLINE1);
						addDTEND = true;
						mockCT++;  // add extra line to count for extra DTEND string created
					}
					else { 	utilLINE2 = utilLINE1; }				
				}  //if curSTR				
				newLINEARRY.add(utilLINE2);
				if (addDTEND==true) { 
					newLINEARRY.add(newDTENDline);  }
				realINLINEct++;
				mockCT++;

			} // for curSTR
			FileUtils.writeLines(SFCALtempONE, newLINEARRY, LFEED, true);	

		}   catch (IOException e)  { 
			e.printStackTrace();	 
		}  // catch
	}	/// end of method

	
	//----new Method ===============================================================//
	static String createDTEND(String utline) {
		String partialEND ="";
		String newDTENDstr = "";  
		partialEND = utline.substring(8,22) + "5Z";
		newDTENDstr ="DTEND:" + partialEND;					
		verboseOut("DTEND: new line is " + newDTENDstr);
		return newDTENDstr;	
	}
	
	static String fixDTSTART(String uline) {
		String newstr = "";  		 
		String partialEND = uline.substring(8,22) + "5Z";
		newstr ="DTSTART:" + partialEND;					
		verboseOut("DTSTART: new line is " + newstr);
		return newstr;
	}
	
//----new Method ===============================================================//
	
	public static void delFiles(String nf) {
		File f1 = new File(nf);
		if ( f1.exists() ) {
			f1.delete();  // delete the inFileName we made last time
		}
	}

	
	//----new Method ===============================================================//
	protected static void mySleep(int timewait) {
		try {
			Thread.sleep(timewait * 1000);	//sleep is in milliseconds
		} catch (Exception e) {
			out.println(e);
		} 
	  } // mySleep
	
	
	//----new Method ===============================================================//
	static String checkForSignQuote(String checkLine) {
		 
		if (checkLine.contains( "\uFFFD"))  {
			out.println("!!!---            ---FOUND WEIRD CHAR in " + checkLine);	
			String newStringy = checkLine.replace( "\uFFFD", " ");  
			return newStringy;
		}
		else { return checkLine;
			}
		}

	
	//----new Method ===============================================================//
	static String checkForSigns(String origLine, String theVal, String theRep) {
		String theFixedLine = "";
		verboseOut("inside checkForSigns checking val rep: "+theVal + theRep);		
		if (origLine.contains(theVal))  {
			theFixedLine = origLine.replace( theVal, theRep);  
			out.println("---------FOUND sign CHAR ------------------The fixed line: " + theFixedLine);
			return theFixedLine;
		}
		else return origLine;	
	}

		
	//----new Method ===============================================================//
	static String replaceSigns(String theInputStr) {
		String answerst =theInputStr;
		verboseOut("inside replaceSigns");		
		HashMap <String, String> theHashmap = makemyhash();
		
		String tsign = theInputStr.substring(23,25);
		String theMoon = theInputStr.substring(8,19);
		if ((theMoon.contains("New Moon")) || (theMoon.contains("Full Moon")) ) {
			answerst = theInputStr.replace(tsign, theHashmap.get(tsign));
		}
		else {
			for (String key : theHashmap.keySet()) {       // check for other possibilities
			   answerst = checkForSigns(theInputStr, key, theHashmap.get(key));
		}   
			}
		verboseOut("val of answerst is: " + answerst);
		return answerst;
	}	

	
	//----new Method ===============================================================//
	static HashMap<String, String>  makemyhash() {
		HashMap <String, String> myHashmap = new HashMap<String, String>();
		myHashmap.put("Cn", "Cancer ");
		myHashmap.put("Ar", "Aries ");
		myHashmap.put("Ta", "Taurus ");
		myHashmap.put("Sg", "Sagittarius ");
		myHashmap.put("Ge", "Gemini ");
		myHashmap.put("Le", "Leo ");
		myHashmap.put("Vi", "Virgo ");
		myHashmap.put("Li", "Libra ");
		myHashmap.put("Sc", "Scorpio ");
		myHashmap.put("Cp", "Capricorn ");
		myHashmap.put("Aq", "Aquarius ");
		myHashmap.put("Pi", "Pisces ");
		return myHashmap;
	}
	
}  // end of class



 	