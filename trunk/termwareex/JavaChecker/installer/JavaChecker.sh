TermWareHome="$INSTALL_PATH"
JavaCheckerHome="$INSTALL_PATH"
java -Dtermware.path="$$JavaCheckerHome/systems" -cp "$$TermWareHome/jlib/TermWare.jar";"$$JavaCheckerHome/jlib/JavaChecker.jar"  ua.kiev.gradsoft.JavaChecker.Main $$*