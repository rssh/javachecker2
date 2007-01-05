set JPE_HOME=C:\home\rssh\work\JCBO\JPE
set TERMWARE_HOME=C:\home\rssh\work\TermWare.2

rem set JAVA_OPTS=-Xms128m -Xmx512m

rem java -cp "%JPE_HOME%\lib\TermWare2.jar";"%JPE_HOME%\lib\TermWareJPP.jar";"%JPE_HOME%\lib\JavaChecker2.jar";"%JPE_HOME%\dist\JPE.jar"  ua.gradsoft.jpe.Main --input-dir "%JPE_HOME%\src" --output-dir "%JPE_HOME%\output\jpe-out-1" --jpehome "%JPE_HOME%" --transformation ID


java %JAVA_OPTS% -cp "%JPE_HOME%\lib\TermWare2.jar";"%JPE_HOME%\lib\TermWareJPP.jar";"%JPE_HOME%\lib\JavaChecker2.jar";"%JPE_HOME%\dist\JPE.jar"  ua.gradsoft.jpe.Main --input-dir "%TERMWARE_HOME%\jsrc-core" --output-dir "%JPE_HOME%\output\jpe-out-2" --jpehome "%JPE_HOME%" --transformation ID