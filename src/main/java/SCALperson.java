
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;


public class SCALperson {
	public static final String BKSLSH = "\\";
	static int G_VERBOSE = 1;
	static final String newfront = "DTEND:";
	public static final String EMPTYSTR = "";
	public static final char LF = '\n';
	public static final char CR = '\r';
	public static Long SYSTIME = currentTimeMillis();
	public static final String UNIXTSTAMP = SYSTIME.toString().substring(3, 9);
	public static String TOPDIR = "C:\\SFOUT";  // WHERE SF dumps files
	public static String START_DIR = TOPDIR + "\\START";  // WHERE SF dumps files
	public static final char DASH = '-';
	public static int outLineCt = 0;
	
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
				e.printStackTrace();
			}
			
			fileCNT++;
		}
		out.println("Finished copyDoneFiles");
	}
	
	public static void moveReadyFiles(String wkdir, String readyDirectory) {
		File readyDir = new File(readyDirectory);  // new dir
		String[] arryFilez = getflist(wkdir);	// create a list of names of those files
		int fileCNT = 0;
		int arlen = arryFilez.length;
		
		while (fileCNT < arlen) {
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
			char ch = str.charAt(0);
			if (ch == CR || ch == LF) {
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
		String sNAME = tnm + "\\SCALtmp" + UNXTS + myIn + ".ics";
		return sNAME;}

	static String[] getflist(String dnm) {  // 1 for name, 2 for file
		File filesDir = new File(dnm);  //READ the list of files in sfcalfiles/vds dir
		String[] arryOfInFiles = filesDir.list();	// create a list of names of those files
		return arryOfInFiles;}

	public static void verboseOut(String theoutline) {
		if (G_VERBOSE==1) {
			out.println(theoutline);
		}}


	// new method // --------------------------------------------------------------
	static String fixDESCRIPTION_line( String  inSTRING) {
		CharSequence badLINEFchars = "\\n";
		String badLINEFstr = (String)badLINEFchars;
		String newstr = EMPTYSTR;  //		out.println("just entered gofixDES. oldstrg is: " +inSTRING );

		String tString = inSTRING.replaceAll("%0A",EMPTYSTR);  // get rid of CRs  - \n
		if (tString.contains(badLINEFchars))    // for newline only
			newstr = tString.replace(badLINEFstr, " - ");
		else newstr = tString;
		tString = continueReplacing(newstr);
		if (tString.startsWith(" "))   // spelling errors in extra lines of DESCRIPTION
			tString = newrepl(tString);
		if (outLineCt > 195) {
			out.println(inSTRING);

		}
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
			//			out.println("!!!----- value of hmap retrieval: " + oldVal + " " + newVal);
			if (localSTR.contains(oldVal)) {
				newstr = localSTR.replace(oldVal, newVal);
				verboseOut("SPELLING ERROR!!!! ----------replaced string with new string... now fixed: " + newstr);
			}
		} //for
		return newstr;
	}


	static String continueReplacing(String fixmeSTR) {
		String newTempStr = fixmeSTR.replace("Transiting ",EMPTYSTR );
		fixmeSTR= newTempStr.replace("Conjunction","Conjunct");
		newTempStr= fixmeSTR.replace("Opposition","Opposite");
		fixmeSTR = newTempStr.replace("Entering","Enters" );
		String newTempStr1 = fixmeSTR.replace("DESCRIPTION:The ","DESCRIPTION:" );
		newTempStr = newTempStr1.replace(" Radix "," " );
		int tlen = newTempStr.length();
		if (outLineCt > 200) {
			if (tlen < 15) {
				out.println("less than 15");
		}}

		verboseOut("new string... now fixed: " + newTempStr+ "value of instr:  " + fixmeSTR+ "return this new value  " + newTempStr);
		return newTempStr;
	}


	// new method // --------------------------------------------------------------
	static String fixSUMMARYsigns(String oldstrg, boolean isDIRorRET) {
		Map<String, String> hm = makeNewhash();
		String tstring = oldstrg.replace("SUMMARY: ", "SUMMARY:");
		String newstr = "empty";
		StringBuffer newbuf = new StringBuffer(tstring);

		List<String> signsARRAY = (Arrays.asList("Ari", "Tau","Gem", "Can", "Leo",
				"Vir", "Lib","Sco", "Sag", "Cap", "Aqu", "Pis"));
		boolean signmatch =false;
		String firstthird = EMPTYSTR;
		String secondthird = EMPTYSTR;
		String lastthird = EMPTYSTR;

		if (!isDIRorRET) {
			lastthird = tstring.substring(22,25);
			String xxx = lastthird;
			verboseOut("value of xxx:  " + xxx);

			// lastthird = tstring.substring(22,25);
			firstthird = tstring.substring(14,17);
			if (signsARRAY.contains(lastthird))
				signmatch = true;
		}
		verboseOut("in fixSUMMARYsigns. first:  " + firstthird+" 2nd  :  " + secondthird+" 3rd  :  " + lastthird);
		//begin third column
		if (!isDIRorRET) {
			String thirdrep = hm.get(lastthird);			
			int start = 22;
			int end = 25;
			newbuf.delete(start, end);
			newbuf.insert(start,thirdrep);
			String info_line3 = "found this in hash:  " + lastthird+"new buf is: " + newbuf;
			verboseOut(info_line3);	
		}
		//begin second column
		if  (!isDIRorRET) {
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
			String info_line2 = "value of signmatch:  " + signmatch+ "found this in hash:  " + secondrep+"new buf is: " + newbuf;
			verboseOut(info_line2);	
		}
		// begin first column
		if (isDIRorRET ) {
			firstthird = tstring.substring(8,11);
			String longsign = hm.get(firstthird);
			newstr = tstring.replace(firstthird, longsign);
		}
		if (!isDIRorRET ) {
			String firstrep = hm.get(firstthird);

			int start = 8;
			int end = 17;
			newbuf.delete(start, end);
			newbuf.insert(8,firstrep);
			newstr = newbuf.toString();
			String info_line = "found this in hash:  " + firstrep + "new buf is: " + newbuf + "    replaced string with new string... now fixed: " + newstr + " | " + " value of newstr:  " + newstr+ "return this new value  " + newstr;
			verboseOut(info_line);		
		}
		return newstr;
	} // gofixhash


	// new method // --------------------------------------------------------------
	static HashMap<String, String> makeSpellhm() {
		HashMap <String, String> spellhm = new HashMap<>();
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
		List<String> oldfileAR = new ArrayList<>();
		String newDateNM = EMPTYSTR;
		String theTst = "DTSTART";
		String ics_ext =  ".ics";
		int whileCT = 0;
		int indColon = 0;
		int tStart = 0;
		int tEnd = 0;
		// filename:
		int oldNmDotIndx = oldname.indexOf(".");
		String oldNameNoExt = oldname.substring(0, oldNmDotIndx);
		try {
			oldfileAR = FileUtils.readLines(new File(oldFileWDir));  //READ the list of files in sfcalfiles/vds dir

			while ( whileCT < 15) {
				String tmpSTR = oldfileAR.get(whileCT);
				if ( tmpSTR.contains(theTst)) {
					indColon = tmpSTR.indexOf(":");
					tStart=indColon+1;
					tEnd=tStart+8;;
					String newDateStr = tmpSTR.substring(tStart, tEnd);
					String newOldName = oldNameNoExt + DASH + newDateStr + ics_ext;
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
		CharSequence SUMstr = "SUMMARY:Tr-Na";
		String DEStr = "DESCRIPTION";
//		String theDTENDline=EMPTYSTR;
//		String cLINEtwo = EMPTYSTR;

//		G_VERBOSE = 1;

		try {
			List<String> origFILEARRY = FileUtils.readLines(origFILE);
			int arraySIZE = origFILEARRY.size();
			System.out.println("orig file size:  " +  arraySIZE + " ----------------------------------%%%%%%%##### total lines: " +  origFILEARRY.size());
			// get ics header lines in 1st-first four header lines of ics inFileName
			int lineCOUNT =0;
			String cLINE;

			// for each line in file:
			while (lineCOUNT < arraySIZE) {
				verboseOut("myLINEct:  " + lineCOUNT);
				outLineCt = lineCOUNT;
				cLINE = origFILEARRY.get(lineCOUNT);
				String cLINEtwo = chmp(cLINE);  // chomp is removing the Z
				cLINE = chkForWeirdChar(cLINEtwo);
				//				System.out.println("    char string is:         " + cLINE);
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
					if ( cLINE.contains(DEStr))   {  /// tests
						out.println(DEStr);
						out.println(lineCOUNT);
					}
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
						String theDTENDline = chkAddDTEND(cLINE);
						nwARRY.add(cLINE );
						nwARRY.add(theDTENDline );
					}
				}
				else if (cLINE.contains("Moon goes void")) {
					cLINEtwo = "SUMMARY:Moon void of course";
					int newSIZE = nwARRY.size();
					int back_three_count = newSIZE - 5;
					int back_two_count = newSIZE - 4;
					String back_3_str = nwARRY.get(back_three_count);
					verboseOut("old string: " + back_3_str);
					String newDTSTART = fixDTSTART(back_3_str);
					String new_DTEND_line = stdUtilCreateDTEND(back_3_str);
					nwARRY.set(back_three_count, newDTSTART);
					nwARRY.set(back_two_count, new_DTEND_line);
					nwARRY.add(cLINEtwo );
					lineCOUNT++;
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
			//			System.out.println("first end");
		}  // try
		catch (IOException e)  {
			e.printStackTrace();
		}  // catch

	}	// end of method

	static String fixDTSTART(String uline) {
		String partialEND = uline.substring(8,22) + "1Z";
		String newstr = "DTSTART:" + partialEND;
		verboseOut("DTSTART: new line is " + newstr);
		return newstr;
	}

	static String stdUtilCreateDTEND(String utline) {
		String partialEND = utline.substring(8,22) + "1Z";
		String newDTENDstr = "DTEND:" + partialEND;
		verboseOut("DTEND: new line is " + newDTENDstr);
		return newDTENDstr;
	}

	static String fixDirRetro(String retroString) {
		String charD = " D";
		String charR = " R";  // MUST have a space first

		if (retroString.startsWith("SUMMARY:Tr "))   {
			String retStringTwo = retroString.replace("Tr ", EMPTYSTR);

			int cStart = retStringTwo.length()-3;  // a space & there's a line ending too
			int cEnd = retStringTwo.length()-1;
			String tempSub = retStringTwo.substring(cStart,cEnd);  // get the last char

			if (tempSub.equals(charR))  {
				retroString = retStringTwo.replace(charR, " goes Retrograde");
			}
			else if (tempSub.equals(charD))  { /// if TR-NA only lines
				retroString = retStringTwo.replace(charD, " goes Direct");
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
				verboseOut("                   !! inside chkAddDTEND -----                        @@@@@  the line is  " + theLine);
				newDTEND = newfront + newback;
				verboseOut("DTEND: new line is " + newDTEND);
			}
		}
		return newDTEND;
	}


	// new method // --------------------------------------------------------------
	static HashMap<String, String>  makeNewhash() {
		HashMap <String, String> localHash = new HashMap<>();
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
		try {
			if (checkLine.contains( "\uFFFD"))  {
				verboseOut("!!!---            ---FOUND WEIRD CHAR -----!!!!  !!!  ");
				verboseOut(checkLine);
				String newStringy = checkLine.replace( "\uFFFD", " ");
				verboseOut("The fixed line: " + newStringy);
				return newStringy;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return checkLine;
	}


	// new method // --------------------------------------------------------------
	static boolean checkLINEfunction(String theLocLine) {
		boolean KG = true;
		if   ((theLocLine.length() > 0 ) )   {

			if   ((theLocLine.length() > 0 ) )
			{ KG = true; }
			else { KG = false; }

			if ( ( theLocLine.contains("THISISATESTONLY"))
					)
			{
				KG = false;
			}
		}
		return KG;
	}

	//----new Method ===============================================================//
	static String replaceSigns(String theInputStr) {
		String answerst =theInputStr;
		String tsign = EMPTYSTR;
		String aspace = EMPTYSTR;
		boolean theProblem = false;
		verboseOut("inside replaceSigns");
		HashMap <String, String> theHashmap = makemyhash();

		String ShortSpace = theInputStr.substring(21,22);
		if (ShortSpace.equals(" ")) {
			theProblem = true;
		}

		if (!theProblem) {     // continue as usual
			aspace = theInputStr.substring(23,24);
			if (aspace.equals(" ")) {
				tsign = theInputStr.substring(24,26);
			}
			else tsign = theInputStr.substring(23,25);
		}

		if (theProblem) {     // scoot everything over one space

			aspace = theInputStr.substring(22,23);
			if (aspace.equals(" ")) {
				tsign = theInputStr.substring(23,25);
			}
			else tsign = theInputStr.substring(22,24);
		}

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

	static String checkForSigns(String origLine, String theVal, String theRep) {
		verboseOut("inside checkForSigns checking val rep: "+theVal + theRep);
		if (origLine.contains(theVal))  {
			String theFixedLine = origLine.replace( theVal, theRep);
			verboseOut("---------FOUND sign CHAR ------------------The fixed line: " + theFixedLine);
			return theFixedLine;
		}
		else return origLine;
	}


	// new method // --------------------------------------------------------------
	static HashMap<String, String>  makemyhash() {
		HashMap <String, String> myHashmap = new HashMap<>();
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

	static void sectionTaskThree(String tFILEin, String finFILE) {   // this part was done by perl script
		int totInFileLines = 0;
		int totInfilesMinusNine = 0;
		int locLineCount = 4;  // start at 5th line
		String LINE_FEED = System.getProperty("line.separator");
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
				ArrayList<String> tinySectionList = new ArrayList<String>();
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
						if (theString.contains("Stationary") &&  (theString.contains("DESCRIPTION") )	)
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

			} //  // while locLineCount
			lastFILE_ARRAY.add("END:VCALENDAR"+LINE_FEED);
			List<Integer> cntLONG = new ArrayList<Integer>();
			String tline= EMPTYSTR;
			String longstr = EMPTYSTR;
			int arSIZE = lastFILE_ARRAY.size();
			int newARRAYSIZE = arSIZE;
			int curLINEct = 0;
			boolean yesDESC = false;

			while ( curLINEct < newARRAYSIZE) {    // while we're still in the file
//				verboseOut("curLINEct:"  + curLINEct +" arSIZE: "  + arSIZE);
				yesDESC = false;
				tline = EMPTYSTR;
				longstr = EMPTYSTR;				
				tline = lastFILE_ARRAY.get(curLINEct);

				if (tline.contains("DESCRIPTION")) {
					yesDESC = true;
					cntLONG.clear();
					cntLONG.add(curLINEct);

					longstr = concatDESC (tline, lastFILE_ARRAY, curLINEct, cntLONG);  
					tline = EMPTYSTR;
				}	// if DESCRIPTION

				if (yesDESC) {
					addmyLines (cntLONG, lastFILE_ARRAY, longstr);
				}
				cntLONG.clear();
				newARRAYSIZE = lastFILE_ARRAY.size();
//				verboseOut("curLINEct: " + curLINEct + " arSIZE: "  + arSIZE + " new array size: " + newARRAYSIZE);

				curLINEct = curLINEct + 1;  //move the line counter up to the next group
			}  // while

			FileUtils.writeLines(finalFILE, lastFILE_ARRAY, true);
			SCALperson.mySleep(1);
			FileUtils.waitFor(finalFILE, 1);
		}  // try  
		catch (IOException e) {  	e.printStackTrace();	 }	// catch
	}  // end of method

	// new method: ----------------------------------------------------------------	
	static String concatDESC (String ttline, List<String> fileARY, int cLineCT, List<Integer> cLONG) {
		String concatline2 = EMPTYSTR;
		String concatline3 = EMPTYSTR;
		String concatline4 = EMPTYSTR;
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
		String sl = tinyList.get(6);  // checking the 6th line : SUMMARY
		verboseOut("   starting over in checkForTossouts. \nThe string is:  " + sl );
		if (sl.contains("SUMMARY"))  {
			if ((sl.contains("Full")) || (sl.contains("New")) || (sl.contains("Quarter"))) {
					verboseOut("========== !!!!! reg method FOUND Full-New-Quarter!!!  tossing: "+ sl);		
					return false;  
			}   // toss
			else {  return true; }
		}
		else {
			return true; 
		}	
	} // method end
}  // class
