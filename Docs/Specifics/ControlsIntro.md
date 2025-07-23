# Intro to Controls

## Going from a goal for a mechanism ("Extend the elevator to 24 inches out" or "Have the drivetrain follow a path in autonomous") to the actual voltage sent to each motor is a non-trivial task

Luckily, we have many tools at our disposal to control motors and mechanisms in a consistent, quick, and precise way

Start by reading through the [WPILib docs Advanced Controls Introduction](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/control-system-basics.html) (you should read all the articles under Advanced Controls Introduction, not just Control System Basics).
You don't need to fully digest the math or jargon, but try to be familiar with the concepts behind PID and Feedforward, and understand the tradeoffs of each.
You should also tune the interactive models in the pages that have them.

If you're still confused after reading through those articles, that's okay! The articles are somewhat dense.
It's worth coming back to these explanations again later once you've digested it a little bit.
Kevin also has a spiel about PID controllers _which we should record and put here_.

Choosing a control strategy is an important takeaway from these articles.
Generally we have a pretty direct tradeoff between time/effort spent on controls and precision in our output (as well as how fast the mechanism gets to that precise output!)
Loosely speaking, there are 3 'levels' of control that we tend to use for our mechanisms.

1. Is simple feedforward control.
   Really, this is only feedforward in the loosest sense, where we set a DutyCycle or Voltage to the motor.
   This sort of control doesn't set the exact speed of the output, and is really only useful on mechanisms that just need some rotation to work, but not anything precise.
   You'll usually see this on intakes or routing wheels where we just want to pull a game piece into or through the robot.
   The [Intake Subsystem](https://github.com/HighlanderRobotics/Charged-Up/blob/main/src/main/java/frc/robot/subsystems/IntakeSubsystem.java) from 2023 is an example of this.

2. Is simple feedback control.
   We tend to use this on mechanisms that we might want to use sensible units for (like rotations per minute, or degrees) instead of an arbitrary output but don't need huge amounts of precision or are so overpowered compared to the forces on them that we can ignore outside forces.
   Often we use this by calling a TalonFX's Position or Velocity control modes.
   [The angle motor on a swerve module](https://github.com/HighlanderRobotics/Charged-Up/blob/main/src/main/java/frc/robot/SwerveModule.java) is an example of this sort of control, and works because the Falcon 500 powering the module angle is so much stronger than the friction of the wheel with the ground.

3. Is combined feedforward and feedback control.
   This is ideal for most situations where we desire precise control of a mechanism, and should be used on all primary mechanisms of a robot.
   It tends to require more effort to tune and model than the previous levels, but is the correct way to control mechanisms.
   The [Elevator Subsystem](https://github.com/HighlanderRobotics/Charged-Up/blob/main/src/main/java/frc/robot/subsystems/ElevatorSubsystem.java) from 2023 is an example of this, specifically the `updatePID()` method which adds the results of a PID controller calculation with a feedforward controller calculation.
   Notice the use of a WPILib feedforward class here.
   WPILib provides several classes that model common mechanisms.
   [This article](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/feedforward.html#feedforward-control-in-wpilib) goes over the classes in more detail.
   Using these means we can avoid a lot of the math involved in feedforward control, but if you are interested in the math you can check out [this book](https://file.tavsys.net/control/controls-engineering-in-frc.pdf), although that is not required for the level of controls we are working with in FRC.

Ideally, we are using the 3rd level of control (or better!) for every mechanism that requires precision on the robot.
To tune this, we are ideally using CAD models and real-life system identification as much as possible.
However, in the past we have relied on hand-tuning values for our control loops.
In general we would like to move away from the hand-tuned approach, as hand tuning is time consuming and results in worse gains than ones found from sysid or calculated from CAD.

### Examples

- [WPILib PID controller implementation](https://github.com/wpilibsuite/allwpilib/blob/01490fc77b3543f80c47252d4bb1f44eb0573006/wpimath/src/main/java/edu/wpi/first/math/controller/PIDController.java)

### Exercises

- Complete all interactive WPILib tutorials and demonstrate tuning one of them to a lead or mentor
