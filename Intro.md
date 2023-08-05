# Intro to FRC Programming

## Programming in FRC is very similar to programming in other contexts, but it's important to be familiar with the tools at our disposal and our architecture and practices

Our code runs on the roboRIO, a small linux computer that lives on the robot. Through the magic of wiring, we connect it to everything else on the robot that needs instructions, which includes hardware parts like motors, sensors, and cameras. The robot connects to a laptop running the driver station at home, or the field at tournaments over Wi-Fi to send and recieve data such as button presses, camera feeds, and enable signals.

However, it becomes very clumsy and difficult to interface with these parts directly. Instead of controlling exactly the bits sent out over our communication networks, we can call methods built into WPILib, the standard library for FRC, or vendor libraries made by the companies which manufacture motors and sensors for FRC.

WPIlib is an open source project that aims to raise the floor of what FRC teams can do with software. The project has a large number of features and is the main building block that our code is built on top of. It also has extensive documentation both for it's own API and for the concepts and math behind many of its features. We also use other libraries for specific tasks that WPILib doesn't cover. These include the Phoenix library made by CTRE, the manufacturer of many of our motor controlers, Photonvision, a library that helps us track targets with cameras, and Pathplanner, which enables us to make advanced auto paths. Many of these libraries will have their own articles here as well as their own documentation.

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
