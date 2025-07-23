# PathPlanner

## A library enabling advanced autonomous path following for FRC

Pathplanner is a combination of an app which lets you draw spline paths on a map of the field and a library which translates that path into commands for our robot to follow.
The library allows extremely easy and concise autonomous programming and is what enables us to have high-level autos.
The library also has features to trigger non-drivetrain Commands during auto letting us do most of the work of writing new auto paths in the path editor instead of in code.

### Resources

- [PathPlanner Repository](https://github.com/mjansen4857/pathplanner)
- [PathPlanner Docs](https://github.com/mjansen4857/pathplanner/wiki)
- _Make a demo video of setting up pathplanner and making an auto_

### Examples

- Previous years code
  - `AutoChooser.java` in 2023
- _example code from video to make_

### Exercises

- Set up PathPlanner in kitbot drive code
  - Make a method that returns a `DifferentialDriveAutoBuilder`
  - Use that `AutoBuilder` to generate a full auto command

### Notes

- We may be reworking how we do our autos in the near future, including doing a partial rewrite of PathPlanner.
  Talk to a software lead or mentor for more details (and _update this page later_!)
