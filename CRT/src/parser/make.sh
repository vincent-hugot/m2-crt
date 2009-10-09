rm Parser.java -f
jjtree *.jjt && javacc *.jj && javac *.java && java Test < testinput.txt