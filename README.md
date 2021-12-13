# Soundshop
This project attempts at applying blurring filters to audio files based on their spectrogram. This project depends on Processing 3.5.4 and Minim Audio library.

All used resources are packed as JAVA byte arrays to avoid pre-loading troubles.

Setup:
1. Check if the device has Java Virtual Machine (JVM) installed, and if the software cannot start, please try JVM with a version higher than 1.8.0.
2. Only the .jar, which can be found in the release section of this page, is needed for using it.
3. Open the .jar file in the release section with JVM.
4. Select a .wav or .png file at the beginning page.
5. Select a filter in the second page and drag the bar to apply the filter at certain magnitude.
6. The playback position can be changed by clicking on the spectrogram.

This program loads both audio and image files as pixel arrays, draw them as grey-scale images, and allows the user to apply some image filters (typically blurring) to audio.
For more specified information about the codes, please refer to the top area of the main class (src/base/SpectrogramDrawing.java).

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
