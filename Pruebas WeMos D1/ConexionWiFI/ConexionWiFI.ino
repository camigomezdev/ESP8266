#include <SPI.h>
#include <WiFi.h>

char ssid[] = "CamilaWiFi";
char password[] = "paguedatos";
byte ledPin = 2;

WiFiServer server(1337);

void setup() {
  Serial.begin(115200);
  Serial.println(ssid);
  Serial.println(password);
  
  //WiFi.begin(ssid, password);
  
  // Configure GPIO2 as OUTPUT.  
  pinMode(ledPin, OUTPUT);  
  
  // Start TCP server.
  server.begin();
}

void loop() {
   if (WiFi.status() != WL_CONNECTED) {
    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
    }
    // Print the new IP to Serial. 
    printWiFiStatus();
  } 
}

void printWiFiStatus(){
  WiFiClient client = server.available();
  if (client) {
    Serial.println("Client connected.");
    while (client.connected()) {  
      if (client.available()) {
        char command = client.read(); 
        if (command == 'H') {
          digitalWrite(ledPin, HIGH);
          Serial.println("Led is now on.");
        }
        else if (command == 'L') {
          digitalWrite(ledPin, LOW);
          Serial.println("Led is now off.");
        }        
      }
    }
    Serial.println("Client disconnected.");
    client.stop();
  }
  }
