TermWareHome="/home/rssh/work/TermWareEx/JavaChecker2"
JAVACHECKER_HOME="/home/rssh/work/TermWareEx/JavaChecker2"
java -Djavachecker.home="$JAVACHECKER_HOME" -classpath "$TermWareHome/lib/TermWare2.jar:$JAVACHECKER_HOME/lib/JavaChecker2.jar:$JAVACHECKER_HOME/lib/JavaChecker2Annotations.jar:$JAVACHECKER_HOME/lib/TermWareJPP.jar"  ua.gradsoft.javachecker.Main $*
