import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TcpJavaTerm {

    public static void main (String[] args) {
        if(args.length != 2){
            System.out.println("Missing ip address or tcp port");
            System.out.println("Usage is \"TcpJavaTerm <ip address> <tcpPortNumber>\"");
            System.exit(1);
        }


        Socket socket = new Socket();
        //must handle exception if connection cannot be opened
        try {
            //one second timeout
            socket.connect(new InetSocketAddress(args[0], Integer.parseInt(args[1])),1000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataInputStream dataIn = null;
        DataOutputStream dataOut = null;
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner cmdLine = new Scanner(System.in);
        String cmd = "";
        while (!cmd.equals("STOP")){
            cmd = cmdLine.nextLine();

            byte[] asciiBytes = cmd.getBytes(StandardCharsets.US_ASCII);
            //once again java is needy and network exceptions MUST be caught
            try {

                //convert string to bytes and send it
                dataOut.write(asciiBytes, 0,asciiBytes.length);

                if(dataIn.available() > 0){
                    System.out.println("Got something back from connection");
                    while (dataIn.available() >0){
                        System.out.println(dataIn.readByte());
                    }
                    System.out.println("Connection receiving data done");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
