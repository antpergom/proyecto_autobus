#include <Arduino.h>
#include <SimpleDHT.h>
#include "ArduinoJson.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

#include <SoftwareSerial.h>
#define dhtpin 0 //D3
#define ledaire 16 //D0
#define DHTTYPE DHT22
#define echopinE 14 //D5
#define triggerpinE 12 //D6
#define echopinS 13    //D7
#define triggerpinS 15 //D8
//#define idDispositivoAutobus 2

String idDispositivoAutobus = "2";
unsigned long previousMillisTemperatura = 0;
unsigned long previousMillisProximidad = 0;
unsigned long previousMillisOcupacion = 0;
const long intervalTemperatura = 1000;
const long intervalProximidad = 1500;
const long intervalOcupacion =  3000;
float temperature = 0;
float humidity = 0;
int ocupacion = 0;
int capacidad = 0;
bool encendido = false;
bool inicializadoOcupacion = false;
WiFiClient client;
SimpleDHT22 dht22(dhtpin);
int cs = 0;
int ls = 0;
const int distanciaBaseE = 47;
const int distanciaBaseS = 30;
/*Parámetros para la configuración de la conexión*/
String SSID = "MOVISTAR_1ED5";
String PASS = "96833DB4EC51AD839FD9";
String IPSERVER = "192.168.1.38";
int SERVER_PORT = 8080;

/*Declaración de los métodos*/

void sendPostRequestT(float temperatura);
void sendPostRequestH(float humedad);
void sendPostRequestP();
void inicializaLCD();
void actualizaLCD();
void leeTempHum();
void sensorPEntrada();
void sensorPSalida();
void inicializarOcupacion();
int ping(int TriggerPin, int EchoPin);
LiquidCrystal_I2C lcd(0x27, 16, 2);

void setup() {
  
  Serial.begin(115200);
  WiFi.begin(SSID, PASS);
  pinMode(ledaire,OUTPUT);
  pinMode(triggerpinE,OUTPUT);
  pinMode(echopinE,INPUT);
  pinMode(triggerpinS, OUTPUT);
  pinMode(echopinS, INPUT);
  Serial.print("Connecting...");
  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
    if(!inicializadoOcupacion){
      inicializadoOcupacion = true;
      inicializarOcupacion();
    }
    unsigned long currentMillis = millis();
    if(currentMillis - previousMillisProximidad >= intervalProximidad){
      previousMillisProximidad = currentMillis;
      sensorPEntrada();
      delay(5);
      sensorPSalida();
    }
    if(currentMillis - previousMillisTemperatura >= intervalTemperatura){
      previousMillisTemperatura = currentMillis;
      leeTempHum();
    }
    if(currentMillis - previousMillisOcupacion >= intervalOcupacion){
      previousMillisOcupacion = currentMillis;
      sendPostRequestP();
      actualizaLCD();
    }
}
void sendPostRequestT(float temperatura){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, IPSERVER, SERVER_PORT, "/tussam_api/sensores/TEMPERATURA", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["nombre"] = "DHT22ESP";
    doc["valor"] = temperatura;
    doc["precision"] = 1;
    doc["idDispositivoAutobus"] = idDispositivoAutobus;

    String output;
    serializeJson(doc, output);
    int httpCode = http.PUT(output);

    Serial.print("Response code: ");
    Serial.println(httpCode);
    String payload = http.getString();

    // Serial.println("Resultado: " + payload);
  }
}

void sendPostRequestH(float humedad){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, IPSERVER, SERVER_PORT, "/tussam_api/sensores/HUMEDAD", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["nombre"] = "DHT22ESP";
    doc["valor"] = humedad;
    doc["precision"] = 1;
    doc["idDispositivoAutobus"] = idDispositivoAutobus;

    String output;
    serializeJson(doc, output);
    int httpCode = http.PUT(output);

    Serial.print("Response code: ");
    Serial.println(httpCode);
    String payload = http.getString();

    //Serial.println("Resultado: " + payload);
  }

}
void inicializarOcupacion()
{
  if (WiFi.status() == WL_CONNECTED)
  {
    HTTPClient http;
    http.begin(client, IPSERVER, SERVER_PORT, "/tussam_api/dispositivos_autobus/" + idDispositivoAutobus, true);
    int httpCode = http.GET();
    const size_t capacity = JSON_OBJECT_SIZE(7) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    if (httpCode == 200){
      Serial.print("Response code: ");
      Serial.println(httpCode);
      String payload = http.getString();
      //Serial.println("Resultado: " + payload);
      deserializeJson(doc, http.getString());
      JsonObject obj = doc.as<JsonObject>();
      capacidad = obj[String("capacidad")];
      ocupacion = obj[String("ocupacion")];
      Serial.print("La capacidad inicial es de: ");
      Serial.println(capacidad);
      Serial.print("La ocupación inicial es de: ");
      Serial.println(ocupacion);
    }else{
      Serial.print("Response code: ");
      Serial.println(httpCode);
    }
    inicializaLCD();
  }
}
void sendPostRequestP()
{
  if (WiFi.status() == WL_CONNECTED)
  {
    HTTPClient http;
    String newocup = (String)ocupacion;
    http.begin(client, IPSERVER, SERVER_PORT,
               "/tussam_api/dispositivos_autobus/ocupacion/" + newocup +"/"+ idDispositivoAutobus, true);
    int httpCode = http.POST("");
    Serial.print("Response code prox: ");
    Serial.println(httpCode);
    Serial.print("La ocupación es de: ");
    Serial.println(ocupacion);
    //String payload = http.getString();
    //Serial.println(payload);
  }
}

void leeTempHum(){
  int err = SimpleDHTErrSuccess;
  if ((err = dht22.read2(&temperature, &humidity, NULL)) != SimpleDHTErrSuccess)
  {
    Serial.print("DHT22 read failed ");
    Serial.println(err);
  }
  else
  {
    Serial.print("DHT22: ");
    Serial.print((float)temperature);
    Serial.print(" *C, ");
    Serial.print((float)humidity);
    Serial.println(" RH%");
    if (temperature >= 29)
    {
      digitalWrite(ledaire, HIGH);
    }
    else
    {
      digitalWrite(ledaire, LOW);
    }
    sendPostRequestT(temperature);
    delay(10);
    sendPostRequestH(humidity);
  }
}

void inicializaLCD(){
  lcd.begin(16, 2);
  lcd.init();
  lcd.backlight();
  lcd.setCursor(0, 0);
  lcd.print("T:");
  lcd.setCursor(4, 0);
  lcd.print((char)223);
  lcd.setCursor(5, 0);
  lcd.print("C");
  lcd.setCursor(9, 0);
  lcd.print("H:");
  lcd.setCursor(13, 0);
  lcd.print("%RH");
  lcd.setCursor(0, 1);
  lcd.print("Ocupacion:");
  lcd.setCursor(10, 1);
  lcd.print(ocupacion);
}

void actualizaLCD(){
  lcd.setCursor(2,0);
  lcd.print((int)temperature);
  lcd.setCursor(11,0);
  lcd.print((int)humidity);
  lcd.setCursor(10, 1);
  lcd.print(ocupacion);
}
void sensorPEntrada(){
  int cm = ping(triggerpinE, echopinE);
  Serial.print("SensorPE : ");
  Serial.print(cm);
  Serial.println(" cm");
  if(cm <= (distanciaBaseE - (distanciaBaseE/2)))
  {
    ocupacion++;
    if(ocupacion > capacidad)
      ocupacion = capacidad;
    Serial.println("Pasajero Entra");
  }
  delay(1);
}

void sensorPSalida(){
  int cm = ping(triggerpinS, echopinS);
  Serial.print("SensorPS: ");
  Serial.print(cm);
  Serial.println(" cm");
  if (cm <= (distanciaBaseS - (distanciaBaseS / 2)))
  {
    ocupacion --;
    if(ocupacion < 0)
      ocupacion = 0;
    Serial.println("Pasajero Sale");
  }
  delay(1);
}


int ping(int TriggerPin, int EchoPin) {
	long duration, distanceCm;

	digitalWrite(TriggerPin, LOW);  //para generar un pulso limpio ponemos a LOW 4us
	delayMicroseconds(4);
	digitalWrite(TriggerPin, HIGH);  //generamos Trigger (disparo) de 10us
	delayMicroseconds(10);
	digitalWrite(TriggerPin, LOW);

	duration = pulseIn(EchoPin, HIGH);  //medimos el tiempo entre pulsos, en microsegundos

	distanceCm = duration * 10 / 292/ 2;   //convertimos a distancia, en cm
	return distanceCm;
}


