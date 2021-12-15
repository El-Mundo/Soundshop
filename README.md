# Soundshop
This project attempts at applying blurring filters to audio files based on their spectrogram. This project depends on Processing 3.5.4, Minim Audio library, and JH Labs Image Filters.

All used resources are packed as JAVA byte arrays to avoid pre-loading troubles.

-----------------------------------------------------
Setup:
-----------------------------------------------------

1. Check if the device has Java Virtual Machine (JVM) installed.
2. Only the .jar is needed for setting up, which can be found in the release section of this page.
3. Open the .jar file with JVM. If the software still cannot start, please try JVM with a version higher than 1.8.0.
4. Select a .wav or .png file at the beginning page.
5. Select a filter in the second page and drag the bar to apply the filter at certain magnitude.
6. The save section allows saving the results as png or wav files. The saved results will be on the directory of the loaded file.
The wav saving requires to start a recorder and play the audio once, and all input will be ignored during the record.

7. The "LAN bokeh" button will lead to a pop-out window asking to set up network for entering a multi-device synchronizing mode.
To use this mode, please refer to the "v.2.0 New Function" section of this Readme file.
In this mode, only a host can play or stop the audio, while only the clients can edit the audio.
Please also note that, after entering this mode, the user can close the pop-out window to go back to the normal mode.

![image](https://github.com/El-Mundo/Soundshop/blob/master/guide.png)

-----------------------------------------------------
Tips:
-----------------------------------------------------

1. The playback position can be changed by clicking on the spectrogram.
2. Please note that changing a filter will result in the processed image to be staged.
3. There are two settings on the load file scene. The "smooth" decides the anti-aliasing level of images, and the "filter" decides whether the loaded sound samples should be squared to remove noises.

This program loads both audio and image files as pixel arrays, draw them as grey-scale images, and allows the user to apply some image filters (typically blurring) to audio.
For more specified information about the codes, please refer to the top area of the main class (src/base/SpectrogramDrawing.java).


-----------------------------------------------------
v.2.0 New Function (LAN Network Bokeh):
-----------------------------------------------------

<font color=#ff0000>Please note that this function may not work properly on Mac OS due to Apple's limitations.</font>

To use this function, please click the "LAN Bokeh" button on the file selection page.
A Java Window will pop out asking you to input an address and a port.

![image](https://github.com/El-Mundo/Soundshop/blob/master/setlan.png)

There are two modes, host and client.
A host is a must for creating a local network room, no clients can connect to the room without a host.
Clients can connect to the room the host created with the IP address and port number provided by the host.

To create a room as the host, input the number of clients that will connect to your room in the "address/client number" area.
As for port, you can input any integer as long as its length is between 4-5 digits.
However, due to the setting of JAVA's Socket class, you may not always get the port you want.
Please be sure about the number of clients, as you cannot successfully create your room before all expected clients have connected to the room.
Please also note that if there is no new client connecting to your room for 2 minutes before you get all expected clients in your room, the room will be timed out and cancelled.
If your room has been created successfully, your window will show a string like "Server started".

To join a room as a client, please first ensure that your device is in the same Wi-Fi with the host.
Then please input the room host's IP address in the "address/client number" area.
Please refer to the string shown on the host's device, it will show a string like this: "Waiting for client #0 on 192.168.1.1:0000".
The "192.168.1.1" in the sample string will be the IP address and the "0000" is the port number.
Input these information and click the "run as client" button.
If you have connected successfully, the window will show a string like "Just connected to /192.168.1.1:0000".

To cancel a room and go back to normal mode, just close the LAN window.
If you want to keep your room, please leave the pop-out window running in the background.

![image](https://github.com/El-Mundo/Soundshop/blob/master/hostandclient.png)

After you join or create a room, you can click the process button just as the normal mode,
the program will automatically check your connection state and open the editor.
The client can blur their sound with image filters, while the host cannot.
The host can play all the sound clips of the room's clients, but the clients cannot play back them without the host's signal.
Also, the wav and png export is forbidden in the LAN room to avoid synchronization issues.

This design is strongly inspired by how cameras' bokeh works.
The host is considered the camera, so the his/her play-back button is the shutter that controls all the contents.
His/her audio clip is the content that is in focus, so it cannot be blurred, similar to bokeh.
On the other hand, the audio clips of the clients are out of focus and up to be blurred.

This function might also be used for producing computer-based sound installations.
For example, one can put eight computers in the corners of a room and connect them to the same Wi-Fi.
After setting up the computers, s/he can exhibit the sound processed with our editor by clicking the play button of the host computer.
This helps installation designers to easily create surrounding sound effects and blur them.


-----------------------------------------------------
Known issues:
-----------------------------------------------------

1. Audio quality will be extremely poor when converting back an image to audio and the pitch will be shifted due to the normalizing algorithm.
2. Can only load .wav files with standard PCM format.
3. When loading big files, the music will sound strange while reaching MIDI sequencer's end.
4. Buttons can still be triggered when pulling a bar, which may cause an accidental exit.


-----------------------------------------------------
Log:
-----------------------------------------------------

Update v.0.0.1(22/11/2021): Initial version, a console program that down-samples a .wav file.

Update v.0.0.2(10/12/2021): Added resizable GUIs, changeable anti-aliasing, and 8-bit ASCII string display.

Update v.0.0.3(11/12/2021): Added embedded audio effects.

Update v.0.0.4(11/12/2021): Optimized file loading for Mac OS and the implement of byte-audio streaming.

Update v.0.0.6(12/12/2021): Added MIDI streaming and optimized the FFT implement.

Update v.0.0.8(12/12/2021): Added reverse FFT based on Minim Audio.

Update v.0.1.0(13/12/2021): Added support for playing processed samples from image.

Update v.0.1.1(13/12/2021): Added Gaussian blur filter.

Update v.1.0.0(13/12/2021): Added GUI for image filters and optimized the filters' implement. It now allows multi-threading image editing.

Update v.1.0.1(13/12/2021): Added support for wav export.

Update v.1.0.2(13/12/2021): Added motion blur and radial blur based on JH Labs.

Update v.1.0.4(13/12/2021): Added filter switch to determine whether the samples should be squared.

Update v.1.0.5(13/12/2021): Fixed a BUG that the program cannot exit properly when recording wav stream.

Update v.1.0.6(13/12/2021): Fixed the radial blur filter.

Update v.2.0.0(14/12/2021): Added local network connection.

Update v.2.0.1(14/12/2021): Optimized some details related to local network.
