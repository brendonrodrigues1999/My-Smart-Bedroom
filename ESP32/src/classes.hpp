#include<Arduino.h>

class Led{
    public:
        Led(int pin);
        void begin();
        void on();
        void off();
        bool status();
        void fadeIn();
        void fadeOut();

    private:
        int _pin;
        bool _status;
        int freq = 5000;
        int ledChannel = 0;
        int resolution = 8;
};


class Button{
    public:
        Button(int pin);
        void begin();
        bool clicked();
    private:
        int _pin;
        bool _status;
        int ButState;                       // Variable for reading the button status
        int previous = LOW;                  // previous
        long lTime = 0;                      // lTime
        long debounce = 500;                 // debounce
};

class PIR {
    public:
        PIR(int pin);
        void begin();
        bool motion();
    private:
        int _pin;
        bool _status;
};
class Motor {
    public:
        Motor(int speed);
        void begin();
        bool state();
        void open();
        void close();
    private:
        int _speed;
        bool _status;
};

class Timer
{
public:
    Timer(int seconds);
    void begin();
    bool running();
    void stop();

private:
    int _seconds;
    long delayStart;   // start delay
    bool delayRunning; // not finished yet
};
