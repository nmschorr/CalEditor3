package com.nmschorr.SFCAL_standard;


import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang.StringUtils;


import org.apache.commons.lang.StringUtils;

import static java.lang.System.out;
//import static com.nmschorr.SFCAL_editor.SFCALeditor.verboseOut;
import static com.nmschorr.SFCAL_standard.SFCALstandard.*;

	
public class SFCALstandardutil {
	static final String newfront  =  "DTEND:";
	
	
	// new method // --------------------------------------------------------------	 		
	static void generalStringFixing(String origFILEnm, String tmpFILEnmONE ) {   
		List<String> nwARRY  =  new ArrayList<String>();
		File origFILE = new File(origFILEnm);
		File SFCALtempONE  =  new File(tmpFILEnmONE);
		CharSequence SUMstr = "SUMMARY:Tr-Tr";
		String cLINEtwo = "";
		String DEStr = "DESCRIPTION";
		String theDTENDline="";
		boolean tempboo = false;

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
				cLINE  =   cLINEtwo;
				System.out.println("    char string is:         " + cLINE);
		// the ifs start here
		// begin the IFS			
				if ( cLINE.contains(SUMstr)) {  /// if TR-TR only lines
					cLINEtwo = fixSUMMARYsigns(cLINE, false) ;
					nwARRY.add(cLINEtwo);
				}										
				else if ( cLINE.contains(DEStr) || cLINE.startsWith(" "))   {  /// if TR-TR only lines
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
					else {
						System.out.println("   writing ORIGINAL string to file         " + cLINE);
						nwARRY.add(cLINE );
					}
				lineCOUNT++;
				//if (lineCOUNT == 1899)
				//	tempboo = true;
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
			else if (tempSub.equals(charD))  { /// if TR-TR only lines
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
					//	( theLocLine.contains("TRANSPARENT")) 
						///     ||
					//	( theLocLine.contains("DTEND")) || 
					//	( theLocLine.contains("VCALENDAR")) || 
					//	( theLocLine.contains("VEVENT")) || 
					//	( theLocLine.contains("DTSTART")) || 
					//	( theLocLine.contains("PRODID:")) || 
					//	( theLocLine.contains("VERSION:")) || 
					//	( theLocLine.contains("METHOD:")) || ( theLocLine.contains("UID")) 
					//	|| ( theLocLine.contains("CATEGORIES")) || ( theLocLine.contains("DTSTAMP")) 
						)
				{ 
				 KG =false; 
				}
			}
			//System.out.println ("val of KG is :" + KG);
			return KG;
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



 	