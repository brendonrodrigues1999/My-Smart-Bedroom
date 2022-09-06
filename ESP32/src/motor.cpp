#include<Arduino.h>
#include "classes.hpp"
#include <Wire.h>
#include <Adafruit_MotorShield.h>

Adafruit_MotorShield AFMS = Adafruit_MotorShield();
Adafruit_DCMotor *myMotor = AFMS.getMotor(1);



Motor::Motor(int speed){
    _speed = speed;
}

void Motor::begin(){
    AFMS.begin();
    Serial.println("Motor Shield found.");

    // turn on motor M1
    myMotor->setSpeed(_speed);
    myMotor->run(RELEASE);
}

void Motor::open(){
    int i;
myMotor->run(BACKWARD);
    for (i=0; i<1000; i++) {
      myMotor->setSpeed(_speed);
      delay(3);
  }
    for (i=_speed; i!=0; i--) {
      myMotor->setSpeed(i);
      delay(3);
  }
  _status = true;
}

void Motor::close(){
    int i;
    myMotor->run(FORWARD);
     for (i=0; i<1000; i++) {
       myMotor->setSpeed(_speed);
       delay(3);
     }
  
     for (i=_speed; i!=0; i--) {
       myMotor->setSpeed(i);
       delay(3);
     }
     _status = false;
}

bool Motor::state(){
    return _status;
}
