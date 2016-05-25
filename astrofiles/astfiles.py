'''
Created on Nov 22, 2015, May 25, 2016
@author: Nancy Schorr
'''
#  translate the old "nfi"-files, that I have (they are basically text-files with a certain structure, as far as I understand... one file consists of a lot of astrological dates, each in one line... each line would be one aaf-file) into aaf data.
## old is nfi, new is aaf
## to do py2exe : use cygwin. cd to prj dir. run python command from there. it's in the path.



import pymsgbox
pymsgbox.confirm("Put nfi files in C:\\astin - new aaf files will be in C:\\astout", 'Welcome to Astrofiles',["Start Converting", 'Cancel'])
#response = pymsgbox.prompt('What is your name?')


#"Translate nfi files into aaf files\n")
#"Put nfi files in C:\\astin - new aaf files will be in C:\\astout")
#"Press the return key to start the program. Go to C:\astout when the program ends to find your files")

 

PYTHONDONTWRITEBYTECODE = True   #prevents bytecode .pyc files from being created
import sys
import datetime
import astrs
import os
#import re

if __name__ == '__main__':   pass

############### constants:
DIR_SEP ='\\'
NLINE = "\n"
cSTAR = "*"
cCOMMA = ","
cInputDIR ='C:\\astin'      ## must be there
cOutDIR =  'C:\\astout'      ## where you are sorting to, created if not there
locInFileList = []

#-----------------globals:
MAIN_DATE_STR = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
global_INDIR_DONE = cInputDIR +  DIR_SEP + 'done' + str(MAIN_DATE_STR)
global_OUTDIR =  cOutDIR +  DIR_SEP + 'done' + str(MAIN_DATE_STR)
global target_FILE

############### end setup vars
#A93:*,Axel,*,27.7.1968,15:31:00,Geseke,D
#B93:2440065.10500,51N38:00,008E31:00,1hE00:00,*


#----------------------------------------------------
def splitNfiFile(inFile):
    adbcFinal = 'AD'
    AAF_END = '.aaf'
    infile_firstpart = inFile[:-4]
    FINAL_OUTFILE= global_OUTDIR + DIR_SEP + infile_firstpart + AAF_END
    target_FILE2 = open(FINAL_OUTFILE, 'w')  ## target_FILE is OPENED
    target_FILE2.close()
    
    theInfile = cInputDIR +  DIR_SEP + inFile
    print "now the infile is: " + theInfile
   
    #target_FILE = open(FINAL_OUTFILE, 'w')  ## target_FILE is OPENED
    #fn = target_FILE.fileno()
 
    print "----------###!!! just got passed: " + inFile 
    print "-------------working on new file: " + FINAL_OUTFILE
       
    print NLINE + "datestring: " + MAIN_DATE_STR + "  cInputDIR : " + cInputDIR
    print "global_OUTDIR: " + global_OUTDIR +" global_INDIR_DONE: " + global_INDIR_DONE + " FINAL_OUTFILE: " + FINAL_OUTFILE + NLINE
   
    #print "file number of newfile is: " + fn
                
                
                
    fileexists = os.path.exists(theInfile)
    if not fileexists :
        pymsgbox.alert("ERROR!! No aaf files in C:\\astin", 'Astrofiles Aborted')
    
    theLines = open( theInfile ).read().splitlines()
    for myLine in theLines :
        endName = 0
        #endNameComma = '#'
        hasComma = False
        hasAtsign = False
        atsignLoc = 0
        hasFirstName = False
        firname = "*"
        fnameEnd = 0
        firnameStart = 0
        #print hasComma + " " + hasAtsign
        lnameEnd = 0
  
        if myLine.startswith("$"):   ### start next record
            if "," in myLine :    ### has first and last name
                hasComma = True;
                commaLoc = myLine.find(',')  ## , is at end of last name
                lnameEnd = commaLoc  ### most simple case
                hasFirstName = True

            if '@' in myLine :    ### has first and last name
                hasAtsign = True;
                atsignLoc = myLine.find('@')  ## @ is at end of full name
                
                if (( hasComma == False ) and (hasAtsign == True )) :
                    lnameEnd = atsignLoc    ### use the @ if there is not comma for lname end
                                            ## has no firstname
               
            if ((hasComma == False) and (hasAtsign == False)) :  # no comma, no @ 
                lnameEnd = len(myLine)    ## has no firstname
            
            if ((hasComma == True) and (hasAtsign == False)) :    # has comma no @
                fnameEnd = len(myLine)     ## firstname ends at end of line
                
            if ((hasComma == True) and (hasAtsign == True)) :     # has comma and @
                fnameEnd = atsignLoc          ## firstname ends at @
            
            if (hasFirstName is True) :
                endName = fnameEnd
            else :
                endName = lnameEnd
            print "lnameEnd= " + str(lnameEnd) + " ---------- !!!! inside qualifying if loop "
            adbcFinal =  astrs.fixadbc(myLine[1:2])
            lname =myLine[40:lnameEnd]
            if (hasFirstName is True) :
                firnameStart = lnameEnd + 1
                firname = myLine[firnameStart:fnameEnd]
            print "firname = " + firname

            yr = myLine[2:6]
            mon =  myLine[6:8]
            day =  myLine[8:10]
            tmraw =  myLine[10:16]
            tm = astrs.formatTime(tmraw)
            zn_offset_orig = myLine[17:22]
            zn_offset = astrs.fixzn_offset(zn_offset_orig)
            lat_orig =  myLine[22:27]
            lat = astrs.fixlat(lat_orig)
            longt_orig = myLine[27:33]
            gender = myLine[33:34]
            longt = astrs.fixlongt(longt_orig)
            country="*"
            city =myLine[endName+1:]
            fullDate=day + "." + mon + "." +yr
            if len(city) < 1:
                city = "*"
            if ((gender != 'M') and  (gender != 'F') ):
                gender = '*'
            if city == 'Bonn' :
                country = "D"
            writeOutFileContent(FINAL_OUTFILE,firname,lname,fullDate,tm,zn_offset,lat,longt,gender,city,country)
       
        ##### special case for extra comment lines:
        if not myLine.startswith("$"):  ## should work for 2 extra comment lines at least
            lineLength = len(myLine)
            print "---------------------------------######################---------------!!!!!!here is empty comments extra myLine:"
            print myLine + "\n" +  " lineLength +  is: " + str(lineLength)
            if lineLength > 0:
                target_FILE2 = open(FINAL_OUTFILE, 'a')  ## target_FILE is OPENED
               
                target_FILE2.write(myLine)  ## no return written, just chars
                target_FILE2.write(" ")   ## so the words in the comments are separated when from 2 lines       
                print "-----------------------------!@@@@@@@@@@@@@@@@ here now!"
                print lname+" Month="+mon+" Day="+day+" "+yr+" "+adbcFinal+" Time="+tm+" Zone="+zn_offset+" Long="+longt+" Lat="+lat
                target_FILE2.close()
             
#------------------- writeOutFileContent() ----------------------------------
def writeOutFileContent(FINAL_OUTFILE,fname,lname,tdate,tm,tzone,tlat,tlong,tGender,city,country):
    target_FILE = open(FINAL_OUTFILE, 'a')  ## target_FILE is OPENED
    print NLINE + "inside writeOutFileContent:" + " lname: " + lname

    julianDay = "*"  
    whiteSpaceLoc = -1
    print whiteSpaceLoc

    try:
        whiteSpaceLoc = lname.index(' ')
    except ValueError:
        print "Could not find space in string"
        whiteSpaceLoc = -2
    except:
        print "Unexpected error:", sys.exc_info()[0]
        raise
    if whiteSpaceLoc > 0 :
        firstname, lastname  = lname.split(' ',1)
        print "---------------!!! new firstname is: " + firstname
        print "---------------!!! new lastname is: " + lastname
        fname = firstname
        lname = lastname

    print "-------------------wholename is: " + lname + "."
    print "-------------------------------------- whiteSpaceLoc = " + str(whiteSpaceLoc)
    
    target_FILE.write(NLINE)
    target_FILE.write("#A93:")    
    target_FILE.write(lname)
    target_FILE.write(cCOMMA)
    
    target_FILE.write(fname)
    target_FILE.write(cCOMMA)
    
    target_FILE.write(tGender)
    target_FILE.write(cCOMMA)
    
    target_FILE.write(tdate)
    target_FILE.write(cCOMMA)
    
    target_FILE.write(tm)
    target_FILE.write(cCOMMA)

    target_FILE.write(city)
    target_FILE.write(cCOMMA)

    target_FILE.write(country)
    target_FILE.write(NLINE)

    target_FILE.write("#B93:")
    target_FILE.write(julianDay)
    target_FILE.write(cCOMMA)
    
    target_FILE.write(tlat)
    target_FILE.write(cCOMMA)
    
    target_FILE.write(tlong)
    target_FILE.write(cCOMMA)
    
    target_FILE.write(tzone)
    target_FILE.write(cCOMMA)
    
    target_FILE.write("0")
    
    target_FILE.write(NLINE)   
    target_FILE.write("#COM:")
    #target_FILE.write(comments)
    #target_FILE.write(" ")  ## add a space so we can add onto the comments if needed
    whiteSpaceLoc = -1
    target_FILE.close()
   
#--------------------------------------------------------------



indirExists2 = os.path.exists(cInputDIR)
    
if indirExists2 is not True :
    pymsgbox.alert("ERROR!! Directory C:\\astin not there!", "Astrofiles Aborted")
    quit()



astrs.printFrontMatter()
astrs.chkFixDirs(global_OUTDIR)  ## this is where alerts are

locInFileList = astrs.getInFilePathList()
print "local file list: " + str(locInFileList)

mylength = locInFileList

for f in locInFileList:
    splitNfiFile(f)
    #target_FILE.close()
pymsgbox.alert("If program worked, new aaf files will be in C:\\astout", 'Astrofiles Finished')

print NLINE + "end of program"











