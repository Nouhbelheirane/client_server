import java.io.*;
import java.net.*;

public class MatrixClient {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost"; // Change this to your server's IP address
        int portNumber = 8080; // Change this to your desired port number

        try (
                Socket socket = new Socket(hostName, portNumber);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            // Login or sign up
            boolean loggedIn = false;

            while (!loggedIn) {
                System.out.println("Choose an option:");
                System.out.println("1. Login");
                System.out.println("2. Sign up");

                int option = Integer.parseInt(stdIn.readLine());
                outputStream.writeInt(option);

                if (option == 1) { // Login
                    System.out.print("Username: ");
                    String username = stdIn.readLine();
                    outputStream.writeUTF(username);

                    System.out.print("Password: ");
                    String password = stdIn.readLine();
                    outputStream.writeUTF(password);

                    boolean loginSuccessful = inputStream.readBoolean();

                    if (loginSuccessful) {
                        System.out.println("Login successful");
                        loggedIn = true;
                    } else {
                        System.out.println("Incorrect username or password");
                    }
                } else if (option == 2) { // Sign up
                    System.out.print("Username: ");
                    String username = stdIn.readLine();
                    outputStream.writeUTF(username);

                    System.out.print("Password: ");
                    String password = stdIn.readLine();
                    outputStream.writeUTF(password);

                    boolean signupSuccessful = inputStream.readBoolean();

                    if (signupSuccessful) {
                        System.out.println("Sign up successful");
                        loggedIn = true;
                    } else {
                        System.out.println("Username already taken");
                    }
                }
            }

            // Matrix sum
            while (true) {
                System.out.println("Enter two matrices to add");
                int[][] matrixA = readMatrix(stdIn);
                int[][] matrixB = readMatrix(stdIn);

                outputStream.writeInt(3); // Option for matrix sum
                sendMatrix(matrixA, outputStream);
                sendMatrix(matrixB, outputStream);

                int[][] result = readMatrix(inputStream);
                printMatrix(result);
            }
        }
    }

    private static int[][] readMatrix(BufferedReader stdIn) throws IOException {
        System.out.print("Rows: ");
        int rows = Integer.parseInt(stdIn.readLine());

        System.out.print("Columns: ");
        int columns = Integer.parseInt(stdIn.readLine());

        int[][] matrix = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("Element at row " + i + ", column " + j + ": ");
                matrix[i][j] = Integer.parseInt(stdIn.readLine());
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

    private static void printMatrix(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
