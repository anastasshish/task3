package org.example.UI;

import java.util.Scanner;

public class GameInputHandler {

    private final Scanner scanner;

    public GameInputHandler() {
        scanner = new Scanner(System.in);
    }


    public String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}