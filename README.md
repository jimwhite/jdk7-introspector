jdk7-introspector
=================

OpenJDK 7 Beans Introspector Backport

This is a quickie backport of java.beans.Introspector from OpenJDK
7 so that it can be used (I hope) with OpenJDK 6 (haven't tried
with 5).  The reason for this backport is that the locking code
for the global introspection cache prior to 7u2(b08) is seriously
broken.  Folks using dynamic languages such as Groovy that make
heavy use of reflection and introspection have been experiencing
deadlocks because of that buggy code.

	[http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7064279]

Build the jar with:

	ant jar
	
Then you can do this smoke test (which is the only testing I have
done - so BEWARE!):

	export JAVA_OPTS=-Xbootclasspath/p:build/jar/jdk7-introspector.jar 
	groovy -e "println java.beans.Introspector.JDK7_BACKPORTED"

Which will get a "No such property" exception if the standard
classes are in use, but will print:

	Thanks Jim! <james.paul.white@gmail.com>

If the bootclass path has been properly prepended with the
backported classes.

ABSOLUTELY NO WARRANTY EXPRESSED OR IMPLIED
USE THIS CODE AT YOUR OWN RISK!

If this doesn't work for you, I will answer emails when I can but
I cannot debug your system for you. I am however a consultant with
a fair bit of experience with concurrency issues and so if you need 
professional help then I may be able to help you.

Jim White <mailto:james.paul.white@gmail.com>
http://www.ifcx.org/
