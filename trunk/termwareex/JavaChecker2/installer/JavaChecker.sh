TermWareHome="$INSTALL_PATH"
JAVACHECKER_HOME="$INSTALL_PATH"
java -Djavachecker.home="$JAVACHECKER_HOME" -cp "$TermWareHome/lib/TermWare2.jar:$JAVACHECKER_HOME/lib/JavaChecker2.jar:$JAVACHECKER_HOME/lib/JavaChecker2Annotations.jar:$JAVACHECKER_HOME/lib/TermWareJPP.jar"  ua.gradsoft.javachecker.Main $*
