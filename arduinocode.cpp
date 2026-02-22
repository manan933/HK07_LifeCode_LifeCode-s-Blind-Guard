#include <Wire.h>
#include <MPU6050.h>
#include <SoftwareSerial.h>
#include <TinyGPS++.h>

MPU6050 mpu;
TinyGPSPlus gps;

// -------- SIM800L --------
SoftwareSerial sim800(A1, A0);  // RX, TX

// -------- GPS --------
SoftwareSerial neo6m(A2, A3);   // RX, TX

// -------- Ultrasonic Pins --------
#define trigF 2
#define echoF 3
#define trigL 4
#define echoL 5
#define trigR 6
#define echoR 7
#define trigD 9
#define echoD 10

// -------- Outputs --------
#define buzzer1 8          // Obstacle buzzer
#define buzzer2 12         // Fall buzzer
#define vibrationPin 13
#define sosButton 11

bool fallDetected = false;
bool sosPressed = false;

// -------- Distance Function --------
int getDistance(int trig, int echo) {
  digitalWrite(trig, LOW);
  delayMicroseconds(2);
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);
  long duration = pulseIn(echo, HIGH, 25000);
  return duration * 0.034 / 2;
}

// -------- Send SMS --------
void sendSMS(String type) {

  String lat = "19.049236";
  String lon = "83.832882";

  unsigned long start = millis();
  while (millis() - start < 5000) {
    while (neo6m.available()) {
      gps.encode(neo6m.read());
    }
    if (gps.location.isValid()) {
      lat = String(gps.location.lat(), 6);
      lon = String(gps.location.lng(), 6);
      break;
    }
  }

  String msg = "BG_ALERT," + type + "," + lat + "," + lon;

  sim800.println("AT");
  delay(1000);
  sim800.println("AT+CMGF=1");
  delay(1000);
  sim800.println("AT+CMGS=\"+919337015264\"");  // Put your number
  delay(1000);
  sim800.print(msg);
  delay(500);
  sim800.write(26); // CTRL+Z
  delay(5000);
}

// -------- Stable Fall Detection --------
void checkFall() {

  int16_t ax, ay, az;
  mpu.getAcceleration(&ax, &ay, &az);

  float totalAcc = sqrt(ax * ax + ay * ay + az * az);

  int fallThreshold = 30000;     // impact
  int uprightThreshold = 17000;  // normal

  if (totalAcc > fallThreshold) {
    if (!fallDetected) {
      fallDetected = true;

      tone(buzzer2, 3500);       // continuous loud alarm
      digitalWrite(vibrationPin, HIGH);

      sendSMS("FALL");
    }
  }

  if (totalAcc < uprightThreshold) {
    fallDetected = false;
    noTone(buzzer2);
    digitalWrite(vibrationPin, LOW);
  }
}

// -------- SOS Button --------
void checkSOS() {
  if (digitalRead(sosButton) == LOW) {
    if (!sosPressed) {
      sosPressed = true;
      sendSMS("SOS");
    }
  } else {
    sosPressed = false;
  }
}

// -------- Setup --------
void setup() {
  Serial.begin(9600);
  sim800.begin(9600);
  neo6m.begin(9600);

  Wire.begin();
  mpu.initialize();

  pinMode(trigF, OUTPUT); pinMode(echoF, INPUT);
  pinMode(trigL, OUTPUT); pinMode(echoL, INPUT);
  pinMode(trigR, OUTPUT); pinMode(echoR, INPUT);
  pinMode(trigD, OUTPUT); pinMode(echoD, INPUT);

  pinMode(buzzer1, OUTPUT);
  pinMode(buzzer2, OUTPUT);
  pinMode(vibrationPin, OUTPUT);

  pinMode(sosButton, INPUT_PULLUP);

  Serial.println("BlindGuard Final System Ready");
}

// -------- LOOP --------
void loop() {

  checkFall();
  checkSOS();

  int dF = getDistance(trigF, echoF);
  int dL = getDistance(trigL, echoL);
  int dR = getDistance(trigR, echoR);
  int dD = getDistance(trigD, echoD);

  int minAlert = 50;      // detection range
  int maxFreq = 3500;     // high pitch
  int minFreq = 800;      // low pitch

  int closest = dF;
  if (dL < closest) closest = dL;
  if (dR < closest) closest = dR;
  if (dD < closest) closest = dD;

  // Obstacle alert only if fall not active
  if (!fallDetected && closest < minAlert) {

    if (closest < 5) closest = 5;

    int freq = map(closest, 5, minAlert, maxFreq, minFreq);
    int beepDelay = map(closest, 5, minAlert, 50, 400);

    tone(buzzer1, freq);
    digitalWrite(vibrationPin, HIGH);
    delay(beepDelay);

    noTone(buzzer1);
    digitalWrite(vibrationPin, LOW);
    delay(beepDelay);
  }
  else if (!fallDetected) {
    noTone(buzzer1);
    digitalWrite(vibrationPin, LOW);
  }

  while (neo6m.available()) {
    gps.encode(neo6m.read());
  }

  delay(10);
}