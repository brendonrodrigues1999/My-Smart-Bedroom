#include <Arduino.h>
#include <WiFi.h>
#include "classes.hpp"
#include <Adafruit_Sensor.h>
#include <DHT.h>
#define DHTTYPE DHT22 // DHT 22

#include <Firebase_ESP_Client.h>  
#include "addons/TokenHelper.h" //Provide the token generation process info.
#include "addons/RTDBHelper.h" //Provide the RTDB payload printing info and other helper functions.

//WiFi SSID and password
char ssid[] = "uos-other";             // your network SSID (name)
char pass[] = "shefotherkey05";    // your network password (use for WPA, or use as key for WEP)
void connectToWiFi();

//Firebase
#define API_KEY "AIzaSyC9-biCGCuqMgNltBmNerrhejd1paGbU6g"
#define DATABASE_URL "https://mysmartbedroom-default-rtdb.firebaseio.com" 
#define USER_EMAIL "berodrigues1@sheffield.ac.uk"
#define USER_PASSWORD "123456"
void connectToFirebase();

FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
// Variable to save USER UID
String uid;

Led bedroom_lights(17);
Button button(21);
PIR pir(16);
DHT dht(18, DHTTYPE);
Timer nightLightTimer(10000);
Timer DTHTimer(4000);

void setup(){
  Serial.begin(921600);
  connectToWiFi();
  connectToFirebase();
  bedroom_lights.begin();
  button.begin();
  pir.begin();
  dht.begin();
  DTHTimer.begin();
}

void loop(){
  if(Firebase.RTDB.getString(&fbdo,uid + "/bedroom lights"))
  {
    if(fbdo.stringData()=="on"){
      bedroom_lights.on();
    }else if (fbdo.stringData()=="off"){
      bedroom_lights.off();
    }
  }

  if (button.clicked())
  {
    if (bedroom_lights.status())
    {
      bedroom_lights.off();
      Firebase.RTDB.setString(&fbdo, uid + "/bedroom lights", "off");
    }
    else
    {
      bedroom_lights.on();
      Firebase.RTDB.setString(&fbdo, uid + "/bedroom lights", "on");
    }
  }

  if(pir.motion()){
    if(bedroom_lights.status())
      {
        bedroom_lights.off(); 
      } else 
      {
        bedroom_lights.on();
        nightLightTimer.begin();
      }
  }

  if(nightLightTimer.running()){
    bedroom_lights.off();
    nightLightTimer.stop();
  }

  if(DTHTimer.running()){
    // Read the humidity in %:
    float h = dht.readHumidity();
    // Read the temperature as Celsius:
    float t = dht.readTemperature();
    // Read the temperature as Fahrenheit:
    float f = dht.readTemperature(true);
    // Check if any reads failed and exit early (to try again):
    if (isnan(h) || isnan(t) || isnan(f)) {
      Serial.println("Failed to read from DHT sensor!");
      // return;
    }
    Serial.println("DHT sensor!");
    Firebase.RTDB.setFloat(&fbdo, uid+"/room temperature", t);
    DTHTimer.begin();
  }
}

void connectToWiFi(){
  WiFi.begin(ssid, pass);
  while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print(".");
  } 
  Serial.println("");
  Serial.println("Connected to WiFi");
}

void connectToFirebase() {
  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;
  // Insert Authorized Email and Corresponding Password


  // Assign the user sign in credentials
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
  // Assign the maximum retry of token generation
  config.max_token_generation_retry = 5;
  
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
  fbdo.setResponseSize(4096);

  // Getting the user UID might take a few seconds
  while ((auth.token.uid) == "") {
    Serial.print('.');
  }
  // Print user UID
  uid = auth.token.uid.c_str();
  Serial.print("User UID: ");
  Serial.print(uid);
}