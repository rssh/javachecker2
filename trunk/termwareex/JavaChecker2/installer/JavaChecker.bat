set TermWareHome=$INSTALL_PATH
set JAVACHECKER_HOME=$INSTALL_PATH
java -Djavachecker.home="%JAVACHECKER_HOME%" -cp "%TermWareHome%\jlib\TermWare2.jar";"%JAVACHECKER_HOME%\jlib\JavaChecker2.jar";"%JAVACHECKER_HOME%\jlib\TermWareJPP.jar"  ua.gradsoft.javachecker.Main %1 %2 %3 %4 %5 %6 %7 %8 %9     