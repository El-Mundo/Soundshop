# Soundshop
This project attempts at applying blurring filters to audio files based on their spectrogram. This project depends on Processing 3.5.4 and Minim Audio library.

All used resources are packed as JAVA byte arrays to avoid pre-loading troubles.

To use this software, select a .wav or .png file at the beginning page and select a filter to apply in the second page.
It loads both audio and image files as pixel arrays, draw them as grey-scale images, and allows the user to apply some image filters to audio.
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
