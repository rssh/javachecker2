TermWareHome="$INSTALL_PATH"
JAVACHECKER_HOME="$INSTALL_PATH"
java -Djavachecker.home="$$JAVACHECKER_HOME/systems" -cp "$$TermWareHome/jlib/TermWare2.jar";"$$JavaCheckerHome/jlib/JavaChecker2.jar";"$$JavaCheckerHome/jlib/TermWareJPP.jar"  ua.gradsoft.javachecker.Main $$*