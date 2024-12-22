package university;

import java.io.Console;
import java.io.IOException;

public class ConsoleHelper {
    static long delay = 2000;

    public static void clearScreen() throws IOException {
        for (int i = 0; i < 10;i++){
            System.out.println();
        }
    }

    public static void clearScreenAfterDelay() {
        try {
            Thread.sleep(delay);  // Sleep for the specified time interval (in milliseconds)
            clearScreen();        // Clear the screen after the delay
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

