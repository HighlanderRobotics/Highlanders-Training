# Feedforward control

## Feedforwards are a broad class of controls that we use to take a first guess at how much control effort we need to use

"Feedforward" is a broad class of controls that describes any controller that does not use measurements from its output to adjust its output. **Essentially, feedforward is a fancy way of giving a constant voltage output**. If you just call `setPercentOut()` on a motor, you are using feedforward control. That being said, we can use feedforward in more complex ways to account for different forces on the mechanism. While a PID controller is dependant on having an accurate sensor reading, a feedforward controller does not need one. The downside of this is a feedforward controller cannot respond to disturbances or other effects to the mechanism we want to control. Usually we use feedforward together with a PID controller to control mechanisms.

### Resources

- [WPILib docs article](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/introduction-to-feedforward.html#introduction-to-dc-motor-feedforward)
- [Implementing feedforward in WPILib](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/feedforward.html#feedforward-control-in-wpilib)

### Examples

- *find or make some*

### Exercises

- *Make an elevator sim, control using motion profile + PIDF*

### Notes

- Make sure to tune feedforwards before tuning PID gains, since the feedforward should be doing the bulk of the work
