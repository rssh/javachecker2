set TermWareHome=c:\home\rssh\work\TermWare
set JavaCheckerHome=c:\home\rssh\work\TermWareEx\JavaChecker
java -Dtermware.path="%JavaCheckerHome%\systems" -cp "%TermWareHome%\jlib\TermWare.jar";"%JavaCheckerHome%\jlib\JavaChecker.jar"  ua.kiev.gradsoft.JavaChecker.Main %1 %2 %3 %4 %5 %6 %7 %8 %9