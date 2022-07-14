# Memory Commands

|Command|Description|
|-------|-----------|
|mem [slot] add|Add the top stack item / line1 into the memory slot provided.  By default, there are 10 slots; 0 through 9.  If you do not provide a slot number it will default to slot 0.  Example:  `mem add`   will add the last stack item into memory slot0|
|mem [slot] <br> copy [slot] <br> recall [slot]|Copies the contents of memory slot provided back onto the stack.  Defaults to Slot0 if no slot number is provided|
|mem [slot] clr|Clear the contents of the memory slot provided.  Defaults to Slot0 if no slot is provided.  Example: `mem 2 clr`  The command `clear` can also be used instead of `clr`|
|mem clearall|Clear the contents of all memory slots.  There is no need to include a SlotNumber as they will all be erased.  Note `mem clrall` will also work|
|mem copyall|Copy all items in memory to the stack.  These will be ordered via memory slot number. i.e. Memory slot 0 will be at the top of the stack / line1. `recallall` will also work|
|mem addall|Add all items in the stack to a memory slot.  The order will be the same as the stack with memory slot0 being the top of the stack.  The command will fail if there are more stack items than there are memory slots - which default to 10.  It may simply be easier to save the stack with a name using the load `-l` command.  Also, any values currently in a needed memory slot will be overwritten|