#include <ESP8266WiFi.h>
#include <MQTT.h>
#include <EEPROM.h>

#define DEBUG 1
#define EEP_ADDR 0
#define MQTT_SERV "192.168.43.2"
#define PIR_PIN 4

const char *ssid = "home_ap";
const char *pass = "mkysga2015";

WiFiClient net;
MQTT myMqtt("", MQTT_SERV, 1883);

volatile bool interrupt = false;
bool tmrActive = false;
unsigned long lastMsg = 0;
unsigned long now = 0;
int duration;

void connect();

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, pass);

  EEPROM.begin(512);
  duration = EEPROM.read(EEP_ADDR);
  
  myMqtt.setClientId("motion");
  myMqtt.onConnected(onConnect);
  myMqtt.onDisconnected(onDisconnect);
  myMqtt.onPublished(onPublish);
  myMqtt.onData(callback);
  
  connect();
  myMqtt.connect();
  delay(500);

  //waitOk();

  pinMode(PIR_PIN, INPUT_PULLUP);
  attachInterrupt(
        digitalPinToInterrupt(PIR_PIN),
        motionDetected,
        HIGH);
}

void loop() {
    timer();

    /*if(interrupt){
        #ifdef DEBUG
          Serial.println("Motion Detected: ");
        #endif
        if(!tmrActive){
            String TOPIC = "motion_0";
            String msg = "motion-detected";
            myMqtt.publish(TOPIC, msg);
            lastMsg = millis();
            detachInterrupt(digitalPinToInterrupt(PIR_PIN));
            tmrActive = true;
        }
        interrupt = false;
    }*/
}


void connect() {
  #ifdef DEBUG
    Serial.print("checking wifi...");
  #endif
  while (WiFi.status() != WL_CONNECTED) {
    Serial.println("Trying to reconnect in 5 seconds.");
    delay(5000);
  }
}

void onConnect() {
#ifdef DEBUG
  Serial.println("connected to MQTT server");
#endif
  myMqtt.subscribe("motion_0");
}

void onDisconnect() {
#ifdef DEBUG
  Serial.println("disconnected. try to reconnect...");
#endif
  delay(500);
  myMqtt.connect();
}

void onPublish() {
#ifdef DEBUG  
  Serial.println("published.");
#endif
}

void callback(String& topic, String& data){
  if(data == "info request") {
      String dur = String(duration, DEC);
      String msg = "[{\"module\":\"motion_0\",\"type\":\"sensor\",\"duration\":\""+dur+"\"}]";
      myMqtt.publish(topic, msg);  
      
  } else if (data.substring(0, 8) == "duration") {
      duration = getIntAfterSpace(data);
      //duration = data.toInt();
      EEPROM.write(EEP_ADDR, duration);
      EEPROM.commit();
      
  #ifdef DEBUG
    Serial.print("duration: ");
    Serial.println(duration);
  #endif

  }
}

void motionDetected () {
  //Serial.println("Interrupt");
  if(!tmrActive){
            String TOPIC = "motion_0";
            String msg = "[{\"module\":\""+TOPIC+"\",\"type\":\"sensor\",\"status\":\"motion-detected\"}]";
            myMqtt.publish(TOPIC, msg);
            lastMsg = millis();
            detachInterrupt(digitalPinToInterrupt(PIR_PIN));
            tmrActive = true;
        }
  //interrupt = true;
}


void timer() {
      if(tmrActive) {
          if(duration != 0){
            now = millis();

            if(now - lastMsg == duration * 1000) {
              #ifdef DEBUG
                Serial.print("Now - LastMsg: ");
                Serial.println(now - lastMsg);
              #endif
              delay(50);
              tmrActive = false;
              attachInterrupt(digitalPinToInterrupt(PIR_PIN), motionDetected, HIGH);
            } 
        }
      }
}

int getIntAfterSpace(String& data) {
  int x = data.indexOf(' ');
  String value = "";
  x = x - 1;
  for(x; x < data.length(); x++) {
      if(isDigit(data.charAt(x))) {
          value += data.charAt(x);  
      }
  }
#ifdef DEBUG
  Serial.print("value: ");
  Serial.println(value.toInt());
#endif
  return value.toInt();
}
