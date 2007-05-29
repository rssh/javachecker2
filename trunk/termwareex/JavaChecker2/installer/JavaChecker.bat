set TermWareHome=$INSTALL_PATH
set JAVACHECKER_HOME=$INSTALL_PATH
java -Djavachecker.home="%JAVACHECKER_HOME%" -cp "%TermWareHome%\lib\TermWare2.jar";"%JAVACHECKER_HOME%\lib\JavaChecker2.jar";"%JAVACHECKER_HOME\lib\JavaChecker2Annotations.jar";"%JAVACHECKER_HOME%\lib\TermWareJPP.jar"  ua.gradsoft.javachecker.Main %1 %2 %3 %4 %5 %6 %7 %8 %9     
