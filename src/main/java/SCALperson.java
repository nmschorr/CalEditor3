
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import static java.util.Map.entry; 

public class SCALperson {
	public static final String BKSLSH = "\\";
	protected static int G_VERBOSE = 0;
	protected static final String newfront = "DTEND:";
	public static final String EMPTYSTR = "";
	public static final char LF = '\n';
	public static final char CR = '\r';
	protected static final Long SYSTIME = currentTimeMillis();
	protected static final String UNIXTSTAMP = SYSTIME.toString().substring(3, 9);
	public static final String TOPDIR = "C:\\SFOUT";  // WHERE SF dumps files
	public static final String START_DIR = TOPDIR + "\\START";  // WHERE SF dumps files
	public static final char DASH = '-';
	public static int outLineCt = 0;
	protected static final String SPC = " ";

	public static final CharSequence SUMstr = "SUMMARY:Tr-Na";
	public static final String DESC = "DESCRIPTION";
	public static final String DTSTART = "DTSTART";
	public static final String ICSEXT =  ".ics";
	public static final String ENTERS =  "Enters";
	public static final String DTEND = "DTEND:";
	
	protected static final String LINE_FEED = System.getProperty("line.separator");

	protected static final Map<String, String> replaceEntryMap = Map.ofEntries(entry("Transiting ", EMPTYSTR), 
		entry("Conjunction","Conjunct"), entry("Opposition","Opposite"), entry("Entering", ENTERS ),
		entry("DESCRIPTION:The ","DESCRIPTION:" ), entry(" Radix "," " ));	
	
	protected static final List<String> DROPPHRASES = Arrays.asList( "Moon Sextile",
        "Moon Trine", "Moon Opposite", "Moon Square", "Moon Quincunx", "Full", "New Moon", 
        "Eclipse", "Partial", "Quarter ", "Lunar ", "Hybrid ", "Solar ", "Total ", "Annular ");

	protected static final Map<String, String> makeSpellEntries = Map.ofEntries(entry("Stabilise","Stabilize"), 	
		entry("Socialise","Socialize"), entry("Entering", ENTERS), entry("organised","organized"), 
		entry("excelent","excellent"), entry("realise","realize"), entry("spiritualilty","spirituality"), entry("wilfull","willful"), 
		entry("possibiities","possibilities"), entry("fantasise","fantasize"), entry("behaviour","behavior"), 
		entry("Aint","Ain't"),
		entry("Strategize","possibilities"),
		entry("Neighbourhood","Neighborhood"),
		entry("recognise","recognise"),
		entry("Organise","Organise"),
		entry("socialise","socialise"),
		entry("crystallised","crystalized"),		
		entry("eadership","Leadership"),		
		entry("self-fulfilment","self-fullfilment"),		
		entry("wilful","willful"),		
		entry("centre","center"),	
		entry("prioritise","prioritize")		
//		entry("Aint","Ain\\'t")		
//		entry("xx","xx"),		
//		entry("xx","xx")	
				);
	protected static final HashMap <String, String> makeSpellhm =  new HashMap<>(makeSpellEntries);

	protected static final Map<String, String> signsEntryMap = Map.ofEntries(
	    entry("Cn", "Cancer "), entry("Ar", "Aries "), entry("Ta", "Taurus "), entry("Sg", "Sagittarius "),
		entry("Ge", "Gemini "), entry("Le", "Leo "), entry("Vi", "Virgo "), entry("Li", "Libra "),
		entry("Sc", "Scorpio "), entry("Cp", "Capricorn "), entry("Aq", "Aquarius "), entry("Pi", "Pisces " ));	
	
	protected static final Map<String, String> planSignsEM = Map.ofEntries(
		 entry("Mon", "Moon"), entry("Ari", "Aries"), entry("Tau", "Taurus"), entry("Gem", "Gemini"), 
		 entry("Can", "Cancer"),  entry("Leo", "Leo"), entry("Vir", "Virgo"), entry("Lib", "Libra"), entry("Sco", "Scorpio"), 
		 entry("Sag", "Sagittarius"),   entry("Cap", "Capricorn"), entry("Aqu", "Aquarius"),  entry("Pis", "Pisces"),
		 entry("Cnj", "Conjunct"), entry("Tri", "Trine"), entry("Opp", "Opposite"), entry("Sqr", "Square"),
		 entry("Sxt", "Sextile"), entry("Qnx", "Quincunx"), entry("Sun", "Sun"), entry("Mer", "Mercury"), 
		 entry("Ven", "Venus"),  entry("Mar", "Mars"), entry("Jup", "Jupiter"), entry("Sat", "Saturn"), 
		 entry("Nep", "Neptune"), entry("Ura", "Uranus"), entry("Plu", "Pluto")	);
	protected static final HashMap <String, String> planSignsAsp = new HashMap<>(planSignsEM);


	public static void main(String[] args) {
		String[] arryOfInFiles = getflist(START_DIR);	// create a list of names of those files
		String firstFileNme = arryOfInFiles[0];
		int strLenBeforeExt = firstFileNme.length()- 4;
		int indxUnderln = firstFileNme.indexOf("_");

		String sfOrigNmePlsTS = firstFileNme.substring(indxUnderln+1, strLenBeforeExt);
		out.println("string len: " + sfOrigNmePlsTS);
		final String sfOrigNmeWJavaTS = sfOrigNmePlsTS + DASH + UNIXTSTAMP;

		final String PersonFpath = BKSLSH  + sfOrigNmeWJavaTS ;
		final String DONEDIR = TOPDIR + "\\USED" + PersonFpath;  // OLD FILES done
		final String WORKDIR = TOPDIR + "\\WORK" + PersonFpath;  // OLD FILES done
		final String outDIRTMP = WORKDIR + "\\tempfiles";
		final String READYDIR = TOPDIR + "\\READY4PY" + PersonFpath;  
		makeDir(DONEDIR);  //READ the list of files in sfcalfiles/vds dir
		
		for (int fileInDirCNT = 0; fileInDirCNT < arryOfInFiles.length; fileInDirCNT++) {
			String infileNM = arryOfInFiles[fileInDirCNT];
			String inFILEstr = START_DIR + BKSLSH + infileNM;

			out.println("-- starting over in main LOOP# " + fileInDirCNT+1 +" filename is: " + infileNM);

			String finFILEnmWdir = mkDateFileNM(inFILEstr, infileNM, WORKDIR);
			String tOUTone = getTMPnmWdir(outDIRTMP,"-one", PersonFpath);
			generalStringFixing(inFILEstr, tOUTone);
			sectionTaskThree(tOUTone, finFILEnmWdir);
			out.println("- datefilename is: " + finFILEnmWdir+"----End of Loop---------NEW filename is: " + finFILEnmWdir);
		}
		copyDoneStartFiles(START_DIR, DONEDIR);
		moveReadyFiles(WORKDIR, READYDIR);
		out.println("Finished Program for " + PersonFpath);
	}

	public static void copyDoneStartFiles(String stdir, String doneDirectory) {
		String[] arryFiles = getflist(stdir);	// create a list of names of those files
		File doneDirPers = new File(doneDirectory);
		
		for (int fileCNT = 0; fileCNT < arryFiles.length; fileCNT++ ) {
			String inFILEstr = stdir + BKSLSH + arryFiles[fileCNT];
			File oldfilef = new File(inFILEstr);
			try {
				FileUtils.copyFileToDirectory(oldfilef, doneDirPers, true);     
			} catch (IOException e) {
				e.printStackTrace(); }
		}
		out.println("Finished copyDoneFiles");
	}
	
	public static void moveReadyFiles(String wkdir, String readyDirectory) {
		File readyDir = new File(readyDirectory);  // new dir
		String[] arryFilez = getflist(wkdir);	// create a list of names of those files		
		for (int fileCNT = 0; fileCNT < arryFilez.length; fileCNT++) {
			String inFILEstr = wkdir + BKSLSH + arryFilez[fileCNT];
			File oldfil = new File(inFILEstr);
			
			if (!oldfil.isDirectory()) {
				try {
					FileUtils.moveToDirectory(oldfil, readyDir, true);     
				} catch (IOException e) {
					e.printStackTrace();
				}			}
		}
		out.println("Finished copyReadyFiles");
	}
	
	public static void makeDir(String fname) {
		File newDir = new File(fname);  //READ the list of files in sfcalfiles/vds dir
		try {
			FileUtils.forceMkdir(newDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String chmp(String str) {
		if (str.length() == 0) {
			return str;
		}

		if (str.length() == 1) {
			if (str.charAt(0) == CR || str.charAt(0) == LF) {
				return EMPTYSTR;
			}
			return str;
		}

		int lastIdx = str.length() - 1;
		char last = str.charAt(lastIdx);

		if (last == LF) {
			if (str.charAt(lastIdx - 1) == CR) {
				lastIdx--;
			}
		} else if (last != CR) {
			lastIdx++;
		}
		return str.substring(0, lastIdx);
	}

	// new method: ----------------------------------------------------------------
	static String getTMPnmWdir(String tnm, String myIn, String UNXTS) {  // 1 for name, 2 for file
		return tnm + "\\SCALtmp" + UNXTS + myIn + ICSEXT; }

	static String[] getflist(String dnm) {  // 1 for name, 2 for file
		return new File(dnm).list();	}			// create a list of names of those files

	public static void verboseOut(String theoutline) {
		if (G_VERBOSE==1) { out.println(theoutline);   }}
		

	static String newrepl(String localSTR) {	
		 for (Map.Entry<String, String> entryPair : makeSpellEntries.entrySet()){
		    String oldVal = entryPair.getKey();
		    String newVal = entryPair.getValue();    
			if (localSTR.contains(oldVal)) {
				localSTR = localSTR.replace(oldVal, newVal);
			}
		}  
		return localSTR;     
		}

	static String continueReplacing(String fixmeSTR) {
		 for (Entry<String, String> entryPair : replaceEntryMap.entrySet()){
			    String oldVal = entryPair.getKey().toString();
			    String newVal = entryPair.getValue().toString();    
				if (fixmeSTR.contains(oldVal)) {
					fixmeSTR = fixmeSTR.replace(oldVal, newVal);     }
			}  
			return fixmeSTR;     
			}
		
	static String fixSUMMARYsigns(String oldstrgFirst) {	
		if (oldstrgFirst.substring(8).equals(SPC))
			oldstrgFirst = oldstrgFirst.replace("SUMMARY: ", "SUMMARY:");
		String tstring = "SUMMARY:";
		StringBuilder newbuf = new StringBuilder(tstring);
		
		int startFirst = 14;
		int endFirst = 17;		
		int startSecond = 18;
		int endSecond = 21;
		int startThird = 22;
		int endThird = 25;				
		String firstPart = oldstrgFirst.substring(startFirst, endFirst);
		String secondPart = oldstrgFirst.substring(startSecond, endSecond);
		String thirdPart = oldstrgFirst.substring(startThird, endThird);
		if (planSignsAsp.containsKey(firstPart)) 
			newbuf.append(( planSignsAsp.get(firstPart)).concat(SPC));
		if (planSignsAsp.containsKey(secondPart)) 
			newbuf.append(( planSignsAsp.get(secondPart)).concat(SPC));
		if (planSignsAsp.containsKey(thirdPart)) 
			newbuf.append(( planSignsAsp.get(thirdPart)));
		String theBuf = newbuf.toString();
		out.println(theBuf);
		return theBuf;
	}

	static String mkDateFileNM(String oldFileWDir, String oldname, String newfiledir) {
		List<String> oldfileAR = new ArrayList<>();
		String newDateNM = EMPTYSTR;
		int whileCT = 0; int tStart = 0; int tEnd = 0;
		String oldNameNoExt = oldname.substring(0, oldname.indexOf("."));
		try {
			oldfileAR = FileUtils.readLines(new File(oldFileWDir));  //READ the list of files in sfcalfiles/vds dir

			while ( whileCT < 15) {
				String tmpSTR = oldfileAR.get(whileCT);
				if ( tmpSTR.contains(DTSTART)) {
					tStart = tmpSTR.indexOf(":")+1;
					tEnd = tStart + 8;
					String newDateStr = tmpSTR.substring(tStart, tEnd);
					String newOldName = oldNameNoExt + DASH + newDateStr + ICSEXT;
					newDateNM = newfiledir + BKSLSH + newOldName;
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
		List<String> nwARRY = new ArrayList<>();
		File origFILE = new File(origFILEnm);
		File SCALtempONE = new File(tmpFILEnmONE);
		try {
			List<String> origFILEARRY = FileUtils.readLines(origFILE);
			out.println("orig file size:  " +  origFILEARRY.size() + " ----------------------------------%%%%%%%##### total lines: " +  origFILEARRY.size());
			// get ics header lines in 1st-first four header lines of ics inFileName
			String cLINE;
			String[] mynew;
			
			// for each line in file:
			int linePosition = 4;
			while (linePosition < origFILEARRY.size()) {
				verboseOut("myLINEct:  " + linePosition);
				outLineCt = linePosition;
				cLINE = origFILEARRY.get(linePosition);
				String cLINEtwo = chmp(cLINE);  // chomp is removing the Z
				cLINE = chkForWeirdChar(cLINEtwo);
				StringBuilder mainDescLineAllsb = new StringBuilder();

				if ( cLINE.contains(SUMstr)) {  /// if TR-Na only lines
					nwARRY.add(fixSUMMARYsigns(cLINE));
					out.println(cLINE);
				}
				
				else if ( cLINE.contains(DESC))   {  /// if TR-NA only lines
					String mainDescLineAll = fixDESCRIPTION_line(cLINE);
					mainDescLineAllsb.append(mainDescLineAll);
					linePosition++;
					int x = 1;
					while ( x < 9) {
						String possContinueText = origFILEARRY.get(linePosition);
						 
						if (possContinueText.startsWith(" ")) {
							String possContinueTextFixed = fixDESCRIPTION_line(possContinueText) ;
							mainDescLineAllsb.append(possContinueTextFixed);
							linePosition++;
							x++;
							}
						else {
							x = 10;
							}
					}
					nwARRY.add(mainDescLineAllsb.toString());
					out.println("!!!!------------------JUST ADDED: " + mainDescLineAllsb.toString());
					} // for
							
				 
				else if ( ( cLINE.contains("DTSTAR") ) && (!cLINE.contains("VALUE")) )  {
					nwARRY.add(cLINE );
					out.println("adding: " + cLINE);
					nwARRY.add(chkAddDTEND(cLINE) );   //skip these; they are moon for the day
					out.println("adding: " + cLINE);
						}
				else {
					verboseOut("   writing ORIGINAL string to file         " + cLINE);
					nwARRY.add(cLINE );
					out.println("adding: " + cLINE);
				}
			
			linePosition++;
			cLINEtwo = EMPTYSTR;	
			
			}  // while

			verboseOut("Writing to file: " + SCALtempONE.getName());
			FileUtils.writeLines(SCALtempONE, nwARRY);
		}  // try
		catch (IOException e)  {
			e.printStackTrace();   }
		}	// end of method

	static String chkAddDTEND (String theLine) {
		if (( theLine.contains("DTSTAR")) && ( !theLine.contains("VALUE")) ) { //moon for the  day
				String newback = theLine.substring(8,23) + "Z";
				verboseOut("   !! inside chkAddDTEND --   @@@@@  the line is  " + theLine);
				return newfront + newback;
			}
		return theLine;
	}

	static String myREPLACE(String bigstr, String oldStr, String newStr) {		
		if ( bigstr.contains(oldStr) ) {
			return bigstr.replace(oldStr, newStr);
		}
		return bigstr;
	}

	protected static void mySleep(long timewait) {
		try {
			Thread.sleep(timewait * 1000);	//sleep is in milliseconds
		} catch (Exception e) {
			out.println(e);
		}
	} // mySleep


	static String chkForWeirdChar(String checkLine) {
		try {
			if (checkLine.contains( "\uFFFD"))  {
				String newStringy = checkLine.replace( "\uFFFD", " ");
				verboseOut("FOUND WEIRD CHAR -----!!!!  !!!  " + checkLine + "The fixed line: " + newStringy);
				return newStringy;
			}
		} catch (Exception e) {
			out.println(e);
		}
		return checkLine;
	}

	static String replaceSigns(String locStr) {
		String answerst = locStr;
		for (Entry entryPair2 : signsEntryMap.entrySet()) {       // check for other possibilities
		    String key = entryPair2.getKey().toString();
		    String val = entryPair2.getValue().toString(); 
		    answerst = checkForSigns(locStr, key, val);
		}
		verboseOut("val of answerst is: " + answerst);
		return answerst;
	}

	static String checkForSigns(String origLine, String theVal, String theRep) {
		verboseOut("inside checkForSigns checking val rep: "+theVal + theRep);
		if (origLine.contains(theVal))  
			return origLine.replace( theVal, theRep);
		else return origLine;
	}

	static void sectionTaskThree(String tFILEin, String finFILE) {   // this part was done by perl script
		Map<String, String> jmap = new HashMap<>();

		
		int totInfilesMinusNine = 0;
		int locLineCount = 4;  // start at 5th line
		File theREADING_FROM_TMP_FILE = new File(tFILEin);
		File finalFILE = new File(finFILE);
		verboseOut("tFILEin: " + tFILEin + " finFILE: " + finFILE);
		ArrayList<String> lastFILE_ARRAY = new ArrayList<String>();
		ArrayList<String> jmap_list_arry = new ArrayList<String>();

		try {
			List<String> tempFILE_ARRAY = FileUtils.readLines(theREADING_FROM_TMP_FILE);
			int totInFileLines = tempFILE_ARRAY.size();
			// get ics header lines in 1st-first four header lines of ics inFileName

			for (int i = 0; i < 4; i++)	{
				lastFILE_ARRAY.add(tempFILE_ARRAY.get(i));
			}
			totInfilesMinusNine = totInFileLines-9;  //  
			verboseOut("!!! totInfilesMinusNine: " + totInfilesMinusNine );
			while ( locLineCount < totInfilesMinusNine )   {                 // while there are still lines left in array // starting on 5th line, load
				ArrayList<String> tinySectionList = new ArrayList<>();
				// first load sections of 10x lines each into smaller arrays  then check each section for voids etc  then correct

				for (int tc=0; tc < 17; tc++) {         //tiny while
					String theString = tempFILE_ARRAY.get(locLineCount);  //get one string
					boolean checkforend = theString.startsWith("END:VEVENT", 0);
					if (checkforend) {
						tinySectionList.add(theString);
						locLineCount++;
						break;
						}
					else {
						if (theString.contains("Stationary") &&  (theString.contains(DESC) )	)
							theString = theString + LINE_FEED;
						tinySectionList.add(theString);
						locLineCount++;
					}
				}  //for

				boolean checkToKeep = true;
				if (tinySectionList.size() > 6) 
					checkToKeep = checkSUMMARYforToss(tinySectionList);	 // true is keep and write 

				if (checkToKeep) {   // IF 	check for toss comes back TRUE, then write this section
					lastFILE_ARRAY.addAll(tinySectionList);
					
					int sizea = tinySectionList.size();
					
					if (sizea > 0) {
						String startstr = tinySectionList.get(0);
						out.println("startstr: " + startstr);
					}
//					String endstr = tinySectionList.get(2);
//					String summarystr = tinySectionList.get(2);
//					String descriptionstr = tinySectionList.get(2);
//
//					jmap.put("start",startstr);
//					jmap.put("end", endstr);
//					jmap.put("summary", summarystr);		
//					jmap.put("description", descriptionstr);
					
					totInFileLines = 0;
				}

			} 						//  // while locLineCount
			lastFILE_ARRAY.add("END:VCALENDAR"+LINE_FEED);
			List<Integer> cntLONG = new ArrayList<>();
			int newARRAYSIZE = lastFILE_ARRAY.size();
			int curLINEct = 0;
			boolean yesDESC = false;

			while ( curLINEct < newARRAYSIZE) {    // while we're still in the file
				yesDESC = false;
				String longstr = EMPTYSTR;				
				String tline = lastFILE_ARRAY.get(curLINEct);

				if (tline.contains(DESC)) {
					yesDESC = true;
					cntLONG.clear();
					cntLONG.add(curLINEct);
					longstr = concatDESC (tline, lastFILE_ARRAY, curLINEct, cntLONG);  
				}	// if DESCRIPTION

				if (yesDESC) {
					addmyLines (cntLONG, lastFILE_ARRAY, longstr);
				}				
				cntLONG.clear();
				newARRAYSIZE = lastFILE_ARRAY.size();    
				curLINEct++;  //move the line counter up to the next group
			}  // while

			FileUtils.writeLines(finalFILE, lastFILE_ARRAY, true);
			SCALperson.mySleep(1);
			FileUtils.waitFor(finalFILE, 1);
		}  					  
		catch (IOException e) {  	e.printStackTrace();	 }	// catch
	} 			 // end of method

	// new method: ----------------------------------------------------------------	
	static String concatDESC (String mainDescString, List<String> fileARY, int cLineCT, List<Integer> cLONG) {
//		int fileLinePosition = cLineCT;
////		for (int myCounter = 1; myCounter < 4; myCounter++) {
//
//		for (int myCounter = 1; myCounter < 2; myCounter++) {
//			fileLinePosition++;
//			String tempLine = fileARY.get(fileLinePosition);
//			if (tempLine.startsWith(" ")) {
//				mainDescString = mainDescString.concat(tempLine);
//			myCounter++;
//		}}
		verboseOut("This Description: " + mainDescString);
		return mainDescString;
	}

	static String fixDESCRIPTION_line(String inSTRING) {
		CharSequence badLINEFchars = "\\n";
		String newstr = EMPTYSTR;  			 

		String tString = inSTRING.replaceAll("%0A", EMPTYSTR);  // get rid of CRs  - \n
		if (tString.contains(badLINEFchars))    // for newline only
			newstr = tString.replace((String)badLINEFchars, " - ");
		else newstr = tString;
		tString = continueReplacing(newstr);
		if (tString.startsWith(" "))  {	 // spelling errors in extra lines of DESCRIPTION
			tString = newrepl(tString);
		}
		return tString;     }
	
	
	// new method: ----------------------------------------------------------------	
	static List<String> addmyLines (List<Integer> cntLONG, List<String> farray, String longstrg) {
		for (int i=0; i < cntLONG.size(); i++) {     // should be around 3	
			int ttInt = cntLONG.get(0);
			farray.remove(ttInt);  // remove little strings
		}
		farray.add(cntLONG.get(0), longstrg);  // add new long string back
		return farray;
	} 	

	// new method: ----------------------------------------------------------------
	static boolean checkSUMMARYforToss(List<String> tinyList) {
		String summaryLine = tinyList.get(5);  // checking the 6th line : SUMMARY
		verboseOut("   starting over in checkForTossouts. \nThe string is:  " + summaryLine );
		
		for (int indx=0; indx < DROPPHRASES.size(); indx++) {
			if (summaryLine.contains(DROPPHRASES.get(indx))) {
				verboseOut("========== !!!!! found a tosser: tossing: "+ summaryLine);	
				return false;
				}
		}
		return true; 
	} // method end
}  // class
