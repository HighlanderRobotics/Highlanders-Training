# Use of a Trigger and State Machine Backed Superstructure File for Subsystem Coordination

Lewis Seiden, 2024 Crescendo Offseason

## Abstract

I started on this project after seeing [2974's](https://github.com/WaltonRobotics/Crescendo/blob/main/src/main/java/frc/robot/subsystems/Superstructure.java) and [5940's](https://github.com/BREAD5940/2024-Onseason/blob/main/src/main/java/frc/robot/subsystems/Superstructure.Java) code, and wondering if using explicitly defined states could help manage our code.
These teams had a superstructure file which coordinated several subsystems based on a state machine.
This allowed for complex control flow with less places for confusion.

5940's Superstructure takes the form of a single subsystem with several IO layers in it.
This subsystem handled their elevator, pivot, and feeder for their 2024 robot.
These mechanical systems worked in concert and required some compensation based on the state of each of the others, so having them tightly coupled in one subsystem makes sense.

To request behavior the class has a number of boolean members, like `requestHome`.
The current state of the class is tracked, and passed through a massive if-else block.
Requests are used to manage state transitions, and IO outputs are set through the block.

This has some advantages, its simple and easy to understand.
It clearly encapsulates several IO objects in a subsystem.
However it is limited to fully encapsulating everything in a subsystem, and doesn't address the problem of wanting additional layers on top of subsystem.
It also invites bugs with a large managed if-else block and no hardware mutexing within the superstructure.

2974's superstructure file is not a subsystem.
Instead, it takes in several subsystems and maps *many* triggers to coordinate them.
They do not explicitly define states, instead they define command bindings using the many triggers defined in the file.

```Java
// An example binding
 (stateTrg_intake.and(trg_subwooferAngle.or(RobotModeTriggers.autonomous()).and(trg_straightThroughReq.negate())))
            .onTrue(
                Commands.parallel(m_intake.fullPower(), m_conveyor.startSlower())
            );
```

This setup is more flexible and easier to integrate with existing command based code, as well as demonstrating the power of Triggers.

However, it requires setting up a *lot* of triggers and doesn't explicitly define states as clearly as 5940.

To this end, I combine the two approaches by explicitly defining a state machine with a Trigger on each state.
By using a Trigger for each state, we can easily integrate with existing Subsystems.
By using an explicit state machine, it clearly structures the codebase.

Because of our highly integrated robot architecture for Banshee, all non-swerve subsystems were integrated in the superstructure.

To set up the superstructure, I wrote out all the states that the robot would be in and the edges between them.
This is the original state machine I worked from.

<img alt="Banshee State Machine" src="../Assets/banshee-state-machine.png" width="400" height="400">

And this is a cleaned up version from the final code.

```mermaid
%%{ init: { 'flowchart': { 'curve': 'linear' } } }%%
graph TD;

IDLE ---> INTAKE;
INTAKE --> INDEX_SHOOTER;
INDEX_SHOOTER --> READY_INDEXED_SHOOTER;
READY_INDEXED_SHOOTER --> PRESHOOT;
READY_INDEXED_SHOOTER --> PREFEED;
READY_INDEXED_SHOOTER --> PRESUB;
PRESHOOT --> SHOOT;
PREFEED --> SHOOT;
PRESUB --> SHOOT;

SHOOT --> IDLE;

INTAKE --> INDEX_CARRIAGE;
INDEX_CARRIAGE --> READY_INDEXED_CARRIAGE;
READY_INDEXED_CARRIAGE --> PREAMP;
PREAMP --> AMP;
AMP --> IDLE;
READY_INDEXED_CARRIAGE --> INDEX_SHOOTER;
READY_INDEXED_SHOOTER --> REVERSE_INDEX_CARRIAGE;
REVERSE_INDEX_CARRIAGE --> INTAKE;


IDLE --> SPIT;
READY_INDEXED_SHOOTER --> SPIT;
READY_INDEXED_CARRIAGE --> SPIT;
SPIT --> PRECLIMB;
PRECLIMB --> CLIMB;
PRECLIMB --> IDLE;
CLIMB --> HOME_ELEVATOR;
CLIMB --> PRECLIMB;

IDLE --> HOME_ELEVATOR;
HOME_ELEVATOR --> IDLE;
IDLE --> HOME_SHOOTER;
HOME_SHOOTER --> IDLE;
```
