# Kitbot Code Tutorial

## Intro

### What is a kitbot?

![AM14U4 or Kitbot Chassis](am-14U4.jpg)

The kitbot is a drivetrain which comes in the Kit of Parts provided by FIRST as part of registration.
It is a typical of example of a type of drivetrain known as a differential drive.
That name comes from the fact that the amount it turns is equal to the difference between the speed of the left and right sides.
This type of drive is also known as skid-steer or tank drive.

### Why kitbot?

We do not use the kitbot or other differential drive for our robots in competition.
Instead we use a much more versatile swerve drive.
However the code for a swerve drive is much more complex than a tank drive, which provides a good exercise to introduce you to programming FRC robots.
The kitbot has somewhat standard dimensions, gearboxes, wheels, and other physical characteristics so it makes for a consistent mechanism to start with.

### So what do we need to program?

Inputs/Outputs:

- A command that takes in velocity and rotational velocity, and outputs the correct wheel speeds to meet that.

Electronics:

- Each side of the chassis has two Falcon 500 motors, which will work together to power the left and right sides of the chassis.
In code this will look like a total of 4 Talon FX motor controllers, since thats the component we can talk to.

## Code Walkthrough

If you think you can figure out how to program this on your own, go for it!
However, check your work against this to see if you did it the same way.
If you have any questions about why we did it this way, ask a lead.

To start, open up WPILib vscode and create a new project.
![A screenshot of wpilib vscode showing where to press to create a new project](image.png)
Select `Template`, `Java`, `Command Robot Skeleton (Advanced)` as the template for your project.
Select whichever folder you want and title the project something along the lines of "Kitbot Example".
Set the team to 8033.
Make sure to enable desktop support, since it's necessary for simulation work which we will do on this project down the line.
![A screenshot of the wpilib vscode project creator with the proper settings](image-1.png)
Once you've created your project, open the src\main\java\frc\robot folder.
In this folder, create a new folder "Subsystems".
![A screenshot of the file tree of the project](image-2.png)
