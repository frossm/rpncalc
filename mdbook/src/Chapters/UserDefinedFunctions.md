# User Defined Functions
RPNCalc can record your commands and save them as a user defined function.  In essence, you are creating a new command that can be run.

When recording a new function, start by adding data to your stack that would emulate when you would run your function.  The only data that will be part of your recording will be data & commands entered ***after*** recording has been turned on.  When you are ready, enter the `record on` command.  A red `[Recording]` alert will appear in the status line. Anything you add during this period will be recorded with the exception of the commands listed below.  Enter your commands, numbers, etc. until you are done.

When recording is complete, enter `record off` to complete your recording.  You'll be prompted for a function name.  Your new command will be this name so choose a name without spaces and that is easy to type when you wish to execute the function in the future.  You should also not choose a name of an existing command as your function will not be able to be called.  If you do not provide a name, i.e, just hit enter, the recording will be discarded.

Then you can run your function whever you like on the stack currently available.  To run the function, simply type the name of your function as a standard command. To see a list of the saved functions, execute `list func` and it will display the name and the steps you recorded.

User defined functions can be deleted with the `func del NAME` command or you can delete all of the functions with `func delall`

Functions are global and can work across any stack.  They are saved in the preferences system and will be reloaded when RPNCalc starts.  They are saved immediatly after you give a new recording a name and press enter.

When you execute a function, the steps of that function are executed one after the other.  Therefore when you execute `undo` you will undo back through your function step by step. You do not `undo` the entire function in one command.  Of course you can always run `undo NUM` where num is the steps to undo.

The following commands can be entered during a recording, but are not recorded.
- frac
- list
- debug
- ver, version
- help, h, ?
- rec, record
- func, function
- cx, x, or exit

|Command|Description|
|-------|-----------|
|record on<br>rec on|Turn on recording.  Most commands and numbers entered after record is enabled will be saved.  There are some that are excluded from being recorded as detailed above|
|record off<br>rec off| Turn off recording.  The user will be prompted to enter in a name for this function and that name will be used to run it in the future.  If you do not enter in a name the recording is canceled and  nothing will be saved|
|func del NAME|Delete a saved function.  The name must match the one given when saved.  A list of functions can be viewed with `list func`.  Undo will not recover a deleted function|
|func delall|Delete all saved user defined functions. Please note that undo will not recover deleted functions|

TODO: Talk about the example here

|Command|Description|
|-------|-----------|
|`c`|Clear the stack|
|`3 [Enter]`|Add the number 3 to the stack
|`record on`|Start recording
|`2`|Add 2 to the stack
|`^`|Square the 3
|`record off`|Stop recording and give the name "square" to the user defined function
|`square`|Square the top item on the stack