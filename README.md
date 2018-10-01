# The 2018 Off-season Robot code
### Setup and Deploy
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