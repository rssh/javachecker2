TermWareHome="$INSTALL_PATH"
JAVACHECKER_HOME="$INSTALL_PATH"
java -Djavachecker.home="$JAVACHECKER_HOME" -cp "$TermWareHome/lib/TermWare-$termware_version.jar:$JAVACHECKER_HOME/lib/JavaChecker-$javachecker_version.jar:$JAVACHECKER_HOME/lib/JavaChecker2Annotations-$javachecker_version.jar:$JAVACHECKER_HOME/lib/TermWareJPP-$termwarejpp_version.jar"  ua.gradsoft.javachecker.Main $*
