import java.io.*;
import java.net.*;

public class MatrixServer {
    public static void main(String[] args) throws IOException {
        int portNumber = 8080; // Change this to your desired port number
        boolean usernameAndPasswordCorrect = false;
        boolean usernameNotTaken = true;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())) {
            // Login or sign up
            boolean loggedIn = false;
            String username = null;

            while (!loggedIn) {
                int option = inputStream.readInt();

                if (option == 1) { // Login
                    String enteredUsername = inputStream.readUTF();
                    String enteredPassword = inputStream.readUTF();

                    // Check if username and password are correct
                    // ...

                    if (usernameAndPasswordCorrect) {
                        outputStream.writeBoolean(true);
                        loggedIn = true;
                        username = enteredUsername;
                    } else {
                        outputStream.writeBoolean(false);
                    }
                } else if (option == 2) { // Sign up
                    String enteredUsername = inputStream.readUTF();
                    String enteredPassword = inputStream.readUTF();

                    // Check if username is taken
                    // ...

                    if (usernameNotTaken) {
                        outputStream.writeBoolean(true);
                        loggedIn = true;
                        username = enteredUsername;
                    } else {
                        outputStream.writeBoolean(false);
                    }
                }
            }

            // Do whatever you need to do after successful login/signup
            System.out.println("User " + username + " logged in");

            // Matrix sum
            while (true) {
                int option = inputStream.readInt();

                if (option == 3) {
                    int[][] matrixA = readMatrix(inputStream);
                    int[][] matrixB = readMatrix(inputStream);

                    int rows = matrixA.length;
                    int columns = matrixA[0].length;

                    int[][] result = new int[rows][columns];

                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            result[i][j] = matrixA[i][j] + matrixB[i][j];
                        }
                    }

                    sendMatrix(result, outputStream);
                }
            }
        }

    }

    private static int[][] readMatrix(DataInputStream inputStream) throws IOException {
        int rows = inputStream.readInt();
        int columns = inputStream.readInt();

        int[][] matrix = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = inputStream.readInt();
            }
        }

        return matrix;
    }

    private static void sendMatrix(int[][] matrix, DataOutputStream outputStream) throws IOException {
        int rows = matrix.length;
        int columns = matrix[0].length;

        outputStream.writeInt(rows);
        outputStream.writeInt(columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                outputStream.writeInt(matrix[i][j]);
            }
        }
    }
}
