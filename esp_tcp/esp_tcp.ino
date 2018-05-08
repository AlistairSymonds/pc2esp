#include <ESP8266WiFi.h>

//fill in with relevant wifi details
const char* ssid = "symnet01";
const char* password = "GreenM00n";


//use this as port in TcpJavaTerm
static int tcpPort = 2812;
WiFiServer server(tcpPort);

//if you want to service multiple people at once
//use an array, just one for the example though
WiFiClient theOneClient;




void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED){
    {
      delay(500);
      Serial.print(".");
    }
    Serial.println();

    
    
  }
  //use this IP address in TcpJavaTerm
  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());  

  server.begin();
  Serial.println("TCP server has begun and is ready to accept connections");
}

void loop() {
  //returns new client if available
  WiFiClient newClient = server.available();
  
  //will return true if connected and false if not
  //however if a client has unread data in its input buffer
  //it will always return true even if the tcp connection has been closed
  if(newClient){
    theOneClient = newClient;
  }
  
  
  if(theOneClient.available() > 0){

      
      Serial.println("Data available");
      //handle data if any is available
      //and by handle we're just printing it out
      while(theOneClient.available() > 0){
        byte data = theOneClient.read();
        Serial.print(data);
        Serial.print(" ");
      }
      Serial.println();
      //some random data to send back
      byte finishedReadingByte = 0x23;
      theOneClient.write(finishedReadingByte);
      Serial.println("Transmission done");     
        
  }else{
    //Serial.println("No client");
  }
}
