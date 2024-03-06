package main;

import Char.SuperChar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    static SuperChar player = new SuperChar();

    public static void print(String content){
        System.out.println(content);
    }

    public static void main(String[] args) throws IOException {

        boolean game = true;
        Scanner scanner = new Scanner(System.in);

        print("Tell me your name.");
        player.name = scanner.nextLine();

        try {
            while (game) {
                print(player.name+":");
                String command = scanner.nextLine();

                switch (command){
                    case "quit", "q":
                        print("Bye!");
                        game = false;
                        break;
                    case "name":
                        print("Your name is " + player.name + ".");
                        break;
                    default:
                        print("There is no such Command as " + command + ".");
                }
            }
        } catch(IllegalStateException | NoSuchElementException e) {
            // System.in has been closed
            System.out.println("System.in was closed; exiting");
        }
    }
}