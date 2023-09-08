# Command Based Presentation Notes

This doc is a set of notes for a live presentation on Command-Based programming.
A recording should be posted once the presentation is completed.

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

- The two main types of Commands we use are `RunCommand`s and `InstantCommand`s
  - `RunCommand`s run an action over and over until they are interrupted
  - `InstantCommand`s run once and then immediately stop

For example:

```Java
// Runs the intake rollers forever
new RunCommand(() -> intakeSubsystem.spinRoller(), intakeSubsystem);

// Retracts the intake
// Note that the real hardware doesn't move instantly
// But we only need to set it once in code
new InstantCommand(() -> intakeSubsystem.retract(), intakeSubsystem);
```

Anatomy of a Command declaration:

- Command Object
- Lambda
- Requirements

### So we made a Command, now what?

- Commands get bound to Triggers
- Usually this is a button on the joystick

```Java
// In RobotContainer.java
// When the a button on the controller is pressed, run the rollers on the intake
controller.a().whenPressed(new RunCommand(() -> intakeSubsystem.runRollers(), intakeSubsystem));
```

- This is somewhat wordy
- But Commands are objects, so we can pass them around!
- Let's make a method that returns the `RunCommand` instead of having to make it here

```Java
// In IntakeSubsystem.java
public CommandBase runRollersCommand() {
    return new RunCommand(() -> intakeSubsystem.runRollers(), intakeSubsystem);
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

```Java
// An example of a more complex group

intakeSubsystem.extendCommand().andThen(
    intakeSubsystem.runRollersCommand().until(() ->
        intakeSubsystem.hasGamePiece()
    ),
    intakeSubsystem.retract()
)
```
