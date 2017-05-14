//pruebas

void setup()
   {  pinMode(BUILTIN_LED, OUTPUT); // Onboard LED
      Serial.begin(9600);
   }

void loop()
   { Serial.println("Prendido");
     digitalWrite(BUILTIN_LED, LOW);  // turn on LED with voltage HIGH
     delay(2000);                      // wait one second
     Serial.println(BUILTIN_LED);
     digitalWrite(BUILTIN_LED, HIGH);   // turn off LED with voltage LOW
     delay(2000);                      // wait one second
     
   }
