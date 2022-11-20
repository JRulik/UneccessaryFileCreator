
Readme in progress....
v0.91

In short:
-This application can create files given size in MB.
-Mabye it could be harmful for your disks (creating many files). Not sure how its managed on OS level.
-Use only at your own risk. See implementation before use of application if you are worry.
-Usage of this application is obvious from UI. UI is simple and good, if you don´t understand usage of it, the fault is not in GUI

About app:
Main purpose of this app was to learn JavaFx and some threading. Not much higher goals.
-There are only few of input checks. This application isn´t robust for all possibilities of inputs. See implementation.
-Some things yet to be implemented.

for run in IDE:
downloads JavaFx libraries and set VM variables (example for eclipse here: https://www.youtube.com/watch?v=_7OM-cMYWbQ&t=312s&ab_channel=BroCode )

More info:
-Spent 2-3 days to try to deploy it with maven and jlink and jpackage (everything is brand new for me). Haven´t been successful. With command mvn javafx:jlink jpackage:jpackage it create hopefully standalone app in folder. 
but output of jpackage isn´t working. Maybe will try maven shade for compiling into .jar sometimes. If someone would know, where is error (probably somewhere in .pom file) I will be happy for any suggestion.


