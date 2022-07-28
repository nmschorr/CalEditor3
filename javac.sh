
# DOS set CLASSPATH='C:/scal;.;C:/scal/commons-io/commons-io-2.1.jar;C:\Program Files\Java\jdk-11.0.12\lib\jrt-fs.jar;'
# DOS set CLASSPATH='C:/scal;.;C:/scal/commons-io/commons-io-2.1.jar;C:\Program Files\Java\jdk-11.0.12\lib\jrt-fs.jar;'
# DOS set CLASSPATH='C:/scal;.;C:/scal/commons-io/commons-io-2.1.jar;C:\Program Files\Java\jdk-11.0.12\lib\jrt-fs.jar;'
# must use -Dfile.encoding=UTF-8 
#  // echo %CLASSPATH%

javac SCALperson.java

# works great just like this, but must manually set classpath above. Don't use jar. 
## use cmd dos
## do first:     set CLASSPATH='C:/scal;.;C:/scal/commons-io/commons-io-2.1.jar;C:\Program Files\Java\jdk-11.0.12\lib\jrt-fs.jar;
set CLASSPATH='C:/scal;.;C:/scal/commons-io/commons-io-2.1.jar;C:\Program Files\Java\jdk-11.0.12\lib\jrt-fs.jar;

java -Dfile.encoding=UTF-8 SCALperson

java -Dfile.encoding=UTF-8 SCALperson



