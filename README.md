# Assymetrik-Business-Card

How to run:

The file that is checked for the unparsed business card data should be in a .txt file. I have provided a sample file, input.txt, that can be used to run the program. This file must be passed as the first command line argument when running the program, as shown in the following instructions.

1. navigate to an empty folder
2. run *git init*
3. run *git pull https://github.com/Malekagr/Assymetrik-Business-Card/*
4. run *javac BusinessCardParser.java*
5. run *java BusinessCardParser input.txt*

The parsed data will then be printed on the command line.

Explanation of files:
1. BusinessCardParser.java is the main file
2. ContactInfo.java is where most of the work is done
3. emailDictionary.txt, nameDictionary.txt, phoneDictionary.txt are files that are used to check for specific characters/words.
4. input.txt is an example file thatcontains the unparsed business card data. 
