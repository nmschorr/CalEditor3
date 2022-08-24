
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
	static int G_VERBOSE = 1;
	static final String newfront = "DTEND:";
	public static final String EMPTYSTR = "";
	public static final char LF = '\n';
	public static final char CR = '\r';
	protected static final Long SYSTIME = currentTimeMillis();
	protected static final String UNIXTSTAMP = SYSTIME.toString().substring(3, 9);
	public static final String TOPDIR = "C:\\SFOUT";  // WHERE SF dumps files
	public static final String START_DIR = TOPDIR + "\\START";  // WHERE SF dumps files
	public static final char DASH = '-';
	public static int outLineCt = 0;
	
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
        "Moon Trine", "Moon Opposite", "Moon Square", "Moon Quincunx", "Full Moon at", "New Moon ", 
        "Eclipse", "Partial", "Quarter ", "Lunar ", "Hybrid ", "Solar ", "Total ", "Annular ");

	protected static final Map<String, String> makeSpellEntries = Map.ofEntries(entry("Stabilise","Stabilize"), 	
		entry("Socialise","Socialize"), entry("Entering", ENTERS), entry("organised","organized"), 
		entry("excelent","excellent"), entry("realise","realize"), entry("spiritualilty","spirituality"), entry("wilfull","willful"), 
		entry("possibiities","possibilities"), entry("fantasise","fantasize"), entry("behaviour","behavior"), entry("Aint","Ain't"),
		entry("Strategize","possibilities"),
		entry("Neighbourhood","Neighborhood"),
		entry("recognise","recognise"),
		entry("Organise","Organise"),
		entry("socialise","socialise"),
		entry("crystallised","crystalized"),		
		entry("eadership","Leadership"),		
		entry("self-fulfilment","self-fullfilment"),		
		entry("wilful","willful"),		
		entry("centre","center")	
//		entry("xx","xx"),		
//		entry("xx","xx"),		
//		entry("xx","xx"),		
//		entry("xx","xx")	
				);
	protected static final HashMap <String, String> makeSpellhm =  new HashMap<>(makeSpellEntries);

	protected static final Map<String, String> signsEntryMap = Map.ofEntries(
	    entry("Cn", "Cancer "), entry("Ar", "Aries "), entry("Ta", "Taurus "), entry("Sg", "Sagittarius "),
		entry("Ge", "Gemini "), entry("Le", "Leo "), entry("Vi", "Virgo "), entry("Li", "Libra "),
		entry("Sc", "Scorpio "), entry("Cp", "Capricorn "), entry("Aq", "Aquarius "), entry("Pi", "Pisces " ));	
	
	protected static final Map<String, String> planSigns = Map.ofEntries(
		 entry("Mon", "Moon"), entry("Ari", "Aries"), entry("Tau", "Taurus"), entry("Gem", "Gemini"), 
		 entry("Can", "Cancer"),  entry("Leo", "Leo"), entry("Vir", "Virgo"), entry("Lib", "Libra"), entry("Sco", "Scorpio"), 
		 entry("Sag", "Sagittarius"),   entry("Cap", "Capricorn"), entry("Aqu", "Aquarius"),  entry("Pis", "Pisces"),
		 entry("Cnj", "Conjunct"), entry("Tri", "Trine"), entry("Opp", "Opposite"), entry("Sqr", "Square"),
		 entry("Sxt", "Sextile"), entry("Qnx", "Quincunx"), entry("Sun", "Sun"), entry("Mer", "Mercury"), 
		 entry("Ven", "Venus"),  entry("Mar", "Mars"), entry("Jup", "Jupiter"), entry("Sat", "Saturn"), 
		 entry("Nep", "Neptune"), entry("Ura", "Uranus"), entry("Plu", "Pluto")	);
	protected static final HashMap <String, String> planSignsAsp = new HashMap<>(planSigns);

	protected static final List<String> signsARRAY = (Arrays.asList("Ari", "Tau","Gem", "Can", "Leo",
		"Vir", "Lib","Sco", "Sag", "Cap", "Aqu", "Pis"));
	
	
	public static void main(String[] args) {
		String[] arryOfInFiles = getflist(START_DIR);	// create a list of names of those files
		String firstFileNme= arryOfInFiles[0];
		int strLenBeforeExt= firstFileNme.length()- 4;
		int indxUnderln= firstFileNme.indexOf("_");

		String sfOrigNmePlsTS = firstFileNme.substring(indxUnderln+1, strLenBeforeExt);
		out.println("string len: " + sfOrigNmePlsTS);
		final String sfOrigNmeWJavaTS = sfOrigNmePlsTS + DASH + UNIXTSTAMP;

		final String PersonFpath = BKSLSH  + sfOrigNmeWJavaTS ;
		final String DONEDIR = TOPDIR + "\\USED" + PersonFpath;  // OLD FILES done
		final String WORKDIR = TOPDIR + "\\WORK" + PersonFpath;  // OLD FILES done
		final String outDIRTMP = WORKDIR + "\\tempfiles";
		final String READYDIR = TOPDIR + "\\READY4PY" + PersonFpath;  // OLD FILES done		out.println("new substring: " + UNXTSTMP);
		makeDir(DONEDIR);  //READ the list of files in sfcalfiles/vds dir
		
		int fileInDirCNT = 0;

		while (fileInDirCNT < arryOfInFiles.length) {
			String infileNM = arryOfInFiles[fileInDirCNT];
			String inFILEstr = START_DIR + BKSLSH + infileNM;

			out.println("-- starting over in main LOOP# " + fileInDirCNT+1 +" filename is: " + infileNM);

			String finFILEnmWdir = mkDateFileNM(inFILEstr, infileNM, WORKDIR);
			delFiles(finFILEnmWdir);  // delete the inFileName we made last time

			String tOUTone = getTMPnmWdir(outDIRTMP,"-one", PersonFpath);

			generalStringFixing(inFILEstr, tOUTone);

			sectionTaskThree(tOUTone, finFILEnmWdir);

			out.println("- datefilename is: " + finFILEnmWdir+"----End of Loop---------NEW filename is: " + finFILEnmWdir);
			fileInDirCNT++;
		}
		copyDoneStartFiles(START_DIR, DONEDIR);
		moveReadyFiles(WORKDIR, READYDIR);
		out.println("Finished Program for " + PersonFpath);
	}

	public static void copyDoneStartFiles(String stdir, String doneDirectory) {
		String[] arryFiles = getflist(stdir);	// create a list of names of those files
		int fileCNT = 0;
		File doneDirPers = new File(doneDirectory);
		
		while (fileCNT < arryFiles.length) {
			String inFILEstr = stdir + BKSLSH + arryFiles[fileCNT];
			File oldfilef = new File(inFILEstr);
			try {
				FileUtils.copyFileToDirectory(oldfilef, doneDirPers, true);     
			} catch (IOException e) {
				e.printStackTrace(); }
			fileCNT++;
		}
		out.println("Finished copyDoneFiles");
	}
	
	public static void moveReadyFiles(String wkdir, String readyDirectory) {
		File readyDir = new File(readyDirectory);  // new dir
		String[] arryFilez = getflist(wkdir);	// create a list of names of those files
		int fileCNT = 0;
		
		while (fileCNT < arryFilez.length) {
			String inFILEstr = wkdir + BKSLSH + arryFilez[fileCNT];
			File oldfil = new File(inFILEstr);
			
			if (!oldfil.isDirectory()) {
				try {
					FileUtils.moveToDirectory(oldfil, readyDir, true);     
				} catch (IOException e) {
					e.printStackTrace();
				}			}
			fileCNT++;
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
		
	static String fixDESCRIPTION_line(String inSTRING) {
		CharSequence badLINEFchars = "\\n";
		String newstr = EMPTYSTR;  					//		out.println("just entered gofixDES. oldstrg is: " +inSTRING );

		String tString = inSTRING.replaceAll("%0A", EMPTYSTR);  // get rid of CRs  - \n
		if (tString.contains(badLINEFchars))    // for newline only
			newstr = tString.replace((String)badLINEFchars, " - ");
		else newstr = tString;
		tString = continueReplacing(newstr);
		if (tString.startsWith(" "))  {	 // spelling errors in extra lines of DESCRIPTION
			tString = newrepl(tString);
		}
		return tString;     }

	static String newrepl(String localSTR) {	
		 for (Map.Entry<String, String> entryPair : makeSpellEntries.entrySet()){
		    String oldVal = entryPair.getKey().toString();
		    String newVal = entryPair.getValue().toString();    
			if (localSTR.contains(oldVal)) {
				localSTR = localSTR.replace(oldVal, newVal);
			}
		}  
		return localSTR;     
		}

	static String continueReplacing(String fixmeSTR) {
		 for (Entry entryPair : replaceEntryMap.entrySet()){
			    String oldVal = entryPair.getKey().toString();
			    String newVal = entryPair.getValue().toString();    
				if (fixmeSTR.contains(oldVal)) {
					fixmeSTR = fixmeSTR.replace(oldVal, newVal);     }
			}  
			return fixmeSTR;     
			}
		
	static String fixSUMMARYsigns(String oldstrg) {				
		String tstring = oldstrg.replace("SUMMARY: ", "SUMMARY:");
		StringBuilder newbuf = new StringBuilder(tstring);

		String firstthird = tstring.substring(14,17);
		String secondthird = tstring.substring(18,21);
		String lastthird = tstring.substring(22,25);
		int start = 22;
		int end = 25;		
		boolean signmatch = true ? signsARRAY.contains(lastthird) : false;
		newbuf.delete(start, end);
		newbuf.insert(start, planSignsAsp.get(lastthird));
	
	//begin second column
		int starta = 18;
		int enda = 21;
		newbuf.delete(starta, enda);
		String secondspot = signmatch ? ENTERS : planSignsAsp.get(secondthird);
		newbuf.insert(start, secondspot);
		verboseOut( "value of signmatch:  " + signmatch + " found this: " + planSignsAsp.get(secondthird) + " new buf: " + newbuf);	
	
		String firstrep = planSignsAsp.get(firstthird);
		int startb = 8;
		int endb = 17;
		newbuf.delete(startb, endb);
		newbuf.insert(8,firstrep);
		verboseOut( "found this in hash:  " + firstrep + " new buf: " + newbuf.toString());		
		return newbuf.toString();
	}
	 // gofixhash

	// new method // --------------------------------------------------------------
	static String mkDateFileNM(String oldFileWDir, String oldname, String newfiledir) {
		List<String> oldfileAR = new ArrayList<>();
		String newDateNM = EMPTYSTR;
		int whileCT = 0; int indColon = 0; int tStart = 0; int tEnd = 0;
		int oldNmDotIndx = oldname.indexOf(".");
		String oldNameNoExt = oldname.substring(0, oldNmDotIndx);
		try {
			oldfileAR = FileUtils.readLines(new File(oldFileWDir));  //READ the list of files in sfcalfiles/vds dir

			while ( whileCT < 15) {
				String tmpSTR = oldfileAR.get(whileCT);
				if ( tmpSTR.contains(DTSTART)) {
					tStart=tmpSTR.indexOf(":")+1;
					tEnd=tStart+8;
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
			int lineCOUNT = 4;
			String cLINE;

			// for each line in file:
			while (lineCOUNT < origFILEARRY.size()) {
				verboseOut("myLINEct:  " + lineCOUNT);
				outLineCt = lineCOUNT;
				cLINE = origFILEARRY.get(lineCOUNT);
				String cLINEtwo = chmp(cLINE);  // chomp is removing the Z
				cLINE = chkForWeirdChar(cLINEtwo);

				if ( cLINE.contains(SUMstr)) {  /// if TR-Na only lines
					nwARRY.add(fixSUMMARYsigns(cLINE));
				}
				
				else if ( cLINE.contains(DESC))   {  /// if TR-NA only lines
					String cLINEnew1 = fixDESCRIPTION_line(cLINE);
					lineCOUNT++;
					int x = 1;
					while (x < 15) {
						String possibleContinue = origFILEARRY.get(lineCOUNT);

						if (possibleContinue.startsWith(" ")) {
							String possibleContinue2 = fixDESCRIPTION_line(possibleContinue) ;
							String cLINEnew2 = cLINEnew1 + possibleContinue2;
							x++;
							lineCOUNT++;
							cLINEnew1 = cLINEnew2;
							}	
						else {
							x = 26;
							lineCOUNT--;
						}
						}
					nwARRY.add(cLINEnew1 + "\n");
				}
				else if ( cLINE.contains("DTSTAR") ) {
					if (!cLINE.contains("VALUE")) { //skip these; they are moon for the day
						String theDTENDline = chkAddDTEND(cLINE);
						nwARRY.add(cLINE );
						nwARRY.add(theDTENDline );
					}
				}
				else {
					verboseOut("   writing ORIGINAL string to file         " + cLINE);
					nwARRY.add(cLINE );
				}
				lineCOUNT++;
				cLINEtwo = EMPTYSTR;
			}  // while lines in file arrray
			verboseOut("Writing to file: " + SCALtempONE.getName());
			FileUtils.writeLines(SCALtempONE, nwARRY);
		}  // try
		catch (IOException e)  {
			e.printStackTrace();   }
		}	// end of method

	static String fixDTSTART(String uline) {
		return DTSTART + uline.substring(8,22) + "1Z";    }


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

	// new method // --------------------------------------------------------------
	public static void delFiles(String f2in) {
		File f1 = new File(f2in);
		if ( f1.exists() ) {
			f1.delete();  // delete the inFileName we made last time
		}
	}

	// new method // --------------------------------------------------------------
	protected static void mySleep(long timewait) {
		try {
			Thread.sleep(timewait * 1000);	//sleep is in milliseconds
		} catch (Exception e) {
			System.out.println(e);
		}
	} // mySleep


	// new method // --------------------------------------------------------------
	static String chkForWeirdChar(String checkLine) {
		try {
			if (checkLine.contains( "\uFFFD"))  {
				String newStringy = checkLine.replace( "\uFFFD", " ");
				verboseOut("FOUND WEIRD CHAR -----!!!!  !!!  " + checkLine + "The fixed line: " + newStringy);
				return newStringy;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return checkLine;
	}

	// new method // --------------------------------------------------------------
//	static boolean checkLINEfunction(String locLine) {
//		boolean KG = true;
////		if   ((locLine.length() > 0 ) )   {
////
////			if   ((theLocLine.length() > 0 ) )
////			{ KG = true; }
////			else { KG = false; }
//
//		KG = true ? locLine.length() > 0  : false;
//		KG = false ? locLine.contains("THISISATESTONLY") : true;
//			
////			if ( ( theLocLine.contains("THISISATESTONLY")) )
////			{
////				KG = false;
////			}
//		
//		return KG;
//	}

	//----new Method ===============================================================//
	static String replaceSigns(String locStr) {
		String answerst = locStr;
//				
//		if (!locStr.substring(21,22).equals(" ")) {     // continue as usual
//			int aCount = locStr.substring(23,24).equals(" ") ? 23 : 24;
//			String tsign = locStr.substring(aCount, aCount+2);
//		}
//
//		else {     // scoot everything over one space
//			int aCount = locStr.substring(22,23).equals(" ") ? 23 : 22;
//			String tsign = locStr.substring(aCount, aCount+2);
//		}
////			if (locStr.substring(22,23).equals(" ")) {
////				tsign = locStr.substring(23,25); }
////			else tsign = locStr.substring(22,24);
////		}
		
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
		if (origLine.contains(theVal))  {
			return origLine.replace( theVal, theRep);
		}
		else return origLine;
	}

	static void sectionTaskThree(String tFILEin, String finFILE) {   // this part was done by perl script
		int totInFileLines = 0;
		int totInfilesMinusNine = 0;
		int locLineCount = 4;  // start at 5th line
		File theREADING_FROM_TMP_FILE = new File(tFILEin);
		File finalFILE = new File(finFILE);
		verboseOut("tFILEin: " + tFILEin + " finFILE: " + finFILE);
		ArrayList<String> lastFILE_ARRAY = new ArrayList<String>();

		try {
			List<String> tempFILE_ARRAY = FileUtils.readLines(theREADING_FROM_TMP_FILE);
			totInFileLines = tempFILE_ARRAY.size();
			// get ics header lines in 1st-first four header lines of ics inFileName

			for (int i = 0; i < 4; i++)	{
				lastFILE_ARRAY.add(tempFILE_ARRAY.get(i));
			}
			totInfilesMinusNine = totInFileLines-9;  //  
			verboseOut("!!! totInfilesMinusNine: " + totInfilesMinusNine );

			while ( locLineCount < totInfilesMinusNine )  
			{                 // while there are still lines left in array // starting on 5th line, load
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
					if ( !checkforend) {
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
				newARRAYSIZE = lastFILE_ARRAY.size();  // verboseOut("curLINEct: " + curLINEct + " arSIZE: "  + arSIZE + " new array size: " + newARRAYSIZE);
				curLINEct = curLINEct + 1;  //move the line counter up to the next group
			}  // while

			FileUtils.writeLines(finalFILE, lastFILE_ARRAY, true);
			SCALperson.mySleep(1);
			FileUtils.waitFor(finalFILE, 1);
		}  					  
		catch (IOException e) {  	e.printStackTrace();	 }	// catch
	} 			 // end of method

	// new method: ----------------------------------------------------------------	
	static String concatDESC (String ttline, List<String> fileARY, int cLineCT, List<Integer> cLONG) {
		String concatline2 = EMPTYSTR; String concatline3 = EMPTYSTR; String concatline4 = EMPTYSTR;
		cLONG.add(cLineCT+1);
		String concatline1 = fileARY.get(cLineCT+1);

		if (fileARY.get(cLineCT+2).startsWith(" ")) {
			concatline2 = fileARY.get(cLineCT+2);
			cLONG.add(cLineCT+2);

			if (fileARY.get(cLineCT+3).startsWith(" ")) {
				concatline3 = fileARY.get(cLineCT+3);
				cLONG.add(cLineCT+3);

				if (fileARY.get(cLineCT+4).startsWith(" ")) {
					concatline4 = fileARY.get(cLineCT+4);
					cLONG.add(cLineCT+4);
				}  
			}  
		}  
		String longstaar = EMPTYSTR.concat(ttline + concatline1 + concatline2 + concatline3 + concatline4);
		verboseOut("longstring is : " + longstaar);
		return longstaar;
	}

	// new method: ----------------------------------------------------------------	
	static List<String> addmyLines (List<Integer> cntLONG, List<String> farray, String longstrg) {
		int numberRemoved = cntLONG.size();  // should be around 3					
		for (int i=0; i < numberRemoved; i++) {
			int ttInt = cntLONG.get(0);
			farray.remove(ttInt);  // remove little strings
		}
		int wheretoaddline = cntLONG.get(0);
		farray.add(wheretoaddline, longstrg);  // add new long string back
		return farray;
	} 	

	// new method: ----------------------------------------------------------------
	static boolean checkSUMMARYforToss(List<String> tinyList) {
		int droplen = DROPPHRASES.size();
		String summaryLine = tinyList.get(6);  // checking the 6th line : SUMMARY
		//		verboseOut("   starting over in checkForTossouts. \nThe string is:  " + summaryLine );
		
		for (int indx=0; indx < droplen; indx++) {
			if (summaryLine.contains(DROPPHRASES.get(indx))) {
				verboseOut("========== !!!!! found a tosser: tossing: "+ summaryLine);	
				return false;
				}
		}
		return true; 
	} // method end
}  // class
