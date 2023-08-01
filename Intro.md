# Intro to FRC Programming

## Welcome! We’re happy to have you here. FRC programming is still built upon the programming principles you know, but may look a little different.

Instead of running programs that, for example, print calculations, run games, or create a website, our code is what gets the robot to do its thing. The roboRIO is a small computer that lives on the robot—it’s what we pass all of our code to and runs it. Through the magic of wiring, we connect it to everything else on the robot that needs instructions, which includes hardware parts like motors, sensors, cameras, and the like.

However, it becomes very clumsy and difficult to interface with these parts directly. Instead of having to deal with the minutiae of “how many volts do we need to send to this thing to make it work”, other folks out there have already done that for us. In addition, there are a lot of utilities that can be reused specifically for FRC. 

"WPILib contains a set of useful classes and subroutines for interfacing with various parts of the FRC control system (such as sensors, motor controllers, and the driver station), as well as an assortment of other utility functions." It is an open source project that aims to raise the floor of what FRC teams can do with software. The project has a little bit of everything and is the main building block that our code is built on top of. It also has extensive documentation.



### Resources

- **[WPILib docs](https://docs.wpilib.org/en/stable/index.html)**
- [WPILib zero to robot guide](https://docs.wpilib.org/en/stable/docs/zero-to-robot/introduction.html)
- [WPILib repo](https://github.com/wpilibsuite/allwpilib)
- [Installation guide](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-2/wpilib-setup.html)
- [Glossary of jargon](https://docs.wpilib.org/en/stable/docs/software/frc-glossary.html)

### Examples

- After installing WPILib, go to the command list by clicking on the WPILib logo in the upper right corner, then pressing "create a new project" then "examples"
- Look at our old codebase!

### Exercises

- Download and install WPILib
- Create a basic set of kitbot drive code from the timed robot skeleton

### Notes

- See the [Command-based](CommandBased.md) article for more information about how we architect our code using WPILib
- A lot of what WPILib does is "under the hood" to let us focus on high-level code rather than low-level hardware interface
