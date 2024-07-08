# Java

## “Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible” (Wikipedia)

The majority of our robot code is made in Java, and at least basic familiarity with the language is necessary to contribute to robot code.
APCSA, FTC, or the following resources are all great starting points to learn java, and you should be able to pick up a lot of it by working with the robot code.
If you already know another language you might be able to skip this step, but familiarizing yourself with the language is always valuable.
Feel free to look at the resources below, but none of them are required.

### Resources

These resources are good if you are totally new to programming, and cover Java from the very basics.

- [Code Academy](https://www.codecademy.com/learn/learn-java) - online course
- [Head First Java](https://www.rcsdk12.org/cms/lib/NY01001156/Centricity/Domain/4951/Head_First_Java_Second_Edition.pdf) - book

If you already have programming experience, but no Java experience, this might be useful:

- [Java for Python Programmers](https://runestone.academy/ns/books/published/java4python/index.html)

Lambdas and functional programming are important concepts to WPILibs Command Based system, which you will learn about in the next few articles.
Feel free to read this article now or wait until you get some of the context about what Command-Based programming is and how we use it.

- [WPILib docs - Functions as Data](https://docs.wpilib.org/en/stable/docs/software/basic-programming/functions-as-data.html)

The best way to learn to program is to do it and look up things as you need them.
The exercises below are a starting point (and required) to learn Java, but you are encouraged to continue to write programs in it to improve your understanding of the language.

### Exercises

- Write a program to compute the first 64 fibbonaci numbers and print them to the standard output.
  This program must complete in under 1 second.
  Feel free to use [this website](https://www.programiz.com/java-programming/online-compiler/) to write and run the code, or run it on your own machine.
- Complete 2 easy problems, or 1 medium problem on [leetcode](https://leetcode.com/problemset/all/) in java if you need more practice.

### Notes

- We use an auto-formatter to keep the code clean.
  To run it type `./gradlew spotlessApply` into the terminal when in a WPILib project.
  This should be done before you push code (if not more frequently!) to keep the codebase readable.
