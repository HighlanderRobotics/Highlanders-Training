# Command Based

## Command Based is a way of structuring our code to make it easy to do complicated tasks, and write control logic in a concise way

Command based programming revolves around three concepts: **Subsystems**, **Commands**, and **Triggers**.

A Subsystem is a set of hardware that forms one system on our robot, like the drivetrain, elevator, arm, or intake.
Each subsystem contains some associated hardware (motors, pistons, sensors, etc.) They are the "nouns" of our robot, what it is.
Each Subsystem is generally made to contain as broad of a set of hardware that will always operate as a unit as possible.

Commands are the "verbs" of the robt, or what our robot does.
Each Subsystem can be used by one Command at the same time, but Commands may use many Subsystems.
Commands can be composed together, so the `LineUp`, `Extend`, and `Outake` Commands might be put together to make a `Score` Command.
Because each Subsystem can only be used by one Command at once, we are safe from multiple pieces of code trying to command the same motor to different speeds, for example.

Subsystems are ways to organize resources that can be used by one Command at a time.

Some hardware might not be stored in a Subsystem if multiple things can/should use it.
For example, a vision setup can be read from by many things, and might not need to be locked by Commands.
Therefore, it might not be stored in a Subsystem.

On the other hand, a roller that is driven by a motor can only go at one speed at a time.
Therefore, we would wrap it in a Subsystem so that only one Command can use it at once.

A Trigger is something which can start a Command.
The classic form of this is a button on the driver's controller.
Another common type is one which checks for when the robot enables.
One non-obvious Trigger we used in 2024 was one which checked when we had detected a game piece in the robot, which we used to flash our LEDs and vibrate the driver controller.
Triggers can be made of any function that returns a boolean which makes them very powerful.
Some large Commands are better represented by several Commands and some Triggers!

### Resources

- [WPILib intro to functional programming](https://docs.wpilib.org/en/stable/docs/software/basic-programming/functions-as-data.html).
  Read through this article on lambda expressions and functional programming if you haven't already.
- [WPILib docs](https://docs.wpilib.org/en/stable/docs/software/commandbased/index.html).
  Read through these docs until you finish "Organizing Command-Based Robot Projects"
  OR watch [this video](https://drive.google.com/file/d/1ykFDfXVYk27aHlXYKTAqtj1U2T80Szdj/view?usp=drive_link).
  Presentation notes for the video are [here](CommandBasedPresentationNotes.md).
  If you watch the video, it is recommended to also read the [Subsystems](https://docs.wpilib.org/en/stable/docs/software/commandbased/subsystems.html), [Binding Commands to Triggers](https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html), and [Organizing Command-Based Robot Projects](https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#) for addition details on using Command-Based.
  
  The important segment of all of this to remember is:
  > Commands represent actions the robot can take. Commands run when scheduled, until they are interrupted or their end condition is met. Commands are very recursively composable: commands can be composed to accomplish more-complicated tasks. See Commands for more info.
  >
  > Subsystems represent independently-controlled collections of robot hardware (such as motor controllers, sensors, pneumatic actuators, etc.) that operate together. Subsystems back the resource-management system of command-based: only one command can use a given subsystem at the same time. Subsystems allow users to “hide” the internal complexity of their actual hardware from the rest of their code - this both simplifies the rest of the robot code, and allows changes to the internal details of a subsystem’s hardware without also changing the rest of the robot code.

### Examples

- Previous season code
- [WPILib built in examples](https://docs.wpilib.org/en/stable/docs/software/examples-tutorials/wpilib-examples.html#command-based-examples)

### Exercises

- Make basic KitBot code using the Command-Based skeleton. You can follow [this](KitbotExampleWalkthrough.md) tutorial.

### Notes

- We prefer making simple Commands with Command factories, or methods in a subsystem that return a Command.
  These methods should be simple interactions like `setTargetExtensionMeters()` or `extendIntake()`.
  Then you can use decorators as described [here](https://docs.wpilib.org/en/stable/docs/software/commandbased/command-compositions.html) to compose the basic Commands into more complex sequences.
  Generally we make these compositions in `Robot` but you can also make single-Subsystem compositions within that Subsystem.
  See our code from previous years for examples of this pattern, or talk to a software lead.
