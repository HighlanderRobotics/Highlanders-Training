# PID Control

## Often we want to be able to control the speed or position of a motor. PID is one way to take the sensor inputs from the motor and turn it into usable voltage outputs

PID stands for Proportional Integral Derivative and is the most common method of control for motors on our robot. A PID controller is just the following equation:

$$ output = kP * error + kI * \int error + kD * error ' $$

Looks a little scary, but I promise it isn't that bad. Lets break down this equation, using the example of a flywheel trying to spin at 1000 rpm. First we have $error$, which is just a variable representing how far we are from where we want to be. In our example, this would be the difference between 1000 rpm and our current velocity. If our current velocity was 500 rpm, then $error$ would be 500 rpm. $output$ is just how fast we want our motor to go, usually in either volts or on a scale of -1.0 to 1.0 with 1.0 being full speed clockwise rotation. $kP$, $kI$, and $kD$ are constants which are specific to each mechanism we want to control, and we will find out what they do soon.

The first term of the equation, $kP *error$, just says that for every unit of error away from our setpoint we are, give some amount of output. You could say that this term scales the output proportionally to the input, hence the "P" in PID. In our example, lets use a $kP$ of 0.01. If we are 500 rpm away from our goal, then we can multiply that by $kP$ to get $0.01* 500 = 5.0$ volts of output. Many of the simple mechanisms we want to control will only use the "P" term of the PID loop, and set $kI$ and $kD$ to 0.

We are going to skip to the third term for now, since it is a little easier to understand. This is the derivative or "D" term of PID. A derivative is just the change in some value over time. If the PID controller is trying to control the position of a motor, the D term will react to velocity. In our example of a flywheel the D term will react to acceleration. Much like the P term, we multiply the derivative of our error by a value called $kD$. Usually we set this value so that it has a dampening effect on our output. That is, if we are very quickly approaching our target the D term will act to slow us down so we don't overshoot. For almost all mechanisms, a PD controller (usually with a *feedforward* *link to article*) is sufficient or even optimal to control the mechanism.

The final term of the PID controller is the "I" or integral term. This term, despite its name, is not incredibly useful for control and you will hardly if at all find it used in our codebase. This term is meant to deal with the case where you get close to your target, but the output from your P term is too small to get all the way there by adding up the error each loop and using that as a component of our output. However, it is prone to overshooting, oscillating, and being annoying to tune. There are also usually better ways to solve the problems that the I term solves, namely using *feedforwards*. Therefore, you can usually leave $kI$ as 0 and ignore the term.

### Resources

- [Video Explanation](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/pid-video.html#pid-introduction-video-by-wpi)
- [WPILib docs explanation](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/introduction-to-pid.html#introduction-to-pid)
- [**Interactive WPILib Examples**](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/tutorial-intro.html)
  - This explanation has a focus on the math behind PID control, which you can largely skim through
- [Implementing PID control in WPILib](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/pidcontroller.html#pid-control-in-wpilib)

### Examples

- [WPILib docs explanation](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/introduction-to-pid.html#introduction-to-pid), math walkthrough
- [WPILib PID controller implementation](https://github.com/wpilibsuite/allwpilib/blob/01490fc77b3543f80c47252d4bb1f44eb0573006/wpimath/src/main/java/edu/wpi/first/math/controller/PIDController.java)

### Exercises

- Complete all interactive WPILib tutorials and demonstrate tuning one of them
- *Sim kitbot closed loop driving tuning?*

### Notes

- See also *feedforward* control
- *remove intro and just use wpilib?*