# Feedforward control

## Feedforwards are a broad class of controls that we use to take a first guess at how much control effort we need to use

"Feedforward" is a broad class of controls that describes any controller that does not use measurements from the mechanism to adjust its output. **Essentially, feedforward is a fancy way of giving a constant voltage output**. If you just call `setPercentOut()` on a motor, you are using feedforward control! That being said, we can use feedforward in more complex ways to account for different forces on the mechanism. While a PID controller is dependant on having an accurate sensor reading, a feedforward controller does not need one. The downside of this is a feedforward controller cannot respond to disturbances or other effects to the mechanism we want to control. Usually we use feedforward together with a PID controller to control mechanisms, which is covered in the [motion profiling](MotionProfiling.md) article.

### Resources

- [WPILib intro to control strategies](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/picking-control-strategy.html)
- [WPILib intro to feedforward](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/introduction-to-feedforward.html#introduction-to-dc-motor-feedforward)
- [Implementing feedforward in WPILib](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/feedforward.html#feedforward-control-in-wpilib)

### Examples

- *find or make some*

### Exercises

- Demonstrate tuning the [WPILib docs flywheel example](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/tuning-flywheel.html).

### Notes

- Make sure to tune feedforwards before tuning PID gains, since the feedforward should be doing the bulk of the work
- It is possible to calculate feedforwards from designs or using the WPILib system identification tool, although in the past we have had issues (possible related to unit conversions?) getting useful gains from the tool
