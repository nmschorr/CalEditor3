#!/usr/bin/bash
# July 29 2022
# 1) use cygwin
# 2) make sure all 3 jars are in this dir
# 3) run this sh script

#java -cp "C:/scal/SCALperson-1.0.jar;C:/scal/commons-io-2.1.jar;C:/scal/jrt-fs.jar;" -Dfile.encoding=UTF-8 SCALperson 
#java -cp "C:/scal/SCALperson-1.0.jar;C:/scal/commons-io-2.1.jar;C:/scal/jrt-fs.jar;" -Dfile.encoding=UTF-8 SCALperson 

java -cp "C:/scal/SCALperson-1.0.jar;C:/scal/commons-io-2.1.jar;C:/scal/jrt-fs.jar;" -Dfile.encoding=UTF-8 SCALperson 

# win setup shortcut:  'C:\cygwin64\bin\run.exe /usr/bin/bash.exe /cygdrive/c/scal/scal.sh'
