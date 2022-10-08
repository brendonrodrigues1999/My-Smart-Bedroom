#include <Arduino.h>
#include <WiFi.h>
#include "classes.hpp"
#include <Adafruit_Sensor.h>
#include <DHT.h>
#define DHTTYPE DHT22 // DHT 22
#include <Firebase_ESP_Client.h> 
#include "addons/TokenHelper.h" //Provide the token generation process info.
#include "addons/RTDBHelper.h" //Provide the RTDB payload printing info and other helper functions.
#define FIREBASE_USE_PSRAM
#include <fstream>
#include <iostream>  
#include<sstream> 
#include<string>  
using namespace std;  
#if CONFIG_SPIRAM_ALLOW_BSS_SEG_EXTERNAL_MEMORY
  #define EXT_RAM_ATTR _SECTION_ATTR_IMPL(".ext_ram.bss",__COUNTER__)
#else
  #define EXT_RAM_ATTR
#endif

//WiFi SSID and password
char ssid[] = "MAFAir";             // your network SSID (name)
char pass[] = "1357913579";    // your network password (use for WPA, or use as key for WEP)
void connectToWiFi();

//Firebase
#define API_KEY "AIzaSyC9-biCGCuqMgNltBmNerrhejd1paGbU6g"
#define DATABASE_URL "https://mysmartbedroom-default-rtdb.firebaseio.com"
#define USER_EMAIL "i@b.com"
#define USER_PASSWORD "123456"
void connectToFirebase();
 
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
// Variable to save USER UID
String uid;
 
Led bedroom_light(21);
Led AC(16);
Led night_light(19);
Led door_locks(17);
Led music(18);
Button button(10);
PIR night_pir(12);
PIR bedMotionPIR(13);
DHT dht(26, DHTTYPE);
Timer nightLightTimer(10000);
Timer DTHTimer(4000);
Motor curtains(50);

void setup(){
 Serial.begin(921600);
 connectToWiFi();
 connectToFirebase();

 night_light.begin();
 AC.begin();
 bedroom_light.begin();
 button.begin();
 night_pir.begin();
 bedMotionPIR.begin();
 dht.begin();
 DTHTimer.begin();
 curtains.begin();
 door_locks.begin();
 music.begin();
}

bool deepSleep = false;
int sleepMotionCounter = 0;
long sleepMotionTime=0;
float room_temp;
String acTemp;
void loop()
{
  if (Firebase.RTDB.getString(&fbdo, uid+"/Lights"))
  {
     if(fbdo.stringData()=="on"){
       bedroom_light.on();
     }else if (fbdo.stringData()=="off"){
       bedroom_light.off();
     }
 }

//  if (Firebase.RTDB.getString(&fbdo, uid+"/Music"))
//   {
//      if(fbdo.stringData()=="on"){
//        music.on();
//      }
//      else if (fbdo.stringData() == "off")
//      {
//       music.off();
//      }
//  }
 if (Firebase.RTDB.getString(&fbdo, uid+"/Door_Locks"))
  {
     if(fbdo.stringData()=="locked"){
       door_locks.on();
       music.on();
     }
     else if (fbdo.stringData() == "unlocked")
     {
       door_locks.off();
       music.off();
     }
 }
 if (Firebase.RTDB.getString(&fbdo, uid+"/AC"))
  {
     if(fbdo.stringData()!="off"){
       AC.on();
     }
     else if (fbdo.stringData() == "off")
     {
      AC.off();
     }
 }

 String roomTemp;

 if(Firebase.RTDB.getString(&fbdo, uid + "/Curtains")){
  if(fbdo.stringData() == "close" and curtains.state() == true ){
    curtains.close();
  }else if(fbdo.stringData() == "open" and curtains.state() == false ){
    curtains.open();
  }
 }
    
 if(Firebase.RTDB.getString(&fbdo, uid + "/Night_Mode")){
   if(fbdo.stringData() == "on" and sleepMotionTime==0){
     sleepMotionTime = millis();
     Firebase.RTDB.getString(&fbdo, uid + "/AC");
     acTemp = fbdo.stringData();
   }
 }
 if(Firebase.RTDB.getString(&fbdo, uid + "/Night_Mode")){
   if(fbdo.stringData() == "off" and sleepMotionTime!=0){ //nightmode turned off
     sleepMotionTime = 0;
     deepSleep = false;
   }
 }
 if(bedMotionPIR.motion()){ // if motion detected
   Firebase.RTDB.getString(&fbdo, uid + "/Night_Mode");
   if(!deepSleep and fbdo.stringData() == "on") {
     if(millis()-sleepMotionTime > 6000000){ //wait time
       deepSleep = true;
       Firebase.RTDB.setString(&fbdo, uid + "/AC", "off");
     }
   }else if(deepSleep){
     if(millis() - sleepMotionTime < 300000){ //motion timer
       sleepMotionCounter++;
     }else{
       sleepMotionCounter = 0;
     }
   }
   sleepMotionTime = millis();
 }
 if(sleepMotionCounter>10 and roomTemp != acTemp){ //user restless/awake
   deepSleep = false;
   Firebase.RTDB.setString(&fbdo, uid + "/AC", acTemp);
   sleepMotionCounter = 0;
   sleepMotionTime = millis();
 }

 if(night_pir.motion()){
    if(!bedroom_light.status()){
      night_light.on();
      nightLightTimer.begin();
    }
 }
 
 if(nightLightTimer.timedout()){
    night_light.off();
   nightLightTimer.stop();
 }
 
 if(DTHTimer.timedout()){
  // Read the temperature as Celcius:
   room_temp = dht.readTemperature();
   // Check if any reads failed and exit early (to try again):
   if (isnan(room_temp)) {
     Serial.println("Failed to read from DHT sensor!");
     // return;
   }
   Firebase.RTDB.setFloat(&fbdo, uid+"/Temperature", int(room_temp));
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
 Firebase.reconnectWiFi(true);
 // Assign the user sign in credentials
 auth.user.email = USER_EMAIL;
 auth.user.password = USER_PASSWORD;
 /* Assign the callback function for the long running token generation task */
 config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
 // Assign the maximum retry of token generation
 config.max_token_generation_retry = 5;
  Firebase.begin(&config, &auth);
  fbdo.setResponseSize(15000); 
 // Getting the user UID might take a few seconds
 while ((auth.token.uid) == "") {
   Serial.print('.');
 }
 // Print user UID
 uid = auth.token.uid.c_str();
 Serial.print("User UID: ");
 Serial.print(uid);
}
