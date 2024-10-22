## Jenani Nedumaran (100782113)

# Currency Converter

The developed application represents a Currency Converter system. The purpose of this application is to provide North American’s with an easy to use currency converter. The multithreaded system allows multiple clients to access the program and compute CAD → USD, or USD → CAD conversions. Additionally, the application has high usability and accessibility given that it is provided in two languages — english and french.

To run the application, proceed to download the two java files. Open two terminal windows in the same directory, and enter the following commands:

## Terminal 1:
1. Compile the Client and Server files
   > javac currencyServer.java currencyClient.java
3. Run the server
   > java currencyServer


## Terminal 2:
1. Run the first client
   > java currencyClient 127.0.0.2


## Terminal 3:
1. Run the second client
   > java currencyClient 127.0.0.5
