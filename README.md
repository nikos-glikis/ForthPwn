Introduction
============

This code tries to make automate the whois request for a .gr domain, to https://grweb.ics.forth.gr/public/whois.jsp?lang=en by cracking the given captcha.

The way that does that is to clean up the image using native java and some simple processing, and then to use google tesseract OCR library, to crack the cleaned up image.

This has about 40% success. The program retries until the request succeeds. On successful run the whois information is printed in the console. 

How
===

For a detailed outline of the method used see the blogpost here: http://nikos.glikis.net/hacks/the-forthpwn/ or see the comments in Main.java.

Compile
=======

The repository is ~160MB. This is because I include the libraries used. I have not used maven, so that the deployment can be easier by users that don't know maven.

Also, the whole Intellij project is uploaded, including project dir .idea, so that is it easier to run and edit. Just download Intellij (free) and open the project folder.

If you want to build and run manually commands are included below: 

Linux:

    javac -cp .:lib/lib/commons-io-2.4.jar:lib/jna-3.3.0.jar:lib/jna-3.3.0-platform.jar:lib/tess4j/dist/tess4j-3.0.jar:lib/jsoup-1.8.3.jar:out/production/captchalib/lib/ghost4j-1.0.0.jar:lib/lib/hamcrest-core-1.3.jar:lib/lib/itext-2.1.7.jar:lib/lib/jai_imageio.jar:lib/lib/jna.jar:lib/lib/jul-to-slf4j-1.7.13.jar:lib/lib/junit-4.12.jar:lib/lib/lept4j-1.0.1.jar:lib/lib/log4j-1.2.17.jar:lib/lib/logback-classic-1.1.3.jar:lib/lib/logback-core-1.1.3.jar:lib/lib/rococoa-core-0.5.jar:lib/lib/slf4j-api-1.7.13.jar:lib/lib/jna.jar:lib/lib/xmlgraphics-commons-1.5.jar -d out/production/captcha src/com/tools/forth/*.java 

Windows:

    javac -cp .;lib/lib/commons-io-2.4.jar;lib/jna-3.3.0.jar;lib/jna-3.3.0-platform.jar;lib/tess4j/dist/tess4j-3.0.jar;lib/jsoup-1.8.3.jar;out/production/captchalib/lib/ghost4j-1.0.0.jar;lib/lib/hamcrest-core-1.3.jar;lib/lib/itext-2.1.7.jar;lib/lib/jai_imageio.jar;lib/lib/jna.jar;lib/lib/jul-to-slf4j-1.7.13.jar;lib/lib/junit-4.12.jar;lib/lib/lept4j-1.0.1.jar;lib/lib/log4j-1.2.17.jar;lib/lib/logback-classic-1.1.3.jar;lib/lib/logback-core-1.1.3.jar;lib/lib/rococoa-core-0.5.jar;lib/lib/slf4j-api-1.7.13.jar;lib/lib/jna.jar;lib/lib/xmlgraphics-commons-1.5.jar -d out/production/captcha src/com/tools/forth/*.java 

    
Run:
====

The first argument is the domain you want to lookup.

Linux:

    java -cp .:lib/lib/commons-io-2.4.jar:lib/jna-3.3.0.jar:lib/jna-3.3.0-platform.jar:lib/tess4j/dist/tess4j-3.0.jar:lib/jsoup-1.8.3.jar:out/production/captchalib/lib/ghost4j-1.0.0.jar:lib/lib/hamcrest-core-1.3.jar:lib/lib/itext-2.1.7.jar:lib/lib/jai_imageio.jar:lib/lib/jna.jar:lib/lib/jul-to-slf4j-1.7.13.jar:lib/lib/junit-4.12.jar:lib/lib/lept4j-1.0.1.jar:lib/lib/log4j-1.2.17.jar:lib/lib/logback-classic-1.1.3.jar:lib/lib/logback-core-1.1.3.jar:lib/lib/rococoa-core-0.5.jar:lib/lib/slf4j-api-1.7.13.jar:lib/lib/jna.jar:lib/lib/xmlgraphics-commons-1.5.jar:out/production/captcha com.tools.forth.Main enikos.gr
        
Windows

    java -cp .;lib/lib/commons-io-2.4.jar;lib/jna-3.3.0.jar;lib/jna-3.3.0-platform.jar;lib/tess4j/dist/tess4j-3.0.jar;lib/jsoup-1.8.3.jar;out/production/captchalib/lib/ghost4j-1.0.0.jar;lib/lib/hamcrest-core-1.3.jar;lib/lib/itext-2.1.7.jar;lib/lib/jai_imageio.jar;lib/lib/jna.jar;lib/lib/jul-to-slf4j-1.7.13.jar;lib/lib/junit-4.12.jar;lib/lib/lept4j-1.0.1.jar;lib/lib/log4j-1.2.17.jar;lib/lib/logback-classic-1.1.3.jar;lib/lib/logback-core-1.1.3.jar;lib/lib/rococoa-core-0.5.jar;lib/lib/slf4j-api-1.7.13.jar;lib/lib/jna.jar;lib/lib/xmlgraphics-commons-1.5.jar;out/production/captcha com.tools.forth.Main enikos.gr
    