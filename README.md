# The 2018 Off-season Project 

```text
﻿    ██╗    ██╗ █████╗ ██████╗ ██████╗ ███████╗
    ██║    ██║██╔══██╗██╔══██╗██╔══██╗╚════██║
    ██║ █╗ ██║███████║██████╔╝██████╔╝    ██╔╝
    ██║███╗██║██╔══██║██╔══██╗██╔═══╝    ██╔╝ 
    ╚███╔███╔╝██║  ██║██║  ██║██║        ██║  
     ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝        ╚═╝  
```

### Project Organization

This project uses the Gradle Multi-Project build system to manage separable 
and/or reusable portions of the code into various projects. The ```:commons```
project includes reusable code used by all robot code and can be included 
by specifying ```compile project(":commons")``` in the dependencies

### Setup and Deploy
This project uses [GradleRIO](https://github.com/wpilibsuite/GradleRIO), a build plugin 
that will become official in 2019. It automatically downloads the required libraries 
and can manage multiple sub-projects. It also lets us use any IDE to develop our robot
code.

##### Important
GradleRIO uses [https://raw.githubusercontent.com](https://raw.githubusercontent.com) to
download some of its libraries, however it's blocked by the school board's network policy.
It may be necessary to perform builds at home whenever the libraries change so they can
be properly downloaded

##### Required Software
Java (Version 8, not higher) is the language used for this project. 
Download it here: [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/).
[Git](https://git-scm.com/download) is needed for making commits and pushing to Github.
We also need the [FRC Update Suite](http://wpilib.screenstepslive.com/s/4485/m/13503/l/599670-installing-the-frc-2017-update-suite-all-languages)
for the Driver Station, RoboRIO Imaging Tool, and the mDNS driver for connecting to the robot network, as well as the 
[Radio Configuration Utility](https://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/144986-programming-your-radio).

##### Preferred Environment
The preferred IDE for this project is [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Version 2018.2 or higher).
There are [Documentation](https://www.jetbrains.com/help/idea/gradle.html) for how to setup a Gradle project.

##### Download Code
Open IntelliJ and select "Check out from Version Control > Git". 
In the dialog, Log into a GitHub account that has access to the repo
and paste `https://github.com/Team865/FRC-2018-Offseason.git`. 
Click ok to download the code. IntelliJ will ask if you will
want to import the build.gradle file. Either import it now,
or go to "Import Project" from the start screen later and
choose build.gradle

##### Import project into IntelliJ
A setup dialog is shown to import the gradle project
Leave the setup options as-is, but change the 
"Use default gradle wrapper (Recommended)" to 
"Use gradle wrapper function" and check "Auto Import".
If it's asking for Gradle JVM, select your java installation
folder that should start with "jdk" and is usually in
"C:\Program Files\Java".
Click next and it will create the project files and download
the libraries

##### Using IntelliJ to Build and Deploy
Find the Gradle Tab in IntelliJ (shown by Navigate > Tool Buttons),
there is list of tasks available for gradle to run.
Find the specific module/project to be built and
navigate to Tasks > build > build under that module. 
Run it by double clicking.
It will download all the required libraries. At this point the
project is ready for programming. To deploy, run the task at 
Tasks > embeddedTools > deploy.