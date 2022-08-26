
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
		entry("prioritise","prioritize"));
	
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
			ArrayList ar = sectionTaskThree(tOUTone);
			finalWrite(finFILEnmWdir, ar);

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
		String theBufStr = newbuf.toString();
		out.println(theBufStr);
		return theBufStr;
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
			
			// for each line in file:
			int linePosition = 4;
			while (linePosition < origFILEARRY.size()) {
				verboseOut("myLINEct:  " + linePosition);
				cLINE = origFILEARRY.get(linePosition);
				String cLINEtwo = chmp(cLINE);  // chomp is removing the Z
				StringBuilder mainDescLineAllsb = new StringBuilder();
				cLINE = cLINEtwo; 
				
				if ( cLINE.contains(SUMstr)) {  /// if TR-Na only lines
					String NewSummary = fixSUMMARYsigns(cLINE);
					nwARRY.add(NewSummary);
					out.println("NewSummary: " + NewSummary);
					linePosition++;
				}
				
				else if ( cLINE.contains(DESC))   {  /// if TR-NA only lines
					String mainDescLineAll = fixDESCRIPTION_line(cLINE);
					mainDescLineAllsb.append(mainDescLineAll);
					int x = 1;
					while ( x < 9) {
						linePosition++;
						String possContinueText = origFILEARRY.get(linePosition);
						 
						if (possContinueText.startsWith(" ")) {
							String possContinueTextFixed = fixDESCRIPTION_line(possContinueText) ;
							mainDescLineAllsb.append(possContinueTextFixed);
							x++;
							}
						else {
							x = 10;  //to break loop
							}
					}
					nwARRY.add(mainDescLineAllsb.toString());
//					out.println("ADDED to first temp file: " + mainDescLineAllsb.toString());
					} // for
											 
				else if ( ( cLINE.contains("DTSTAR") ) && (!cLINE.contains("VALUE")) )  {
					nwARRY.add(cLINE );
					out.println("adding dtstart line: " + cLINE);
					String dtendline = chkAddDTEND(cLINE);   //skip these; they are moon for the day
					nwARRY.add(dtendline );   //skip these; they are moon for the day
					out.println("adding: " + dtendline);
					linePosition++;
						}
				else {
					verboseOut("   using ORIGINAL string:  " + cLINE);
					nwARRY.add(cLINE );
					out.println("adding: " + cLINE);
					linePosition++;
				}
			cLINEtwo = EMPTYSTR;	
			}  // while
			verboseOut("Writing to file: " + SCALtempONE.getName());
			FileUtils.writeLines(SCALtempONE, nwARRY);
		}  // try
		catch (IOException e)  {
			e.printStackTrace();   }
		}	// end of method

	static String chkAddDTEND (String theLine) {
		StringBuilder locStrBdr = new StringBuilder(newfront);
		if (( theLine.contains("DTSTAR")) && ( !theLine.contains("VALUE")) ) { //moon for the  day
			String tlin = theLine.substring(8,23);
			locStrBdr.append(tlin).append("Z");
			out.println("   !! inside chkAddDTEND --  dtend: " +  locStrBdr.toString());
			}
		return locStrBdr.toString();
	}

	protected static void mySleep(long timewait) throws InterruptedException {
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

	static String checkForSigns(String origLine, String theVal, String theRep) {
		verboseOut("inside checkForSigns checking val rep: "+theVal + theRep);
		if (origLine.contains(theVal))  
			return origLine.replace( theVal, theRep);
		else return origLine;
	}

	static ArrayList sectionTaskThree(String tFILEin) {   //1 this part was done by perl script
		int totInfilesMinusNine = 0;
		int locLineCount = 0;  // start at 5th line
		File theREADING_FROM_TMP_FILE = new File(tFILEin);
		ArrayList<String> lastFILE_ARRAY = new ArrayList<String>();

		try {  //2
			List<String> tempFILE_ARRAY = FileUtils.readLines(theREADING_FROM_TMP_FILE);			
			totInfilesMinusNine = tempFILE_ARRAY.size()-9;  //  
			verboseOut("!!! totInfilesMinusNine: " + totInfilesMinusNine );
			while ( locLineCount < totInfilesMinusNine )   {//3                 // while there are still lines left in array // starting on 5th line, load
				ArrayList<String> tinySectionList = new ArrayList<>();
				// first load sections of 10x lines each into smaller arrays  then check each section for voids etc  then correct

				for (int tc=0; tc < 11; tc++) {//4         //tiny while
					String theString = tempFILE_ARRAY.get(locLineCount);  //get one string
					boolean checkforend = theString.startsWith("END:VEVENT", 0);
					if (checkforend) {
						tinySectionList.add(theString);
						locLineCount++;
						break;
						}
					else {
						tinySectionList.add(theString);
						locLineCount++;
						}
					}  //4     

				boolean checkToKeep = checkSUMMARYforToss(tinySectionList);	 // true is keep and write 
				 
				if (checkToKeep) { //4   // IF 	check for toss comes back TRUE, then write this section
					lastFILE_ARRAY.addAll(tinySectionList);
					} 		//4
				}		//3					//  // while locLineCount
		}  catch (Exception e){
			e.printStackTrace(); }			
		return lastFILE_ARRAY;
	} 			 // end of method

	static void finalWrite(String finFILE, ArrayList arlist ) {
		File finalFILE = new File(finFILE);
		try {	
			FileUtils.writeLines(finalFILE, arlist, true);
			SCALperson.mySleep(1);
			}
		catch (Exception e){
				e.printStackTrace();	
		}			
		FileUtils.waitFor(finalFILE, 1);
	}

	static String fixDESCRIPTION_line(String inSTRING) {
		CharSequence badLINEFchars = "\\n";
		String newstr = EMPTYSTR;  			 

		String tString = inSTRING.replaceAll("%0A", EMPTYSTR);  // get rid of CRs  - \n
		if (tString.contains(badLINEFchars))    // for newline only
			newstr = tString.replace((String)badLINEFchars, " - ");
		else newstr = tString;
		tString = continueReplacing(newstr);  // remove Radix and other long words
		if (tString.startsWith(" "))  {	 // spelling errors in extra lines of DESCRIPTION
			tString = newrepl(tString);
		}
		return tString;     }
	
	static boolean checkSUMMARYforToss(ArrayList mySummaryList) {
		int summaryLineInt = 6;
		String summaryLine = mySummaryList.get(summaryLineInt).toString();
		
		verboseOut("   starting over in checkForTossouts. \nThe string is:  " + summaryLine );
		
		for (int indx=0; indx < DROPPHRASES.size(); indx++) {
			String ckStr = DROPPHRASES.get(indx);
			if (summaryLine.contains(ckStr)) {
				verboseOut("========== !!!!! found a tosser: tossing: "+ summaryLine);	
				return false;
				}
		}
		return true; 
	} // method end
}  // class

//		ArrayList<String> jmap_list_arry = new ArrayList<String>();
//					int sizea = tinySectionList.size();					
	//					if (sizea > 0) { String startstr = tinySectionList.get(0);
	//						out.println("startstr: " + startstr); }
	//					String endstr = tinySectionList.get(2); String summarystr = tinySectionList.get(2);
	//					String descriptionstr = tinySectionList.get(2);
	//					jmap.put("start",startstr);   	jmap.put("description", descriptionstr);					




