# Pen-Drive-Security-Linux
Java Applet to secure USB ports on Linux platforms using password authentication

This is a simple implementation for securing USB ports on linux systems. Kindly run the code and then try plugging any usb device, it asks you the password you have set. I have used the bind and unbind command lock and unlock the usb port. As you plug in any device, the running code will detect the used port, retrieve it's device ID and unbind it till you provide it the password. You can also include this script to your shell, so that when the system boots the script starts running automatically and your port are secured. 
