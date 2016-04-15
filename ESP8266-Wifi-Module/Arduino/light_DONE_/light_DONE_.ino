#include <ESP8266WiFi.h>
#include <MQTT.h>
#include <EEPROM.h>

#define DEBUG 1
#define EEP_ADDR 0
#define MQTT_SERV "192.168.43.2"


const char *ssid = "home_ap";
const char *pass = "mkysga2015";

WiFiClient net;
MQTT myMqtt("", MQTT_SERV, 1883);

bool stepOk = false;
unsigned long lastMillis = 0;

void connect(); // <- predefine connect() for setup()

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, pass);
  
  EEPROM.begin(512);
  int pinState = EEPROM.read(EEP_ADDR);

  pinMode(4, OUTPUT);
  digitalWrite(4, pinState);
  
  //client.connect();
  myMqtt.setClientId("light");
  myMqtt.onConnected(onConnect);
  myMqtt.onDisconnected(onDisconnect);
  myMqtt.onPublished(onPublish);
  myMqtt.onData(callback);
  
  connect();
  myMqtt.connect();
  delay(500);
  //myMqtt.subscribe("wifi/light_0");
  waitOk();
  //client.begin("192.168.8.100", net);

  
}

void connect() {
  Serial.print("checking wifi...");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.println("Trying to reconnect in 5 seconds.");
    delay(5000);
  }

  //Serial.print("\nconnecting...");
  //while (!client.connect("arduino")) {
   // Serial.print(".");
  //}

  //Serial.println("\nconnected!");

  //client.subscribe("wifi/light_0");
  // client.unsubscribe("/example");
}

void waitOk()
{
  while(!stepOk)
    delay(100);
 
  stepOk = false;
}

void onConnect() {
#ifdef DEBUG
  Serial.println("connected to MQTT server");
#endif
  myMqtt.subscribe("light_0");
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
#ifdef DEBUG
  Serial.print("Data: ");
  Serial.println(data);
#endif

  if(data == "info request") {
    //send back the last state and
      String state = "";
      if (EEPROM.read(EEP_ADDR) == 1)
        state = "true";
      else
        state = "false";
        
    myMqtt.publish(topic, "[{\"module\":\"light_0\",\"type\":\"switch\",\"last_state\":\""+state+"\"}]");  
  } else if (data == "on") {
    
      digitalWrite(4, HIGH);
      EEPROM.write(EEP_ADDR, 1);
      EEPROM.commit();
      
  } else if (data == "off") {
    
      digitalWrite(4, LOW);
      EEPROM.write(EEP_ADDR, 0); 
      EEPROM.commit();
      
  } else {}
}

void loop() {
  
  //client.loop();
  //delay(10); // <- fixes some issues with WiFi stability

  //if(!client.connected()) {
   // connect();
  //}

  // publish a message roughly every second.
  /*if(millis() - lastMillis > 1000) {
    lastMillis = millis();
    client.publish("wifi/light_0", "world");
  }*/
}

/*void messageReceived(String topic, String payload, char * bytes, unsigned int length) {
  Serial.print("incoming: ");
  Serial.print(topic);
  Serial.print(" - ");
  Serial.print(payload);
  Serial.println();
}*/
