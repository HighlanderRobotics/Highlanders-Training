# AdvantageKit Code Structure Reference

This document contains a quick reference for how we structure our code.
It should be used when planning out the structure of a subsystem.

## Basic Layout

```mermaid
flowchart TD
    Subsystem-->IO
    IO-->IO Implementation Real
    IO-->IO Implementation Sim
```

```mermaid
graph TD;
    A-->B;
    A-->C;
    B-->D;
    C-->D;
```

This diagram shows the basic structure of an AKit Subsystem.
It includes 3 layers:

### Subsystem

- The "subsystem" layer is a class which extends `SubsystemBase`.
- This class should contain methods that return Commands for this subsystem.
- This class should contain all higher-level control flow within this mechanism.
  - For instance, it could contain a `SwerveDriveOdometry` object to track a drivetrain's position.
  - It might contain information about the current target of a mechanism, or whether or not the mechanism has homed its position yet.
