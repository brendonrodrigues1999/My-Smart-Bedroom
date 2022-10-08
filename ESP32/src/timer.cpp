#include<Arduino.h>
#include "classes.hpp"

Timer::Timer(int seconds){
    _seconds = seconds;
}

void Timer::begin(){
    delayStart = millis();   // start delay
    delayRunning = true; // not finished yet
}

bool Timer::timedout(){
    if (delayRunning && ((millis() - delayStart) >= _seconds)) {
        return true;
    }
    else{
        return false;
    }
}

void Timer::stop(){
    delayRunning = false; // // prevent this code being run more then once
}