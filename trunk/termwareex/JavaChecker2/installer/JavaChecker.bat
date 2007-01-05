set TermWareHome=$INSTALL_PATH
set JavaCheckerHome=$INSTALL_PATH
java -Dtermware.path="%JavaCheckerHome%\systems" -cp "%TermWareHome%\jlib\TermWare.jar";"%JavaCheckerHome%\jlib\JavaChecker.jar"  ua.gradsoft.javachecker.Main %1 %2 %3 %4 %5 %6 %7 %8 %9