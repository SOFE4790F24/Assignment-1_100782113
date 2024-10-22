import java.io.*;
import java.net.*;
import java.util.Scanner;

public class currencyClient {

    public static void main(String argv[]) throws Exception {
        String address, serverMsg, clientMsg;
        String conversion, amount; //Input to be sent to Server
        Boolean check = false; //user input validation

        //Validate that the IP Address was specified
        if (argv.length != 1) {
            System.err.println(
                    "Usage: java MathClient <IP Address>");
            System.exit(1);
        }

        address = argv[0];
        Socket socket = new Socket(address,1010); //Client socket

        //Input and output streams
        BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter write = new PrintWriter(socket.getOutputStream(), true);

        //To get user input
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        //Welcome message & Conversion type selection
        while ((serverMsg = read.readLine()) != null){
            System.out.println(serverMsg);

            if (serverMsg.contains("Convertir de USD")){ //Once the last line of the prompt is printed, pause for user input
                break;
            }
        }

        //User input for conversion type
        while (!check){
            conversion = input.readLine(); //Get user input

            //When the user wants to exit
            if (conversion.equalsIgnoreCase("close")){ //case insensitive
                System.out.println("The application is now closing... // L'application est en train de fermer...");
                //Close connections
                read.close();
                write.close();
                socket.close();
                return; //Exit
            }

            //Valid input
            else if (conversion.matches("[1-2]")){
                write.println(conversion); //Send selection to server
                check = true;
            }

            //Invalid input
            else{
                System.out.println("Please send a valid selection (1 or 2). // Veuillez envoyer une sélection valide (1 ou 2).");
            }

        }

        //User input for conversion amount
        while ((serverMsg = read.readLine()) != null){
            System.out.println(serverMsg);

            if (serverMsg.contains("Veuillez inscrire le montant")){ //Once the last line of the prompt is printed, pause for user input
                break;
            }
        }

        //User input for conversion amount
        check = false;
        while (!check){
            amount = input.readLine(); //Get user input

            //When the user wants to exit
            if (amount.equalsIgnoreCase("close")){ //case insensitive
                System.out.println("\nThe application is now closing... // L'application est en train de fermer...\n\n");
                //Close connections
                read.close();
                write.close();
                socket.close();
                return; //Exit
            }

            //Valid input
            else if (amount.matches("\\d+\\.\\d{2}")){
                write.println(amount); //Send amount to server
                check = true;
            }

            //Invalid input
            else{
                System.out.println("Please enter a valid value with 2 decimal places (e.g. 45.66). // Veuillez entrer une valeur valide avec 2 décimales (par exemple 45,66).");
            }
        }

        //Result and Closing Message
        while ((serverMsg = read.readLine()) != null){
            System.out.println(serverMsg);
            if (serverMsg.contains("Merci")){ //signaling final message
                break;
            }
        }

        //Close Connections
        System.out.println("\nThe application is now closing... // L'application est en train de fermer...\n\n");
        read.close();
        write.close();
        socket.close();
    }
}