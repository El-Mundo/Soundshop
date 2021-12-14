# Soundshop
This project attempts at applying blurring filters to audio files based on their spectrogram. This project depends on Processing 3.5.4, Minim Audio library, and JH Labs Image Filters.

All used resources are packed as JAVA byte arrays to avoid pre-loading troubles.

Setup:
1. Check if the device has Java Virtual Machine (JVM) installed.
2. Only the .jar is needed for setting up, which can be found in the release section of this page.
3. Open the .jar file with JVM. If the software still cannot start, please try JVM with a version higher than 1.8.0.
4. Select a .wav or .png file at the beginning page.
5. Select a filter in the second page and drag the bar to apply the filter at certain magnitude.
6. The save section allows saving the results as png or wav files. The saved results will be on the directory of the loaded file.
The wav saving requires to start a recorder and play the audio once, and all input will be ignored during the record.

![image](https://github.com/El-Mundo/Soundshop/blob/master/guide.png)

Tips:
1. The playback position can be changed by clicking on the spectrogram.
2. Please note that changing a filter will result in the processed image to be staged.
3. There are two settings on the load file scene. The "smooth" decides the anti-aliasing level of images, and the "filter" decides whether the loaded sound samples should be squared to remove noises.

This program loads both audio and image files as pixel arrays, draw them as grey-scale images, and allows the user to apply some image filters (typically blurring) to audio.
For more specified information about the codes, please refer to the top area of the main class (src/base/SpectrogramDrawing.java).

Known issues:
1. Audio quality will be extremely poor when converting back an image to audio and the pitch will be shifted due to the normalizing algorithm.
2. Can only load .wav files with standard PCM format.
3. When loading big files, the music will sound strange while reaching MIDI sequencer's end.
4. Buttons can still be triggered when pulling a bar, which may cause an accidental exit.

Log:

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
