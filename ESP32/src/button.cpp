#include<Arduino.h>
#include "classes.hpp"

Button::Button(int pin){
    _pin = pin;
}

void Button::begin(){
    pinMode(_pin, INPUT);
}

bool Button::clicked(){
    ButState = digitalRead(_pin);
    if (ButState == HIGH && previous == LOW && millis() - lTime > debounce) 
    {
        _status = true;
        lTime = millis();
    } else{
        _status = false;
    }
    previous == ButState;
    return _status;
}
