This project is for visualizing different epidemic models. Its options are managed using the command line.
Run the class `Main` with the option `h` to see help.

The program will generate a network of the specified type, and then simulate an epidemic using the specified model in that network.
It generates images into the specified directory (which must be pre-created).

## Building
I built this using IntelliJ and Gradle. It should be possible to build with any Gradle supporting IDE, but at time of
writing I don't have enough familiarity with Java build systems to say for sure.

## Creating a .gif from output images
This program outputs a series of images, which can then be reconstructed into a GIF. I did so using the unix tool `convert`.
```
convert -delay 100 *.png output.gif
```