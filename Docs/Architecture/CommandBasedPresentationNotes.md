# Command Based Presentation Notes

This doc is a set of notes for a live presentation on Command-Based programming.
A recording of this talk is available [here](https://drive.google.com/file/d/1ykFDfXVYk27aHlXYKTAqtj1U2T80Szdj/view?usp=sharing).
Note that some of the code shown in the video differs from the current version of this document, as this has been updated with newer best practices.

## What is Command-Based?

- Architecture built by WPILib
- Helps write organized, safe, and correct code

## Commands And Subsystems

### Subsystems

- Class that extends `SubsystemBase`
- Contain hardware
  - Motors
  - Pneumatics
  - Sensors
- Mutex, prevents multiple things from using the same hardware at the same time

### Commands

- Actions which use Subsystems
- Each Command uses one or more Subsystems as "requirements"
- Only one Command may use a Subsystem at a time

### How to make Commands

- The two main types of Commands we use are `run`s and `runOnce`s
  - `run` runs an action over and over until it is interrupted
  - `runOnce` runs once and then immediately stops

For example:

```Java
// Runs the intake rollers forever
Commands.run(() -> intakeSubsystem.spinRoller(), intakeSubsystem);

// Retracts the intake
// Note that the real hardware doesn't move instantly
// But we only need to set it once in code
Commands.runOnce(() -> intakeSubsystem.retract(), intakeSubsystem);
```

If these Commands are defined in a Subsystem file, we can make them even simpler by calling `run` and `runOnce` on the subsystem itself

```Java
// Inside IntakeSubsystem.java

// Notice how we don't need to define the requirements for these
// The subsystem does it implicitly
this.run(() -> spinRoller());

this.runOnce(() -> retract());
```

Anatomy of a Command declaration:

- Command Binding
- Lambda
- Requirements

### So we made a Command, now what?

- Commands get bound to Triggers
- Usually this is a button on the joystick

```Java
// In RobotContainer.java
// When the a button on the controller is pressed, run the rollers on the intake
controller.a().whenPressed(Commands.run(() -> intakeSubsystem.runRollers(), intakeSubsystem));
```

- This is somewhat wordy
- But Commands are objects, so we can pass them around!
- Let's make a method that returns the `RunCommand` instead of having to make it here

```Java
// In IntakeSubsystem.java
public CommandBase runRollersCommand() {
    // Note implicit requirements
    return this.run(() -> runRollers());
}
```

Then we can replace the call in RobotContainer with this new method

```Java
// In RobotContainer.java
// When the a button on the controller is pressed, run the rollers on the intake
controller.a().whenPressed(intakeSubsystem.runRollersCommand());
```

### Composing Commands

- These actions on their own are useful, but they only form simple, single actions
- Commands can be chained into Command groups

```Java
intakeSubsystem.extendCommand().andThen(intakeSubsystem.runRollersCommand())
```

- Several different "decorators" exist
  - `until()` will run and Command and then stop it when a condition is met
  - `andThen()` adds another Command after one finishes
  - `alongWith()` will run two or more Commands side by side until both finish
  - `raceWith()` will run two or more Commands side by side and interrupt all of them when any Command finishes
  - `withTimeout()` will interrupt a Command after a set amount of seconds

```Java
// An example of a more complex group

intakeSubsystem.extend().andThen(
    intakeSubsystem.runRollers().until(() ->
        intakeSubsystem.hasGamePiece()
    ),
    intakeSubsystem.retract()
)

// Another example

elevatorSubsystem.runToScoringHeight()
  .alongWith(
    grabberSubsystem.holdGamePiece()
  ).until(() -> elevatorSubsystem.isAtScoringHeight())
  .andThen(
    // Outtake game piece for 1 second
    grabberSubsystem.outtakeGamePiece().withTimeout(1.0)
  )
```
