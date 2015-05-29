My Music Player
==========

Licensing information: BSD3
---

Overview
---------
This Android app is a project I started to help myself learn more about Android and Java development.

Using the app
---------
* The app displays and sorts the music files based on their respective fielpath and parent directory.
* After a file is finished playing the next sequential file will be played, unless the shuffle setting is turned on.
* The shuffle setting can be toggled via the ```Shuffle::ON``` or ```Shuffle::OFF``` setting in the action bar.
* ```.mp3```, ```.flac```, ```.m4a```, and ```.wav``` files are supported.
* Pressing the ```Next``` button in the lower right corner will cause the current file that is playing, if any, to stop and the next file to play. If no file is playing, the ```Next``` button does nothing.
* A playing file can be pause and resumed by pressing the ```Play/Pause``` button in the bottom center. If no file is playing, the ```Play/Pause``` button does nothing.
* Playback of a file will continue if the app is not in the foreground. 

Bugs/Limitations
---------
* The ```Previous``` button has not been implemented.
* The files are not displayed and sorted based on metadata.

TODO
-------
* Implement ```Previous``` button.
* Manage files based on metadata.
* Add lock screen widget.
