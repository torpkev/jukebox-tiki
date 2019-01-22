Jukebox-Tiki

Jukebox-Tiki allows the continuous loop of discs within a Jukebox.  It adds a persistent internal storage where music discs can be placed.

Placing a Jukebox in-game while you have the jukebox.place permission will automatically create it as a looping Jukebox.  You can access the storage by right clicking the Jukebox while sneaking.  To access the controls (stop/next song), simply right click the Jukebox.

Only the owner of the Jukebox can access the storage/controls or break the Jukebox unless the player also has the jukebox.admin permission.

To break the Jukebox, any music discs must be removed from the storage first.

The Jukebox can be pre-configured to include a list of discs, these are set in the internal_prestocked config entry.

Permissions:
jukebox.place
jukebox.admin

Commands:
/jukebox save

Config:
internal_prestocked: - This is a list of music discs that a Jukebox begins with.  These must match the material name for the discs, these include: MUSIC_DISC_11, MUSIC_DISC_13, MUSIC_DISC_BLOCKS, MUSIC_DISC_CAT, MUSIC_DISC_CHIRP, MUSIC_DISC_FAR, MUSIC_DISC_MALL, MUSIC_DISC_MELLOHI, MUSIC_DISC_STAL, MUSIC_DISC_STRAD, MUSIC_DISC_WAIT, MUSIC_DISC_WARD
distance: 64 - This is the distance that a player must be within for the Jukebox to continue playing.  If no player is within range, the next disc will not be played.
auto_save_secs: 600 - This is the number of seconds between saving the Jukebox locations/storage.  These will be automatically saved when stopping the plugin/shutting down the server
verbose_logging: false - Verbose logging provides additional information during plugin usage
debug_logging: false - Debug logging provides a lot of additional information during plugin usage, only to be used during troubleshooting