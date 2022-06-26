package com.nmschorr.SFCAL_personal;


import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;


import org.apache.commons.lang.StringUtils;

import static java.lang.System.out;
import static com.nmschorr.SFCAL_personal.SFCALpersonal.*;

	
public class SFCALPersonalParent {
	static final String newfront  =  "DTEND:";
	
		static void generalStringFixing(String origFILEnm, String tmpFILEnmONE ) {   
		List<String> nwARRY  =  new ArrayList<String>();
		File origFILE = new File(origFILEnm);
		File SFCALtempONE  =  new File(tmpFILEnmONE);
		CharSequence SUMstr = "SUMMARY:Tr-Na";
		String cLINEtwo = "";
		String DEStr = "DESCRIPTION";
		String theDTENDline="";

		G_VERBOSE = 1;
		
		try {
			List<String> origFILEARRY  =   FileUtils.readLines(origFILE);
			int arraySIZE  =  origFILEARRY.size();
			System.out.println("orig file size:  " +  arraySIZE   );			
			System.out.println("----------------------------------%%%%%%%##### total lines: " +  origFILEARRY.size());
			// get ics header lines in 1st-first four header lines of ics inFileName
			int lineCOUNT =0;
			String cLINE;

			// for each line in file:
			while (lineCOUNT < arraySIZE) {
				System.out.println("myLINEct:  " + lineCOUNT);
				cLINE = origFILEARRY.get(lineCOUNT);
				cLINEtwo  =   StringUtils.chomp(cLINE);  // chomp is removing the Z
				cLINE = chkForWeirdChar(cLINEtwo);
				System.out.println("    char string is:         " + cLINE);
					// the if's start here
					// begin the IFS			
				if ( cLINE.contains(SUMstr)) {  /// if TR-Na only lines
					cLINEtwo = fixSUMMARYsigns(cLINE, false) ;
					nwARRY.add(cLINEtwo);
				}			
				else if ( cLINE.contains("Full Moon") || cLINE.contains("New Moon")) {
					cLINEtwo = replaceSigns(cLINE);
					cLINE = cLINEtwo;
					nwARRY.add(cLINE );
				} 
				
				else if ( cLINE.contains(DEStr) || cLINE.startsWith(" "))   {  /// if TR-NA only lines
						cLINEtwo = fixDESCRIPTION_line(cLINE) ;
						nwARRY.add(cLINEtwo);
					}										
					else if (cLINE.startsWith("SUMMARY:Tr "))   { // direct or retrograde
						cLINEtwo= fixDirRetro(cLINE);
						cLINEtwo = fixSUMMARYsigns(cLINEtwo, true) ;
						nwARRY.add(cLINEtwo );
					}  // SUMMARY:TR 	

					
					else if ( (cLINE.contains(";VALUE")) && ( cLINE.contains("DT")) )  { // moon for the day
						nwARRY.add(cLINE );
					}  // SUMMARY:TR 
				
					else if ( cLINE.contains("DTSTAR") ) {
						if (!cLINE.contains("VALUE")) { //skip these; they are moon for the day
							theDTENDline = chkAddDTEND(cLINE);
							nwARRY.add(cLINE );
							nwARRY.add(theDTENDline );
						}
					}
					else if (cLINE.contains("Moon goes void")) {
						cLINEtwo = "SUMMARY:Moon void of course";
						int newSIZE  =  nwARRY.size();

						int back_three_count = newSIZE - 5;
						int back_two_count = newSIZE - 4;
						System.out.println("------here's back three----");
						String back_3_str = nwARRY.get(back_three_count);
						System.out.println("old string: " + back_3_str);
						String newDTSTART = fixDTSTART(back_3_str);
						String new_DTEND_line = stdUtilCreateDTEND(back_3_str);
						
						nwARRY.set(back_three_count, newDTSTART);
						nwARRY.set(back_two_count, new_DTEND_line);
						
						nwARRY.add(cLINEtwo );
						lineCOUNT++;
						}

					
					else {
						System.out.println("   writing ORIGINAL string to file         " + cLINE);
						nwARRY.add(cLINE );
					}
				lineCOUNT++;
				cLINEtwo ="";
			}  // while lines in file arrray
			System.out.println("Writing to file: " + SFCALtempONE.getName());
			FileUtils.writeLines(SFCALtempONE, nwARRY);	
			System.out.println("first end");
		}  // try
				catch (IOException e)  { 
					e.printStackTrace();	 
					}  // catch

	}	// end of method

	static String fixDTSTART(String uline) {
		String newstr = "";  		 
		String partialEND = uline.substring(8,22) + "1Z";
		newstr ="DTSTART:" + partialEND;					
		verboseOut("DTSTART: new line is " + newstr);
		return newstr;
	}

	static String stdUtilCreateDTEND(String utline) {
		String partialEND ="";
		String newDTENDstr = "";  
		partialEND = utline.substring(8,22) + "1Z";
		newDTENDstr ="DTEND:" + partialEND;					
		verboseOut("DTEND: new line is " + newDTENDstr);
		return newDTENDstr;	
	}
	
	static String fixDirRetro(String retroString) {
		String charD = " D";  
		String charR = " R";  // MUST have a space first
		String retStringTwo="";
		String tempSub="";
		
		if (retroString.startsWith("SUMMARY:Tr "))   { 
			retStringTwo = retroString.replace("Tr ", "");

			int cStart = retStringTwo.length()-3;  // a space & there's a line ending too
			int cEnd = retStringTwo.length()-1;
			tempSub = retStringTwo.substring(cStart,cEnd);  // get the last char

			if (tempSub.equals(charR))  {  
				retroString  = retStringTwo.replace(charR, " goes Retrograde");
			}
			else if (tempSub.equals(charD))  { /// if TR-NA only lines
				retroString  = retStringTwo.replace(charD, " goes Direct");
			}
		}
	return retroString;
}
	
	
	
	// new method // --------------------------------------------------------------	 	
	static String chkAddDTEND (String theLine) {
		String newDTEND = theLine;
		if ( theLine.contains("DTSTAR") )   {  // double check
			if  ( !theLine.contains("VALUE")) { //moon for the  day			
			String newback = theLine.substring(8,23) + "Z";
			out.println("                   !! inside chkAddDTEND -----                        @@@@@  the line is  " + theLine);
			newDTEND = newfront + newback;  					
			out.println("DTEND: new line is " + newDTEND);
		}
	}
	return newDTEND;
	}
	

	// new method // --------------------------------------------------------------	 	
static HashMap<String, String>  makeNewhash() {
		HashMap <String, String> localHash  =  new HashMap<String, String>();
		localHash.put("Mon", "Moon");
		
		localHash.put("Ari", "Aries");
		localHash.put("Tau", "Taurus");
		localHash.put("Gem", "Gemini");
		localHash.put("Can", "Cancer");
		localHash.put("Leo", "Leo");
		localHash.put("Vir", "Virgo");
		localHash.put("Lib", "Libra");
		localHash.put("Sco", "Scorpio");
		localHash.put("Sag", "Sagittarius");
		localHash.put("Cap", "Capricorn");
		localHash.put("Aqu", "Aquarius");
		localHash.put("Pis", "Pisces");

		localHash.put("Cnj", "Conjunct");
		localHash.put("Tri", "Trine");
		localHash.put("Opp", "Opposite");
		localHash.put("Sqr", "Square");
		localHash.put("Sxt", "Sextile");
		localHash.put("Qnx", "Quincunx");
		
		localHash.put("Sun", "Sun");
		localHash.put("Mer", "Mercury");
		localHash.put("Ven", "Venus");
		localHash.put("Mar", "Mars");
		localHash.put("Jup", "Jupiter");
		localHash.put("Sat", "Saturn");
		localHash.put("Nep", "Neptune");
		localHash.put("Ura", "Uranus");
		localHash.put("Plu", "Pluto");		
	return localHash;
	}

	
	// new method // --------------------------------------------------------------	 	
	static String myREPLACE(String bigstr, String oldStr, String newStr) {
		if ( bigstr.contains(oldStr) ) {
			bigstr.replace(oldStr, newStr);
		}		
		return bigstr;
	}
	
	// new method // --------------------------------------------------------------	 	
	public static void delFiles(String f2in) {
		File f1 = new File(f2in);
		if ( f1.exists() ) {
			f1.delete();  // delete the inFileName we made last time
		}
	}

	// new method // --------------------------------------------------------------	 	
	protected static void mySleep(int timewait) {
		try {
			Thread.sleep(timewait * 1000);	//sleep is in milliseconds
		} catch (Exception e) {
			System.out.println(e);
		} 
	  } // mySleep
	

	// new method // --------------------------------------------------------------	 	
	static String chkForWeirdChar(String checkLine) {
		 
		if (checkLine.contains( "\uFFFD"))  {
			System.out.println("!!!---            ---FOUND WEIRD CHAR -----!!!!  !!!  ");
			System.out.println(checkLine);	
			String newStringy  =  checkLine.replace( "\uFFFD", " ");  
			System.out.println("The fixed line: " + newStringy);
			return newStringy;
		}
		else { return checkLine;
			}
		}

	
	// new method // --------------------------------------------------------------	 	
	static boolean checkLINEfunction(String theLocLine) {
			boolean KG = true;
			if   ((theLocLine.length() > 0 ) )   {

				if   ((theLocLine.length() > 0 ) )   
					{ KG  =  true; } 
				else { KG = false; }

				if ( ( theLocLine.contains("THISISATESTONLY")) 
						)
				{ 
				 KG =false; 
				}
			}
			return KG;
		}
		
	//----new Method ===============================================================//
		static String replaceSigns(String theInputStr) {
			String answerst =theInputStr;
			String tsign = "";
			String aspace = "";
			String theMoon = "";
			boolean theProblem = false;
			verboseOut("inside replaceSigns");		
			HashMap <String, String> theHashmap = makemyhash();

			String ShortSpace = theInputStr.substring(21,22);
			if (ShortSpace.equals(" ")) {
				 theProblem = true;
			}
			
			if (theProblem == false) {     // continue as usual
				aspace = theInputStr.substring(23,24);
				if (aspace.equals(" ")) {
					 tsign = theInputStr.substring(24,26);
				}
					 else tsign = theInputStr.substring(23,25);
			}
			
			if (theProblem == true) {     // scoot everything over one space
			
				aspace = theInputStr.substring(22,23);
				if (aspace.equals(" ")) {
					 tsign = theInputStr.substring(23,25);
				}
					 else tsign = theInputStr.substring(22,24);
			}
					 
			theMoon = theInputStr.substring(8,19);
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

		
		// new method // --------------------------------------------------------------	 	
		static HashMap<String, String>  makemyhash() {
			HashMap <String, String> myHashmap  =  new HashMap<String, String>();
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
}



 	