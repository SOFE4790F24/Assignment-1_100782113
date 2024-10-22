import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;

public class currencyServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1010);
            System.out.println("Waiting for Client connections...\n");

            while(true) {
                Socket clientSocket = serverSocket.accept(); //Accept any incoming client connection requests
                System.out.println("Client connected from IP Address: " + clientSocket.getRemoteSocketAddress().toString()); //Print out client and it's IP whenever one is connected

                DataOutputStream write = new DataOutputStream(clientSocket.getOutputStream());
                write.writeBytes("Connected to server...\n"); //Print confirmation message onto client side

                new Dispatcher(clientSocket).start(); //Assign client to dispatcher which will allocate a thread
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class Dispatcher extends Thread {
    private Socket clientSocket;
    private BufferedReader read;
    private PrintWriter write;
    private String number;
    private int conversion;
    private double tempresult, result, num;
    private double USDrate = 1.38;
    private double CADrate = 0.72;
    private boolean stay = true;



    public Dispatcher(Socket socket){
        try {
            this.clientSocket = socket;
            this.read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //input stream for client
            this.write = new PrintWriter(clientSocket.getOutputStream(), true); //output stream for client

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            //Print welcome message on client side
            write.println("Welcome to the Currency Converter System! // Bienvenue dans le système de conversion de devises!\n" +
                    "Please type \"close\" if you would like to quit the application // Veuillez taper \"close\" si vous souhaitez quitter l'application.\n");


            //Currency Conversion Selection
            while (stay) {

                //English
                write.println("Please choose one of the options by entering \"1\" or \"2\".\n" +
                        "1) Convert from CAD -> USD\n" +
                        "2) Convert from USD -> CAD\n\n"+

                //French
                "Veuillez choisir l'une des options en entrant \"1\" ou \"2\".\n" +
                        "1) Convertir de CAD -> USD\n" +
                        "2) Convertir de USD -> CAD\n\n");


                String input = read.readLine(); //Get user's entered input

                //When the user wants to quit
                if (input == null){ //client connection is already closed from client side
                    stay = false;
                    System.out.println("Client disconnected from IP Address: " + clientSocket.getRemoteSocketAddress().toString());
                    break;
                }

                else if (input.matches("[1-2]") ){ //if the user input is 1 or 2
                    conversion = Integer.parseInt(input); //Convert user input to an integer

                    //User choose CAD -> USD
                    if (conversion == 1){
                        write.println("You have selected: CAD -> USD // Vous avez sélectionné: CAD -> USD\n");
                    }

                    //User choose USD -> CAD
                    else if (conversion == 2){
                        write.println("You have selected: USD -> CAD // Vous avez sélectionné: USD -> CAD\n");
                    }
                    break;
                }

                //Check for invalid input
                else {
                    write.println("Please send a valid selection. // Veuillez envoyer une sélection valide.\n");
                }
            }


            //Get User Input for Currency to be Converted
            while (stay){

                //Get user input two numbers (eng, fr)
                if (conversion == 1) {
                    write.println("Please enter the CAD amount to be converted: // Veuillez inscrire le montant de la CAD à convertir:\n"); //CAD -> USD
                }else if (conversion == 2){
                    write.println("Please enter the USD amount to be converted: // Veuillez inscrire le montant de la USD à convertir:\n"); //USD -> CAD
                }

                number = read.readLine(); //Get amount to be converted from user


                //Validate user input

                //When the user wants to quit
                if (number == null){ //client connection is already closed from client side
                    stay = false;
                    System.out.println("Client disconnected from IP Address: " + clientSocket.getRemoteSocketAddress().toString());
                    break;
                }

                //Valid input
                else if (number.matches("\\d+\\.\\d{2}")){ //input should be of double type with 2 decimal places
                    num = Double.parseDouble(number);
                    write.println("The amount to be converted: $" + num +" // Montant à convertir : $" + num + "\n");
                    break;
                }

                //Invalid input
                else{
                    write.println("Please enter a valid value with 2 decimal places (e.g. 45.66). // Veuillez entrer une valeur valide avec 2 décimales (par exemple 45,66).\n");
                }
            }

            //Calculate Conversion & Print Result
            while(stay){

                if (conversion == 1){
                    tempresult = USDrate * num; //calculate using rate
                    BigDecimal result = new BigDecimal(tempresult).setScale(2, RoundingMode.HALF_UP);

                    //Print Result
                    write.println("Given: $" + num + " CAD\n" +
                            "Conversion: $" + result + " USD\n" +
                            "Conversion Rate: " + USDrate + "\n\n" +
                            "Thank you for using Currency Converter!\n\n" +
                            "Compte tenu: $" + num + " CAD\n" +
                            "Conversion: $" + result + " USD\n" +
                            "Taux de conversion: " + USDrate + "\n\n" +
                            "Merci d'utiliser Currency Converter!\n\n");
                } else if (conversion == 2){
                    tempresult = CADrate * num; //calculate using rate
                    BigDecimal result = new BigDecimal(tempresult).setScale(2, RoundingMode.HALF_UP); //Rounding to 2 decimal places

                    //Print Result
                    write.println("Given: $" + num + " USD\n" +
                            "Conversion: $" + result + " CAD\n" +
                            "Conversion Rate: " + CADrate + "\n\n" +
                            "Thank you for using Currency Converter!\n\n"+
                            "Compte tenu: $" + num + " USD\n" +
                            "Conversion: $" + result + " CAD\n" +
                            "Taux de conversion: " + CADrate + "\n\n" +
                            "Merci d'utiliser Currency Converter!\n\n");
                    }

                break; //Run the finally block
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        finally {
                try {
                    clientSocket.close(); //Close client connection
                    System.out.println("Client disconnected from IP Address: " + clientSocket.getRemoteSocketAddress().toString());
                } catch (IOException e){
                    System.out.println(e.getMessage());
            }
        }
    }

}
