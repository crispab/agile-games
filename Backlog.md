# Backlog

This is the queue for future features. Not. It is my memory list for things
that come up, and I wish to offload my brain by putting them here.

## Bugs

No known bugs.

### Need to reload

It has been reported that the players need to reload when the facilitator
clicks for estimation. Possible cause is websocket timeout (speculation).

## Publish the BDD report

While putting the specification in a Word document has some value, 
I think the report of the test run also have that. Would it be possible
to publish that in a Travis build so that it can be linked to from the 
readme, like quality and build results?

## Testing with multiple players

Design choice to not use the URL to pick a game led to the impossibility
to have several sessions in the same browser, albeit two with anonymous
works. This is a bit inconvenient when testing. A solution could be to 
change the design but another would be to add **bot players**. That could be
valuable to users when the group is not large enough.

## Executing 

Nothing right now.

## Reporting

### Report outcome

When reporting, show outcome with estimations per goal and total, 
for each player. Both players and facilitator should see them.

## Facilitation

### Explain the visuals in the player list

They are small, and they are cryptic.

### Show the next phase 

Show the next phase to the facilitator so that they do not skip one 
by mistake.

