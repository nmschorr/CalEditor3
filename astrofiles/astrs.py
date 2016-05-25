'''
Created on Jan 31, 2016

@author: user
'''

#Created on Nov 22, 2015
#@author: Nancy Schorr

#  translate the old "nfi"-files, that I have (they are basically text-files with a certain structure, as far as I understand... one file consists of a lot of astrological dates, each in one line... each line would be one aaf-file) into aaf data.
## old is nfi, new is aaf

PYTHONDONTWRITEBYTECODE = True   #prevents bytecode .pyc files from being created
import glob
import os
#import sys
#import re
#import shutil
import datetime
import pymsgbox

if __name__ == '__main__':   pass

__all__ = ["printFrontMatter"]



#--------------constants--------------------------------------
ccDirSEP ='\\'
ccNLINE = "\n"
ccSTAR = "*"
ccInputDIR ='C:\\astin'      ## must be there
ccOutDIR =  'C:\\astout'      ## where you are sorting to, created if not there
ccAaf_END = '.aaf'
ccNFI_END = '.nfi'
#--------------globals--------------------------------------
#ggDateStr=datetime.datetime.now().strftime('%Y%m%d%H%M%S')
#ggOutdirDone =  ccOutDIR +  ccDirSEP + 'done' + str(ggDateStr)

#-------------------------------------------------

def printFrontMatter():
    print ccNLINE + "Starting program to translate .nfi to .aaf files"
    print "    Program concept by Axel Becker. Code by Nancy Schorr"

def setGlobalEachFile(locFileList):
    global ggEachFileList; 
    ggEachFileList = locFileList;
    
def getGlobalEachFile():
    return ggEachFileList;

def prtExitEarlyStatements():
    print ccNLINE + "ERROR!! nfi files not there. Program exiting early. "
    print '  Please put C:\\astin\\filename.nfi there and start over.'
    print '  Can be any filename as long as it ends with \".nfi\"'
    pymsgbox.alert("ERROR!! No files in C:\\astin", "Astrofiles Aborted")

#2------------------- chkFixDirs() ----------------------------------
### check both dirs present; if inDirToChg not there, stop & give error  
### if cOutDIR isn't there, create it
 
def chkFixDirs(loc_outdir):
    indirExists = os.path.exists(ccInputDIR)
    outdirExists = os.path.exists(ccOutDIR)
    
    if indirExists is True :
        print   ccNLINE +"inputdir pathexists. Continuing..."  
         
    if indirExists is True :
        if outdirExists is True :
            print ccNLINE + "outdirExists YES path exists boolean value: " + str(outdirExists)
        
        if outdirExists is False :
            print ccNLINE + "outdirExists NO not there : " + str(outdirExists)+  "Creating new dir: " + ccOutDIR
            os.makedirs(ccOutDIR)
    
        os.makedirs(loc_outdir)
        
    ckNfi(ccInputDIR)

#3--------------------------------------------------
def getInFilePathList():
    print    ccNLINE +"inside getInFilePathList(). storing vals to array"
    eachFileList = []
    eachFilePathList = []
    
    for inputFile in os.listdir(ccInputDIR):
        if inputFile.endswith(".nfi"):
            eachFileList.append(inputFile);
            eachFilePathList.append(ccInputDIR + ccDirSEP + inputFile)
     
    setGlobalEachFile(eachFileList)
    print ccNLINE + "  now eachFileList: "
    for i in ggEachFileList :
        print i;
    print ccNLINE + "  now eachFilePathList: "
    for ii in eachFilePathList :
        print ii;
    print "  End of getInFilePathList()" + ccNLINE
    print "-------------------!!! entire eachFilePathList : " + str(eachFilePathList)
    return eachFileList
    
#--------------------------------------------------

def formatTime(tim):
    p1 =  tim[0:2]
    p2 =  tim[2:4]
    p3 =  tim[4:6]   
    print "rawtime: " + tim
    print "-----------time parts: "
    newtime = p1 + ":" + p2 + ":" + p3
    print newtime   
    return newtime

#--------------------------------------------------
### not used?

def ckNfi(input_dir):   
    loc_nfi_files = input_dir + ccDirSEP + ccSTAR + ccNFI_END
    #### check if there is at least one filename.nfi file
    if glob.glob(loc_nfi_files):
        print ccNLINE + "YES nfi files are there!" + ccNLINE
    else:
        prtExitEarlyStatements()
        quit();
                
    myfiles = glob.iglob(loc_nfi_files)   
    print ccNLINE + "Here are the nfi files to be translated to type aaf: "
   
    for f in myfiles :
        print "    " + f
   
#--------------------------------------------------
    
 
def addCommentLn(targt, mLine):
        print "---------------------------------######################---------------!!!!!!here is empty comments extra myLine:"
        print mLine;
        lineLen = len(mLine)
        print "lineLen is: " + str(lineLen)
        if lineLen > 0:
            targt.write(mLine)  ## no return written, just chars
            targt.write(" ")   ## so the words in the comments are separated when from 2 lines       
        print "-----------------------------!@@@@@@@@@@@@@@@@ here now!"
  
#------------------------------------------------
def fixadbc(adbc_loc):
    adbc_fin = adbc_loc
    if adbc_loc == 'B' :
        adbc_fin = 'BC'
    return adbc_fin
    
#------------------------------------------------
    
def fixlat(alat):
    p1 =  alat[0:2]
    p2 =  alat[2:4]
    p3 =  alat[4:5]  
    newlat = p1 + p3 + p2 
    print "p1: " +  p1
    print "p2: " +  p2
    print "p3: " +  p3
    print "input lat: " + alat
    print "newlat = " + newlat
    print
    return newlat
#------------------------------------------------

def fixlongt(longt_org):
    p1 =  longt_org[0:3]
    p2 =  longt_org[3:5]
    p3 =  longt_org[5:6]  
    newlongt = p1 + p3 + p2 
    print "p1: " +  p1
    print "p2: " +  p2
    print "p3: " +  p3
    print "input longt_org: " + longt_org
    print "newlongt = " + newlongt
    print
    return newlongt
#------------------------------------------------

def fixzn_offset(zn):
    p1 =  zn[0:2]
    p2 =  zn[2:4]
    p3 =  zn[4:5]  
    newzn = p1 + p3 + p2 
    print "p1: " +  p1
    print "p2: " +  p2
    print "p3: " +  p3
    print "input zone_offset_orig: " + zn
    print "newzone_offset = " + newzn
    print
    #if (newzn == '00E28'):
    #    newzn = '01E00'    
    return newzn




