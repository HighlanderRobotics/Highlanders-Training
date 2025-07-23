# Command Based

## Command Based is a way of structuring our code to make it easy to do complicated tasks, and write control logic in a concise way

Command based programming revolves around two concepts: Subsystems and Commands.
A Subsystem is a set of hardware that forms one system on our robot, like the drivetrain, elevator, arm, or intake.
Each subsystem contains some associated hardware (motors, pistons, sensors, etc.) They are the "nouns" of our robot, what it is.
Commands are the "verbs" of the robt, or what our robot does.
Each Subsystem can be used by one Command at the same time, but Commands may use many Subsystems.
Commands can be composed together, so the `Line Up`, `Extend`, and `Outake` Commands might be put together to make a `Score` Command.
Because each Subsystem can only be used by one Command at once, we are safe from multiple pieces of code trying to command the same motor to different speeds, for example.

### Resources

- [WPILib docs](https://docs.wpilib.org/en/stable/docs/software/commandbased/index.html).
  Read through these docs until you finish "The Command Scheduler"
  OR watch [this video](https://drive.google.com/file/d/1ykFDfXVYk27aHlXYKTAqtj1U2T80Szdj/view?usp=drive_link).
  Presentation notes for the video are [here](CommandBasedPresentationNotes.md)
  The important segment to remember is:
  > Commands represent actions the robot can take. Commands run when scheduled, until they are interrupted or their end condition is met. Commands are very recursively composable: commands can be composed to accomplish more-complicated tasks. See Commands for more info.
  >
  > Subsystems represent independently-controlled collections of robot hardware (such as motor controllers, sensors, pneumatic actuators, etc.) that operate together. Subsystems back the resource-management system of command-based: only one command can use a given subsystem at the same time. Subsystems allow users to “hide” the internal complexity of their actual hardware from the rest of their code - this both simplifies the rest of the robot code, and allows changes to the internal details of a subsystem’s hardware without also changing the rest of the robot code.
- [WPILib intro to functional programming](https://docs.wpilib.org/en/stable/docs/software/basic-programming/functions-as-data.html).
  Read through this article on lambda expressions and functional programming.

### Examples

- Previous season code
- [WPILib built in examples](https://docs.wpilib.org/en/stable/docs/software/examples-tutorials/wpilib-examples.html#command-based-examples)

### Exercises

- Make basic KitBot code using the Command-Based skeleton. You can follow [this](KitbotExampleWalkthrough.md) tutorial.

### Notes

- We prefer making simple Commands with Command factories, or methods in a subsystem that return a Command.
  These methods should be simple interactions like `setTargetExtensionInches()` or `extendIntake()`.
  Then you can use decorators as described [here](https://docs.wpilib.org/en/stable/docs/software/commandbased/command-compositions.html) to compose the basic Commands into more complex sequences.
  Generally we make these compositions in `RobotContainer` but you can also make single-Subsystem compositions within that Subsystem.
  See our code from previous years for examples of this pattern, or talk to a software lead.
- In our 2023 code we started using a pattern called the `SuperstructureSubsystem`.
  This pattern involves creating a Subsystem that has references to most other Subsystems to make Command factory methods with multiple Subsystems.
  This pattern doesn't really make sense on inspection though, since the `SuperstructureSubsystem` doesn't actually lock any hardware from multiple-access and you can just make the composed Commands in `RobotContainer` instead.
  In the future, just make multiple-Subsystem Command factories in `RobotContainer`.
