# Electronics Crash Course

## There are a lot of electrical components on an FRC robot, many of which we will need to interface with in software

### Electrical system diagram

![Rev electrical system diagram](<Assets\RevElectricalSystemDiagram.png>)

This diagram shows many of the electrical components found on a typical FRC robot.
You don't need to memorize this layout, but its handy to have this as a reference.

### Battery

![Battery](Assets\Battery.jpg)

All of the power our robot uses comes from a single 12-volt car battery.
These can be found on our battery cart in the corner of the lab.
Not all batteries are created equal: Some brands and specific batteries can hold more juice for longer than others.
One way we can see the performance of a battery is the resting voltage (or voltage when no motors or similarly power hungry devices are running) of the battery after it has charged.
This can be checked with the battery beak or on the driver station.
A good battery will have a resting voltage of over 13 volts, and any battery we use will be above 12.
As the battery is used the voltage will go down.
We tend to swap our batteries after they get to around 11 and a half volts, although if we are testing something sensitive to voltage (like auto) we might swap them sooner.
After a match a good battery is usually still above 12 volts.
To track batteries so we know which batteries are "good" and which are "bad" we give them names like Garbanzo or W.I.S. (Women In STEM).

When the robot is on the voltage will also drop as power is actively used by mechanisms.
This is called voltage sag.
This means that we can't pull full power from every motor on the robot at once and have to be careful to design and program around our power budget.
When the voltage of the robot drops too low we can brownout.
This is usually first visible when the LEDs on the robot go out.
When the voltage is low enough motors will begin to be turned off which is visible as a stuttering or jerking.
If the voltage is even lower, the robot can restart.
Needless to say we want to avoid any brownout conditions in a match or when testing.

### Main Breaker

![Main Breaker](Assets\Breaker.jpg)

The main breaker is the power switch of the robot.
The red button will turn the robot off, and a small black lever on the side of the breaker will turn it on.
The main breaker is directly connected to the battery and limits the current draw of the robot, or the amount of power it can use at once.
Whenever you need to be hands on with the robot, make sure the breaker is off.
If you can't connect to the robot, make sure it's on.

The breaker should be mounted in a visible and accessible location on all robots, although it tends to blend in with its black casing.
We tend to have a 3dprinted guard over the off switch to prevent accidental presses by other robots mid-match.

### Power Distribution Hub (PDH)

![PDH](Assets\PDH.webp)

The PDH takes the power from the battery and distributes it to the motors, sensors, and other components on the robot (Almost like its a hub for distributing power!).
We don't have to do anything with the PDH in code, but if a motor or sensor does not have power it could have a bad connection with the PDH.
The PDH will also have a smaller breaker for each motor on it, which limits the power draw from each individual motor to 40 amps.
In practice, we can draw more for a short period of time.
If you are interested in more details about how and why that works, look at the electrical coursework for the team.

The PDH is also usually at one end of our CAN network. What's CAN? Glad you asked . . .

### The CAN Network

![CAN Wire](Assets\CANWire.png)

CAN is a type of communication along wires that allow our sensors and motors to communicate along our robot.
In FRC, CAN is transmitted over yellow and green pairs of cables.
Each device on the CAN network has a unique ID that it uses to talk to other devices on the network.
All of our motors and many of our sensors will communicate over CAN.
CAN is also one of the easiest places for electrical problems to become apparent in software because of the thin wires and large reach of the system.
When you start up the robot you may see red error messages in the drive station console reporting CAN errors.
If you see that, something might be broken on the CAN network.

We have to set the IDs of the devices on the CAN network to make sure they are all uniquely and correctly identified.
It would be very awkward to try to drive the robot and accidentally run the elevator!
To set the IDs of devices on the CAN network we use the [Tuner X](https://pro.docs.ctr-electronics.com/en/stable/docs/tuner/index.html) app built by CTRE.
This app also has features to test and configure devices on the CAN network.

### RoboRIO 2

![RoboRIO](Assets\RoboRIO.jpg)

The RoboRIO (rio) is the brain of the robot.
It is built around a computer running Linux with a large number of Input/Output (IO) ports.
These ports include:

- Digital IO (DIO) can accept inputs and outputs that are either a 1 or 0, on or off.
 When used as an input the signal can rapidly be turned on and off to send numerical data.
 This is done using Pulse Width Modulation (PWM), essentially measuring how long a signal is on compared to how long it is off in a given time window to get a numerical value.
 For more information on PWM read [this article](https://learn.sparkfun.com/tutorials/pulse-width-modulation/all)
- PWM pins provide additional pins to *output* PWM signals.
  Unlike the DIO ports the PWM ports cannot take inputs.
  Many motor controllers support using a PWM signal for control instead of CAN, although it has significantly limited features.
- Analog inputs provide adititional sensor ports, although most sensors use DIO or CAN.
- The large set of pins in the middle of the Rio is the MXP port.
  MXP (and the SPI port in the top-right corner) is used for communication over serial interfaces such as I²C protocal.
  Unfortunately, there is an issue with I²C that can cause the code to lock up when it is used, so we avoid using the serial ports.
- The CAN network originates at the RIO.
- Several USB ports are available on the Rio.
  Common uses include external USB sticks to store logs or the CANivore (see later in this page)
- An ethernet port which connects to the radio (keep reading) to communicate to the driver station.

The Rio also has an SD card as its memory.
When we set up a Rio for the season we need to image it using a tool that comes with the driver station.
The WPILib docs have [instructions](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-3/roborio2-imaging.html) on how to image the SD card.

### Radio

![Radio](Assets\Radio.jpg)

The radio is essentially a Wi-Fi router that connects the robot to the driver station.
At tournaments we have to take the radio to get reprogrammed for that competition.
This makes it able to connect to the field so that our robot gets enabled and disabled at the right times during matches, however it prevents us from connecting to the robot with a laptop wirelessly.
The radio has two ethernet ports and one barrel jack port.
One ethernet port connects to the rio.
We can connect certain advanced sensors, like vision systems, to ethernet.
Sometimes we add a network switch to the ethernet network, which is a device that allows us to have more ethernet ports than the radio provides.

After each competition we have to reimage the radio to allow it to connect to a laptop wirelessly again.
We do this using the radio imaging utility that comes with the driver station.
Follow [these instructions](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-3/radio-programming.html) to reimage the radio.

The radio can either be powered using Power-Over-Ethernet (PoE) or the barrel jack port.
PoE reduces the number of cables we have to run, so we usually use it on the robot.
However, we have had issues reimaging the radio while using PoE so having a barrel jack connector on standby is useful.
If you have issues with imaging the radio, check the way it is powered.
Other things that can cause imaging issues include bad cables or radios, so be thourough and careful when imaging especially with an unknown radio.

### Motor Controllers

![Spark Max](Assets\SparkMax.jpg)

Motors are the primary form of movement on the robot, but on their own they are just expensive paperweights.
Motor controllers take signals from our code, often over CAN, and turn them into the voltage sent to the motor.
These controllers plug into slots on the PDH, the CAN network (or much more rarely PWM ports), and the power lines of the motor.

Many motor controllers have more features than just commanding a voltage to a motor.
For instance they might be able to run PID loops much faster than our code is able to.
Knowing what motor controller you are using and what features it has is very important when writing robot code.
Pictured above is the Spark Max built by Rev Robotics, a common modern motor controller often used with the NEO and NEO 550 motors.
However, our combo of choice is . . .

### The Falcon 500 / Talon FX

![Falcon](Assets\Falcon.jpg)

The Falcon 500 (falcon) motor is the primary motor we use on the robot.
Unlike many other motors, falcons come with a built in motor controller called the Talon FX.
The falcon has a built in encoder, or sensor that tracks the rotation and speed of the motor.
Documentation for the software to control falcons can be found [here for the v5 version of the software we used in 2023](https://v5.docs.ctr-electronics.com/en/stable/) and [here for the v6 version we will use going forward](https://pro.docs.ctr-electronics.com/en/stable/).

### Solenoids and Pneumatics

![A solenoid](Assets\Solenoid.jpg)

Pneumatics are useful for simple, linear, and repeatable motions where motors might not be ideal.
In the past we have used them primarily for extending intakes.
If pneumatic pistons are like motors, solenoids are like motor controllers.
A solenoid takes a control signal and uses it to switch incoming air into one of two output tubes to cause it to either extend or retract.
We usually use double solenoids, which have both powered extension and retraction.
Single solenoids only supply air for extension, and rely on the piston having a spring or something similar to retract them.

### Robot Status Light

![RSL](Assets\RSL.jpg)

The Robot Status Light (RSL) is an orange light we are required to have on our robot by competition rules.
When the robot is powered on it will glow orange.
When the robot is enabled ie being controlled by joysticks or autonomous code it will flash orange.
This is handled automatically by WPILib.
**When the robot is enabled, do not go near the robot**.

We have additional LEDs on the robot that indicate the state of the robot, but they are not standardized year to year and should not be relied upon for safety information.

### CANivore

![CANivore](Assets\CANivore.webp)

The CANivore is a device that connects to the Rio over usb.
It allows us to have a second CAN network with increased bandwidth.
This is useful because CAN has a limited amount of information that can travel over it each second, and we can easily reach that limit with the number of motors and sensors we have.
The CANivore gets around some hardware limitations of the Rio to have extra bandwidth on its network.

## Sensors

### Encoders

![CANcoder](Assets\CANcoder.jpg)
![Rev Through Bore ENcoder](Assets\RevThroughBore.webp)

Encoders are devices that measure rotation.
We use them to measure the angles of our swerve modules, the speed of our wheels, the position of our elevator, and much more.
Modern motors have encoders built in.
However these encoders are relative encoders.
Relative encoders are set to 0 when the robot is turned on, and track the change in position from there.
If all we care about measuring is velocity this is fine, but if we wanted to know how far our elevator is extended or what angle our swerve module is at we would need to align them to some known position whenever we started up our robot.
Instead we can use absolute encoders to measure the absolute position of a mechanism.
These encoders don't reset when powered off so we don't need to worry about reseting the position of a mechanism when we power on.

Some examples of absolute encoders are the CTRE CANcoder (upper picture).
The CANcoder sends absolute position and velocity data over the CAN network and uses a magnet embedded in the mechanism to keep track of position.
We use these to track the heading of our swerve modules.
However, designing and manufacturing mechanisms to hold that magnet is inconvenient so we don't usually use CANcoders for other mechanisms.

The Rev Through Bore encoder (lower picture) solves this mounting problem by connecting directly to hex shaft (the most common type of shaft in FRC).
However, the Through Bore does not communicate over CAN and requires wiring to the DIO ports.
We used one of these on the hood of our 2022 robot.

Absolute encoders are only useful when they cannot rotate more than once, because they only return their position in a 0-360 degree range.
This is something to keep in mind when prototyping or looking at designs for mechanisms.
If it is infeasable to have an encoder that goes 1 or less rotations for a mechanism, you might want to take a look at . . .

### Limit Switches

![Limit Switch](Assets\LimitSwitch.jpg)

A limit switch is a simple sensor that tells us whether a switch is pressed or not.
They are one of the electrically simplest sensors we can use, and are quite useful as a way to reset the encoder on a mechanism ("Zero" the mechanism).
They can also be used to set up safety limits for mechanisms so they don't damage themselves or others.
They are quite fragile, however, so absolute encoders are better to be used where possible.
We used a limit switch on our 2023 robot that was pressed when the elevator was fully retracted.
When it was pressed, we knew that the elevator must be fully retracted so we could reset the encoder to 0 inches of extension.

### IMU

![Pigeon 2 Gyro](Assets\Pigeon2.webp)

An Inertial Measurement Unit (IMU or sometimes Gyro) is a sensor that measures its acceleration and rotation.
We primarily use them to measure the rotation of the robot (heading) so that the robot knows which way it is pointing on the field.
This lets us run field-relative swerve drive.
Pictured above is the Pigeon 2.0 IMU by CTRE, an IMU that connects over the CAN network.

### Cameras and Vision

![A Limelight Camera](Assets\LimeLight.png)

Cameras and vision systems are an important part of advanced FRC software.
Vision in FRC can be used to detect special retroreflective tape on the field, visual markers called apriltags on the field, game pieces, and even other robots.
The pictured camera is a Limelight camera, a purchaseable vision solution that we have used for the past few years.
Limelights connect to the robot over ethernet.
However they are generally not built for apriltag detection and pose estimation, which has pushed us towards custom solutions.
See the [Vision](Vision.md) article for more details.
