<img align="right" width="125" src="../Images/Memory.png">

# Memory Commands

The memory capabilities are fairly flexible.  By default, there are 10 memory slots you can use (numbers 0 - 9).  This amount can be increased with the `set memslots` command.  If you increase or decrease the number of memory slots, it is persistent across RPNCalc executions.  You can change it as will, but if you decrease the number of slots, anything held in the "no longer there" slots will be discarded.

For example, say you `set memslots 20` and store values in all of them.  If you later `set memslots 10`, the values in the upper 10 slots will be gone.

The operational command `reset` will set the number of memory slots back to the default of 10.

The basic need to is to save a value from the current `line1` into a memory slot and copy it back later.  The memory slots are saved between program executions so if you need it in a later session, it will be there.  It's saved in the Java preferences system as discussed earlier.

|Command|Description|
|-------|-----------|
|mem [slot] add|Add the top stack item / `line1` into the memory slot provided.  By default, there are 10 slots; 0 through 9.  If you do not provide a slot number it will default to slot 0.  Example:  `mem add`   will add the contents of `line1` memory slot0|
|mem [slot] copy<br>mem [slot] recall|Copies the contents of memory slot provided back onto the stack.  Defaults to Slot0 if no slot number is provided|
|mem [slot] clr|Clear the contents of the memory slot provided.  Defaults to Slot0 if no slot is provided.  Example: `mem 2 clr`  The command `clear` can also be used instead of `clr`|
|mem clearall|Clears the contents of all memory slots.  There is no need to include a SlotNumber as they will all be erased.  Note `mem clrall` will also work|
|mem copyall|Copy all items from the memory slots onto the stack.  These will be ordered via memory slot number. i.e. Memory slot 0 will be at the top of the stack / `line1`. `recallall` will also work|
|mem addall|Add all items in the stack to a memory slot.  The order will be the same as the stack with memory slot0 being the top of the stack.  The command will fail if there are more stack items than there are memory slots - which default to 10.  It may simply be easier to save the stack with a name using the load `-l` command.  Also, any values currently in a needed memory slot will be silently overwritten|