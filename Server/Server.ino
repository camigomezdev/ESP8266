#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>

const char* ssid = "camigomez35";
const char* password = "";

ESP8266WebServer server(80);

//no need authentification
void encenderLED(){
  String message = "Encender LED";
  server.send(200, "text/plain", message);
  digitalWrite(BUILTIN_LED, LOW);  // turn on LED with voltage HIGH
}

void apagarLED(){
  String message = {"Apagar LED"};
  server.send(200, "text/plain", message);
  digitalWrite(BUILTIN_LED, HIGH);  // turn on LED with voltage HIGH
}

void conectado(){
  String message = {"Apagar LED"};
  server.send(200, "text/plain", message);
  digitalWrite(BUILTIN_LED, HIGH);  // turn on LED with voltage HIGH
}

void setup(void){  
  Serial.begin(115200);
  WiFi.begin(ssid, password);
  Serial.println("");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  
  pinMode(BUILTIN_LED, OUTPUT); // Onboard LED
  server.on("/encender", encenderLED);
  server.on("/apagar", apagarLED);
  server.on("/", conectado);

  server.begin();
  Serial.println("HTTP server started");
}

void loop(void){
  server.handleClient();
}
