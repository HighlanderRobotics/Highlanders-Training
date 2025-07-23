# This is an example: Using Current Sensing for Zeroing Mechanisms

This is a made up example for a stage 3 project writeup.

## Abstract

Zeroing or homing mechanisms is an important part of reliable controls.
To achieve we have used limit switches and absolute encoders.
However, both of these methods have drawbacks.
Limit switches have had reliability issues, and absolute encoders are not always feasible to design in.
To get around these limitations, we attempt to use current sensing to zero mechanisms in this project.

## Procedure

Current sensing is the process of using a "spike" in current when a mechanism is stalled to detect the presence of a hard stop.
This gives the mechanism a reference for its position.

To test this method, we use an elevator on our previous robot.
The following method generates a Command to home the mechanism.

```Java
public Command zero() {
  return this.run(() -> io.setVoltage(-1.0))
    .until(() -> inputs.statorCurrent > 20.0)
    .finallyDo(
      (interrupted) -> {
          io.setVoltage(0.0);
          if (!interrupted) io.setPosition(0.0);
        }
      );
}
```

This Command starts by running the elevator at a set voltage towards the hard stop.
Once the current goes above a threshold, we set the output to 0 volts.
Then, if the elevator was not interrupted by another command we set the position recorded by the encoder to 0.
The current threshold must be tuned to not trigger when the elevator starts moving, but still trigger on stall without damaging the elevator.
Future improvements could include a minimum motion time or other filtering.

This command worked reliably to zero the elevator, both from starts at or near zero and for starts while extended.

## Conclusion

Current zeroing is a reliable and low overhead way to home a mechanism 👍.
