/**
 * This class contains methods that remove extra calendar events from an ics calendar file.
 * @author Nancy M. Schorr
 * @version 1.1
 * 
 */
package com.nmschorr.SFCAL_standard;


import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
//import static com.nmschorr.SFCAL_editor.SFCALeditor.*;
import static java.lang.System.out;
	

public class SFCALvoidsutil {
	final static String LFEED = System.getProperty("line.separator");
	static int G_VERBOSE = 1;

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

					if ( utilLINE1.substring(0,6).equals("DTSTAR") )   {   // add DTEND line, chg  start line ending to 1Z to add 5 secs	 			
						newDTENDline = createDTEND(utilLINE1);
						utilLINE2 = voidsUtilfixDTSTART(utilLINE1);
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
		partialEND = utline.substring(8,22) + "1Z";
		newDTENDstr ="DTEND:" + partialEND;					
		verboseOut("DTEND: new line is " + newDTENDstr);
		return newDTENDstr;	
	}
	
	static String voidsUtilfixDTSTART(String uline) {
		String newstr = "";  		 
		String partialEND = uline.substring(8,22) + "1Z";
		newstr ="DTSTART:" + partialEND;					
		verboseOut("DTSTART: new line is " + newstr);
		return newstr;
	}
	
	// new method: ----------------------------------------------------------------	
		public static void verboseOut(String theoutline) {
			if (G_VERBOSE==1) {
				out.println(theoutline);
			}
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
	// new method: ----------------------------------------------------------------	
		static void sectionTask(String infORIGstr, String dFILE_OUT) {   // this part was done by perl script
			List<String> inARRAY = new ArrayList<String>();
			List<String> outARRAY = new ArrayList<String>();
			List<String> tinySectionList;
			File infileORIG = new File(infORIGstr); 
			File dateFILE_OUT = new File(dFILE_OUT); 
			boolean shouldKEEP = false;
			int tinyCounter =0;
			int totLines=0;
			int locLineCount=4;  // start at 5th line
			inARRAY.clear();
			List <String> moonsList = new ArrayList <String>();
			moonsList.add("New Moon");
			moonsList.add("Full Moon");
			moonsList.add("Eclipse");
			String lineOne = "";
			String lineTwo = "";
			String lineSix = "";

			try {
				inARRAY =  FileUtils.readLines(infileORIG);
				totLines = inARRAY.size();

				System.out.println("!!! INSIDE sectiontask. lines: " + totLines +" " 
						+ dateFILE_OUT.getName());
				// get ics header lines in 1st-first four header lines of ics inFileName

				for (int i = 0; i < 4; i++)	{
					outARRAY.add(inARRAY.get(i));
				}

				int goLines = totLines-2;
				while ( locLineCount < goLines )  
				{  // while there are still lines left in array // starting on 5th line, load
					tinyCounter = 0;
					tinySectionList = null;
					tinySectionList = new ArrayList<String>();

					// first load sections of 10x lines each into smaller arrarys
					// then check each section for voids etc  then correct

					//while (tinyCounter < 10) {         //tiny while
					while ((locLineCount < totLines ) && (tinyCounter < 10)) {         //tiny while

						String theString = inARRAY.get(locLineCount);  //get one string
						tinySectionList.add(theString);
						locLineCount++;
						tinyCounter++;
					}  // tiny while
					// }
					shouldKEEP = ckToKEEPvoids(tinySectionList);	 

					
					// Next - check tinySectionList for Full, New and Eclipse
					// and change the end of DTEND to "05"
					// if line 6 contains "Full Moon" or "New Moon" or Eclipse
					// then change last two chars of line 3 to 0Z

					lineOne = tinySectionList.get(1);
					lineTwo = tinySectionList.get(2);

					for (String excStr : moonsList) {
						if (tinySectionList.get(6).contains(excStr)) {
							tinySectionList.set(1, lineOne.replace("5Z", "0Z"));
							tinySectionList.set(2, lineTwo.replace("5Z", "0Z"));
						}	
					}		

					if (shouldKEEP == true) {   // IF 	checkfortoss comes back TRUE, then write this section
						outARRAY.addAll( tinySectionList);
					}
					tinyCounter=0;

				} //  // while locLineCount
					//			String lastLine = outARRAY.get( outARRAY.size()-1);  // the last line of the array
					//			if (lastLine.equals("")) {
					//				out.println("Removing last blank line of array.");
					//				outARRAY.remove(outARRAY.size()-1);
					//			}
				FileUtils.writeLines(dateFILE_OUT, outARRAY, true);	
				FileUtils.writeStringToFile(dateFILE_OUT, "END:VCALENDAR", true);
			 
				System.out.println("!!! INSIDE sectiontask. filename  - "+ dateFILE_OUT.getName());			
			}  // try  
			catch (IOException e) {  	e.printStackTrace();	 }	// catch
		}  // end

		
	// new method: ----------------------------------------------------------------
		static boolean ckToKEEPvoids(List<String> tinyList) {  // returns true to write
			String sl = tinyList.get(6);
			out.println("\n\n"+"               %%%%%%%%%%%%%%%%% starting over in ckToKEEP");
			out.println("The string is:  " + sl );

			if ( (sl.contains("SUMMARY")) && (sl.contains("Eclipse")) )
			{
				out.println("==========    ===== !!!!! reg method FOUND ECLIPSE!!! !!  !  ========== writing: "+ sl);		
				return true;  //keep
			}

			else if ( (sl.contains("void of")) || (sl.contains("SUMMARY:Full")) || 
					( sl.contains("SUMMARY:New Moon")) )     // we are removing the quarters
			{
				out.println("==========    ===== !!!!! reg method FOUND ! ========== writing: "+ sl);		
				return true; //keep
			}
			else  {
				return false;  // don't keep
			}
		} // method end			
			
}  // end of class



 	