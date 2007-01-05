set TermWareHome=c:\home\rssh\work\TermWare.2
set JavaCheckerHome=c:\home\rssh\work\TermWareEx\JavaChecker2
java -Dtermware.path="%JavaCheckerHome%\systems" -cp "%JavaCheckerHome%\jlib\TermWare2.jar";"%JavaCheckerHome%\jlib\JavaChecker2.jar";"%JavaCheckerHome%\jlib\TermWareJPP.jar"  ua.gradsoft.javachecker.Main %1 %2 %3 %4 %5 %6 %7 %8 %9