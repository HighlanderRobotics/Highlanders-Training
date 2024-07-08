# Vision Processing

## Vision processing for FRC

**WIP**  
When we navigate the world, we have our eyes and vision processing parts of our brains to help us.
This is also the case for our robots—we have a camera connected to a coprocessor that lets us use visual input in our code.

We primarily have two uses for vision: target detection and pose estimation.
The first usually involves some sort of visual fiducial marker (something that can act as a known point of reference that you can use to find your current location) near the target that we can align to and then score which is relatively easy to set up.
The second involves a more complicated system of identifying where certain visual fiducial markers are relative to the robot and then using that to generate an estimated pose of the robot on the field.

### Retroreflective tape

Retroreflective (RR) tape is the shiny stuff you see on roads, safety vests, traffic cones, and more.
For most reflective surfaces, any light hitting it bounces off at an angle, but RR surfaces will reflect the majority of the light back at the source.
This makes it a lot easier to accurately identify targets.
For example, in Charged Up there was RR tape on the cone targets which can be used to align for scoring.

### AprilTags

Apriltags are a system of visual tags developed by researchers at the University of Michigan to provide low overhead, high accuracy localization for many different applications.
They look similar to QR codes, but with less pixels.
This trades amount of information in each tag for quick, easy, and robust detection.
In the context of FRC, AprilTags are useful for helping your robot know where it is on the field, so it can align itself to some goal position.
The idea behind this is that when the camera detects an Apriltag, it will calculate the distance, angle, etc. that the robot is from that target and then map that to a diagram that shows where all the Apriltags are on the field and then estimate where the robot is from there.
This is useful because with a reliable pose estimation system we can automate more of the scoring process.

### PhotonVision

Photonvision is an app and library that gets vision data from the robot for a variety of applications.
It makes it a lot easier to interface with the camera and also has plenty of utility methods to get information like distance and position, as well as having some built in pose estimator features and more.

### Limelight

Limelight (LL) is a camera and processor system for FRC designed to make vision easy and simple.
We used a Limelight 2+ with Limelight's software in 2022 for RR tape detection, and a LL2+ with Photonvision software in 2023 for Apriltag detection.
The LL2+ was not satisfactory for apriltag detection for us, and we will likely move on to different hardware in the near future.

### Resources

- [Photonvision docs](https://docs.photonvision.org/en/latest/index.html)
- [Photonvision repo](https://github.com/PhotonVision/photonvision)
- [WPILib article on vision](https://docs.wpilib.org/en/stable/docs/software/vision-processing/index.html)

### Examples

- [Official photonlib examples](https://github.com/PhotonVision/photonvision/tree/master/photonlib-java-examples)
- [5026 2023 code](https://github.com/Iron-Panthers/FRC-2023/blob/037d52ac93f4e46a2518491acd2e195d429d6dbd/src/main/java/frc/robot/subsystems/VisionSubsystem.java)
- [8033 2023 apriltag code](https://github.com/HighlanderRobotics/Charged-Up/blob/main/src/main/java/frc/robot/subsystems/ApriltagVisionSubsystem.java)
- [8033 2023 rr tape code](https://github.com/HighlanderRobotics/Charged-Up/blob/main/src/main/java/frc/robot/subsystems/TapeVisionSubsystem.java)

### Exercises

- Once you have photonvision installed and the web dashboard set up, mess around with the pipeline tuning settings
- wip

### Notes

- Photonvision is installed on the coprocessor not on your computer, what you’re looking at on your computer is just a web dashboard
- Last year we used a system called a Limelight which had a camera bundled in with a coprocessor. It had a couple of performance issues, so this year we’re probably going to use custom hardware **still tbd if we’re going to use an opi or not so i’ll come back and edit this**

### Whats Next?

- Next course in course progression, if applicable
