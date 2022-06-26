package com.nmschorr.SFCALpersonal;


import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import static com.nmschorr.SFCALpersonal.SCALperUtils;
import static java.lang.System.out;

	
public class SCALpersMain {
	static final String newfront  =  "DTEND:";
	
	public static void main(String[] args) {	
		String indirMAIN = getindir();
		String outDIR = getoutdir();
		String outDIRTMP = outDIR + "\\tempfiles";
		String[] arryOfInFiles = getflist(indirMAIN);	// create a list of names of those files	
		int fileInDirCNT=0;
		
		int arraysize = arryOfInFiles.length;

		while (fileInDirCNT < arraysize) {  
			String infileNM= arryOfInFiles[fileInDirCNT];
			String inFILEstr = indirMAIN +"\\" + infileNM;
			   
			out.println("-- starting over in main LOOP# " + fileInDirCNT+1 +" filename is: " + infileNM);
			
			String finFILEnmWdir = mkDateFileNM(inFILEstr, infileNM, outDIR);
			delFiles(finFILEnmWdir);  // delete the inFileName we made last time
			 
			String tOUTone = getTMPnmWdir(outDIRTMP,"-one");
			String tOUTtwo = getTMPnmWdir(outDIRTMP,"-two");;
				
			generalStringFixing( inFILEstr, tOUTone);
			
			SCALperUtils.sectionTaskThree(tOUTone, tOUTtwo, finFILEnmWdir);
			
			out.println("- datefilename is: " + finFILEnmWdir+"--------End of Loop------------NEW filename is: "+finFILEnmWdir);		
			
			fileInDirCNT++;		
		}			
		out.println("Finished Program");
	}

	// new method: ----------------------------------------------------------------	
	static String getindir() {  // 1 for name, 2 for file
		String iDIRNM = "C:\\tmp\\PERSONAL\\1solarf_out_personal";
		return iDIRNM;
		}
	 
	// new method: ----------------------------------------------------------------	
	static String getoutdir() {  // 1 for name, 2 for file
		String oDIRNM =  "C:\\tmp\\PERSONAL\\2javaout_personal";
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
		String newTempStr1 = fixmeSTR.replace("DESCRIPTION:The ","DESCRIPTION:" );
		newTempStr = newTempStr1.replace(" Radix "," " );

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
			String xxx = lastthird;
			out.println("value of xxx:  " + xxx);

			// lastthird = tstring.substring(22,25);
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



 	