package main;

import Char.Player;
import Char.Slime;
import Skills.HeavySlash;
import Skills.MagicAttack;
import Skills.SuperSkill;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.Math.round;
import static main.Battle.encounter;

public class Main {
    static Player player = new Player();
    public static void print(String content){
        System.out.println(content);
    }

    public static void saveGame(){
        Scanner scanner = new Scanner(System.in);
        print("Your save file will be called: "+player.name+"-[\"YOUR INPUT\"].sav\nYour Input:");
        String input = scanner.nextLine();
        String savename = player.name+"-"+input+".sav";
        try{
            FileOutputStream fos = new FileOutputStream(savename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(player);
            oos.flush();
            oos.close();
            print("Game Saved as: "+savename);
        }catch (Exception e){
            print("Serialization Error! Can't save data\n"
                    +e.getClass() + ": " + e.getMessage() + "\n");
        }
    }
    public static void loadGame(){
        boolean load = true;
        String savename = "";
        while (load){
            Scanner scanner = new Scanner(System.in);
            print("Name of your Character:");
            String character = scanner.nextLine();
            print("Your input:");
            String input = scanner.nextLine();
            savename = character+"-"+input+".sav";
            print("Is the file called: \""+savename+"\"?");
            String conf = scanner.nextLine();
            if(conf.equals("yes")|| conf.equals("y")){
                load = false;
            }
        }
        try{
            FileInputStream fis = new FileInputStream(savename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            player = (Player) ois.readObject();
            ois.close();
            print("Game Loaded");
        }catch (Exception e){
            print("\n\nNO SUCH FILE AS \""+ savename +"\", ABORTING PROCESS!\n\nSerialization Error! Can't load data\n"
                    +e.getClass() + ": " + e.getMessage() +"\n");
            System.exit(0);
        }
    }

    public static int nextLevel(int level){
        return (int) (level * 30 * 1.1);
    }
    public static void defaultSetup(){
        player.gold = 10;
        player.skills[0] = true;
        player.atk = 15;
        player.def = 15;
        player.hpMax = 20;
        player.hp = 20;
        player.mpMax = 10;
        player.mp = 10;
        player.lvl = 1;
        player.xp = 0;
        player.nextXp = nextLevel(player.lvl);
    }

    public static boolean levelUp(int xpGet){

        int xpTotal = xpGet + player.xp;

        if(xpTotal >= player.nextXp){
            int xpDif = 0;
            while (xpTotal > player.nextXp){
                xpDif = xpTotal - player.nextXp;
                player.lvl = player.lvl + 1;
                xpTotal = xpDif;
                player.nextXp = nextLevel(player.lvl);
            }
            player.xp = xpDif;
            return true;
        }else {
            player.xp = player.xp + xpGet;
            return false;
        }
    }

    public static SuperSkill[] skill = {new MagicAttack(), new HeavySlash()};

    public static void main(String[] args) throws IOException {

        boolean game = true;
        Scanner scanner = new Scanner(System.in);

        print("New Game or Load Game?");

        String gameState = scanner.nextLine();
        gameState = gameState.toLowerCase();

        if (gameState.equals("new") || gameState.equals("new game") || gameState.equals("n")){
            print("Tell me your name.");
            player.name = scanner.nextLine();
            defaultSetup();
        }else if (gameState.equals("load") || gameState.equals("load game") || gameState.equals("l")) {
            loadGame();
        }else{
            print("No Such Command as "+ gameState +".");
            print("Terminating Process.");
            game = false;
        }

        try {
            while (game) {
                print("\n"+ player.name+": Level "+ player.lvl +" "+ player.hp +"/"+ player.hpMax +" HP");
                String command = scanner.nextLine();
                command = command.toLowerCase();

                switch (command){
                    case "quit", "q":
                        print("Bye!");
                        game = false;
                        break;
                    case "savegame", "save":
                        saveGame();
                        break;
                    case "name":
                        print("Your name is " + player.name + ".");
                        break;
                    case "lvl":
                        print("Level: " + player.lvl);
                        break;
                    case "xp", "exp":
                        print("Exp: "+ player.xp + " Exp needed for next level: "+ (player.nextXp - player.xp));
                        break;
                    case "slime":
                        encounter(new Slime());
                        break;
                    case "getxp":
                        String s = (levelUp(10)) ? "level up!" : "not enough...";
                        print(s);
                        break;
                    case "getxps":
                        String st = (levelUp(1000)) ? "level up! Lvl:"+ player.lvl : "not enough...";
                        print(st);
                        break;
                    default:
                        print("There is no such Command as " + command + ".");
                        break;
                }
            }
        } catch(IllegalStateException | NoSuchElementException e) {
            // System.in has been closed
            System.out.println("System.in was closed; exiting");
        }
    }
}