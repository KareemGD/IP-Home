//ESP8266 Wifi module + Microcontroller
function ReadGasSensor(); //on gas sensor data changed, send GET/POST data to RPi_IP/commands/gas.php?data=[PUT_THE_CHANGED_DATA_HERE]
function ReadMotionSensor(); //same process, change the GET/POST data URL to /motion.php?data=[PUT_THE_CHANGED_DATA_HERE]
function ReadLDR(); //..., /ldr.php?data=[PUT_THE_CHANGED_DATA_HERE]
function IRTransmit(); //A Command that will be sent from the server to the Wifi module in this format -->WiFi_IP/IRT.php?TurnOn=1&TurnOff=0

//Raspberry Pi
UpdateHome.java //A service running on the background checking regularly on the database to execute the commands.
Full Web Design to the main controls over the system.

/var/www/commads/gas.php //execute an Alarm if gas data is leaking.
/var/www/commands/motion.php //in database 'home_db' table 'leds' put 1 or 0 to led_name='junk_room' based on motion data.
/var/www/commands/ldr.php //in database 'home_db' table 'leds' put 1 or 0 to led_name='reception_room' based on ldr data
/var/www/commands/IRT.php //send HTML request to the wifi module with the IRT with the command to be sent to the TV or Air Comditioner
 
//Android
Voice command app Using regular Google Speech API (try to add TTS functionality on executing commands)
commands sent the same way with an HTTP request.. for example: we have a command 'Turn on led'
this is what the HTTP request URL should look like --> RPi_IP/commands/leds.php?data=[on/off] <-- without the brackets.

The voice commands app will be built with WebView layout to be able to import the UI implemented on the Raspberry Pi
directly to the App.
