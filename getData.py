import serial
import pip
installed_packages = pip.get_installed_distributions()
ser = serial.Serial('COM4', 9600)
while True:
   print (ser.readline())