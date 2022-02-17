INSERT INTO article (id, title, text) VALUES (1, 'Welcome Article!', 'Let''s go!');
INSERT INTO article (id, title, text) VALUES (2, 'First fact about JAVA language you probably did not know: ' ||
 'Java was called Oak at the beginning',
'
The original name for Java was Oak. It was eventually changed to Java by Sun''s marketing department when Sun lawyers ' ||
 'found that there was already a computer company registered as Oak. But a legend has it that Gosling and his gang ' ||
  'of programmers went out to the local cafe to discuss names and ended up naming it Java. There seems to be some ' ||
   'truth in this as the "0xCafeBabe" magic number in the class files was named after the Cafe where the Java team ' ||
    'used to go for coffee.');
INSERT INTO article (id, title, text) VALUES (3, 'Second fact about JAVA language you probably did not know: ' ||
 'Java was invented by accident',
'James Gosling was working at Sun Labs, around 1992. Gosling and his team was building a set-top box and started by ' ||
 '"cleaning up" C++ and wound up with a new language and runtime. Thus, Java or Oak came into being.');


INSERT INTO secret (id, val) VALUES (1, 'TOP SECRET: USE PREPARE STATEMENTS');