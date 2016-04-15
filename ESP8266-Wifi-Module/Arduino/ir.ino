#include <IRremoteESP8266.h>
#include <ESP8266WiFi.h>
#include <MQTT.h>
#include <ArduinoJson.h>

#define DEBUG 1
#define MQTT_SERV "192.168.1.15"
#define SEND_PIN 4
#define RECV_PIN 12

IRsend irsend(SEND_PIN);
IRrecv irrecv(RECV_PIN);
decode_results results;

const char *ssid = "GDHome";
const char *pass = "MkYsGa2015";

WiFiClient net;
MQTT myMqtt("", MQTT_SERV, 1883);

void connect();

void setup() {
  Serial.begin(9600);

  WiFi.begin(ssid, pass);

  myMqtt.setClientId("ir");
  myMqtt.onConnected(onConnect);
  myMqtt.onDisconnected(onDisconnect);
  myMqtt.onPublished(onPublish);
  myMqtt.onData(callback);
  
  connect();
  myMqtt.connect();
  delay(500);
  //irsend.begin();
  irrecv.enableIRIn();
  
  
}

bool Send = false;
bool Recv = false;
void loop() {
  // put your main code here, to run repeatedly:

  if (Recv) {
    if(irrecv.decode(&results)) {
        Serial.print("result: ");
        Serial.println(results.value, HEX);
        String s = String(results.value, HEX);
        String& code = s;
        Serial.print("code: ");
        Serial.println(code);
        myMqtt.publish("ir_0", code); 
        receiverEnabled(false);
        irrecv.resume();
    }
  }
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
  myMqtt.subscribe("ir_0");
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
        String msg = "[{\"module\":\"ir_0\", \"type\":\"ir\"}]";  
        myMqtt.publish(topic, msg);
    } else {
        data = data.substring(1, data.length()-1);
      
        StaticJsonBuffer<200> jsonBuffer;
        JsonObject& root = jsonBuffer.parseObject(data);

        const char* command = root["command"];
        
        
        if(command != NULL){
            Serial.print("\tcommand: ");
            Serial.println(command);
            if(!strcmp(command, "ir_receive")) {
                Serial.println("\tEnabling IRReceiver");
                receiverEnabled(true);
            } else if(!strcmp(command, "ir_send")) {
                const char* code = root["code"];
    
                  unsigned long convCode = strtol(code, NULL, 16);
                  IRSend(convCode);
            } else {
                
            }
        }
    }
}

void receiverEnabled(bool t) {
 Recv  = t;
}

void IRSend(unsigned long convCode) {
  //Serial.println(convCode);
  //Serial.println(convCode, HEX);
  irsend.sendNEC(convCode, 36);
  delay(500);
}


