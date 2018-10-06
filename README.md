# The 2018 Off-season Robot 
```text
﻿    ██╗    ██╗ █████╗ ██████╗ ██████╗ ███████╗
    ██║    ██║██╔══██╗██╔══██╗██╔══██╗╚════██║
    ██║ █╗ ██║███████║██████╔╝██████╔╝    ██╔╝
    ██║███╗██║██╔══██║██╔══██╗██╔═══╝    ██╔╝ 
    ╚███╔███╔╝██║  ██║██║  ██║██║        ██║  
     ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝        ╚═╝  
```

### Setup and Deploy
**Important**
The school network blocks some of the network required to download 
the necessary software, especially [https://raw.githubusercontent.com](https://raw.githubusercontent.com).
Therefore it may be necessary to set up the following things at home

**Download Software:**
Java is used to program the robot and the preferred IDE
is IntelliJ.Install the following: [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/) 
(Version 8, not 9 or 10), [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
(Version 2018.2 or higher), and [Git](https://git-scm.com/download). Also download and install the 
[FRC Update Suite](http://wpilib.screenstepslive.com/s/4485/m/13503/l/599670-installing-the-frc-2017-update-suite-all-languages)
for the Driver Station, RoboRIO Imaging Tool, and the mDNS driver for connecting to the robot network, as well as the 
[Configuration Utility](https://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/144986-programming-your-radio)
for the radio. This project uses Gradle which will
automatically download WPILib and related Java libraries.

**Download Code:**
Open IntelliJ and select "Check out from Version Control > Git". 
In the dialog, Log into a GitHub account that has access to the repo
and paste `https://github.com/Team865/FRC-2018-Offseason.git`. 
Click ok to download the code. IntelliJ will ask if you will
want to import the build.gradle file. Either import it now,
or go to "Import Project" from the start screen later and
choose build.gradle

**Import project into IntelliJ:**
A setup dialog is shown to import the gradle project
Leave the setup options as-is, but change the 
"Use default gradle wrapper (Recommended)" to 
"Use gradle wrapper function" and check "Auto Import"
Click next and it will create the project files and download
the libraries

**Using IntelliJ to Build and Deploy**
Find the Gradle Tab in IntelliJ (shown by Navigate > Tool Buttons),
there is list of tasks available for gradle to run.
Navigate to Tasks > build > build and run it by double click.
It will download all the required libraries. At this point the
project is ready for programming. To deploy, run the task at 
Tasks > embeddedTools > deploy.

All tasks can also all be ran from pure command
line. See [GradleRIO docs](https://github.com/wpilibsuite/GradleRIO)

### Programming Resources
A list of some relevant resources, not sorted in any order

- [FRC Control System Documentation](https://wpilib.screenstepslive.com/s/4485) - All docs
- [Team 254’s GitHub](https://github.com/Team254) - Code by Team 254
- [3rd Party Libraries](https://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/682619-3rd-party-libraries) - For specific components
- [Limelight Camera](https://limelightvision.io/) - Vision processing camera
- [FRC Java Programming](https://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/826278-2018-game-data-details) - List of Java topics
- [YOLO](https://pjreddie.com/darknet/yolo/) - Object detection vision algorithm
- [PID Control](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/599721-operating-the-robot-with-feedback-from-sensors-pid-control) - Using WPILib PID Controller
- [GradleRIO](https://github.com/wpilibsuite/GradleRIO) - Build and Deploy to the robot with Gradle
- [WPILib Intro](https://wpilib.screenstepslive.com/s/currentCS/m/java/l/599696-what-is-wpilib) - Create a simple program
- [Pathfinder](https://github.com/JacisNonsense/Pathfinder) - Trajectory path generation library
- [Network Troubleshooting](https://wpilib.screenstepslive.com/s/currentCS/m/troubleshooting/l/284355-roborio-network-troubleshooting) - on the RoboRIO
- [Cheesy Drive](https://github.com/Team254/FRC-2018-Public/blob/master/src/main/java/com/team254/lib/util/CheesyDriveHelper.java) - Curvature driving by Team 254
- [2019 Changes to WPILib](https://wpilib.screenstepslive.com/s/currentCS/m/79833/l/943086-alpha-test-info) - for the new season
- [FRC Programming Done Right](https://frc-pdr.readthedocs.io/en/latest/) - set of tutorials
- [Programming Forum](https://www.chiefdelphi.com/forums/forumdisplay.php?f=51) - Chief Delphi
- [Looper and Loops](https://github.com/FRC-Team-955/Team955RobotLib/wiki/Looper-and-Loops) - By Team 955
- [PID Loops Theory](https://github.com/FRC-Team-955/Team955RobotLib/wiki/PIDF-Loop) - By Team 955

### Package Organization

- Reusable code are put in subpackages under the ```ca.warp7.frc``` packages. 
These include internal code useful to all robots such as the looping mechanism,
state observers, and autonomous runners. 

  - The entry point is located at ```ca.warp7.frc.core.Robot```
  - ```.frc.core``` also has classes that manage subsystems, autos, loops,
  and accumulating states.
  - Autonomous action scheduling are managed in ```.frc.scheduler```
  - The custom speed controller and Xbox controller are in ```.frc.wpi_wrapper```
  - Most other packages in ```.frc``` are for support data structures

- Code for specific robots are in packages named ```ca.warp7.frc20xx```. 
Replace ```20xx``` with the year of the game the robot is designed for.
If it is a second robot for that year, add ```_r2``` to the end. If it is
a second codebase for the same robot, add ```_v2``` to the end.

- The ROBOT_CLASS is saved to the top level of the previously said 
package. There should not be other files at this level. There could, however,
be another copy of the ROBOT_CLASS with different constants configuration.

- Subsystems are in the ```subsystems``` package of the robot package

- Auto modes and actions are in the ```auto.modes``` and ```auto.actions``` packages respectively.
Test modes go in the ```auto.test_modes``` package

- Constants and RobotMap are in the ```constants``` package.

### Java Naming Conventions

```java
// All Classes: CamelCase
class RobotNamingConventions{ 
	
	// Private instance members: CamelCase with 'm' at start
	private DualController mController;
	private Drive mDrive;
	
	// Constants: CamelCase with 'k' at start
	static final double kMaxCharacters = 255;
	
	// Static fields: CamelCase with 's' at start
	private static DataPool sDrivePool;
	
	// Record Class fields: mixedCase
	// This includes subsystems and robot map
	public double x, y, tiltAngle;
	
	// Methods: mixedCase
	public void robotInit() {
		// Method variables: mixedCase
		String robotMessage = "Hello me is robit";
		System.out.println(robotMessage);
	}
}

// Enums and lists of common-purpose constants:
// All Caps with underscore
public enum ButtonState {
	KEPT_UP,
	HELD_DOWN,
	PRESSED,
	RELEASED
}

// Interfaces: PascalCase with 'I' at start
interface ISubsystems {
}
```