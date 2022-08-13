#include<Arduino.h>
#include "classes.hpp"

Led::Led(int pin){
    _pin = pin;
}

void Led::begin(){
    pinMode(_pin, OUTPUT);
    // ledcSetup(ledChannel, freq, resolution);
    // // attach the channel to the GPIO to be controlled
    // ledcAttachPin(_pin, ledChannel);
}

void Led::on(){
    digitalWrite(17, HIGH);
    _status = true;
}

void Led::off(){
    digitalWrite(17, LOW);
    _status = false;
}

bool Led::status(){
    return _status;
}

void Led::fadeIn(){
     // increase the LED brightness
    for(int dutyCycle = 0; dutyCycle <= 255; dutyCycle++){  
        // changing the LED brightness with PWM
        ledcWrite(ledChannel, dutyCycle);
        delay(15);
    }
}

void Led::fadeOut(){
    // decrease the LED brightness
    for(int dutyCycle = 255; dutyCycle >= 0; dutyCycle--){
        // changing the LED brightness with PWM
        ledcWrite(ledChannel, dutyCycle);  
        delay(15);
    }
}