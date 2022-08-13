#include<Arduino.h>
#include "classes.hpp"

PIR::PIR(int pin){
    _pin = pin;
}

void PIR::begin(){
    pinMode(_pin, INPUT);
}

bool PIR::motion(){
    // The OpenPIR's digital output is active high
    int motionStatus = digitalRead(_pin);

    // If motion is detected, turn the onboard LED on:
    if (motionStatus == HIGH)
        _status = true;
    else // Otherwise turn the LED off:
        _status = false;

    return _status;
}