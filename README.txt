Assumptions:
- State updates contains only one values-entry (if more, eg. values:{"foo":"bar", "bar":"foo"}, only the "foo":"bar" will be processed. Logs this as a warning if it occurs.
- All players who update their state is already added to the database


Instructions:
Compile
javac -cp ".;./jars/*" PlayerStateApp.java

Execute
java -cp ".;./jars/*" PlayerStateApp <filename>