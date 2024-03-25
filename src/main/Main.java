package main;

import Enemy.LivingTree;
import Enemy.Slime;
import Enemy.SuperEnemy;
import Npc.Pater;
import Skills.HeavySlash;
import Skills.MagicAttack;
import Skills.SuperSkill;
import Area.Forest;
import Area.Plain;
import Area.SuperArea;

import java.io.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static main.Battle.encounter;

public class Main {
    static Player player = new Player();
    static Pater pater  = new Pater();

    public static int randNum(int min,int max){
        int range = max - min + 1;
        return (int) ((Math.random() * range) + min);
    }
    public static void print(String content){System.out.println(content);}
    public static void space(){System.out.println("\n\n\n");}

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
        boolean will = true;
        boolean z = true;
        String savename = "";
        Scanner scanner = new Scanner(System.in);
        while(z){
            print("Do you want to load a gamefile?");
            String swill = scanner.nextLine();
            if (swill.equalsIgnoreCase("yes") || swill.equalsIgnoreCase("y")){
                z = false;
            }else if (swill.equalsIgnoreCase("no") || swill.equalsIgnoreCase("n")){
                will = false;
                load = false;
                z = false;
            }else {
                print("Not a valid Input: Try \"Yes\" or \"No\".");
            }
        }
        while (load){
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
        if (will){
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
    }

    public static int nextLevel(int level){
        return (int) (level * 30 * 1.1);
    }
    public static int nextStat(int level){
        float n = (float) level / 10;
        float res = n * (500 + randNum(1, 25)) / 255;
        return (int) res;
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
                player.hpMax = player.hpMax + nextStat(player.lvl);
                player.hp = player.hpMax;
                player.mpMax = player.mpMax + nextStat(player.lvl);
                player.mp = player.mpMax;
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
    public static SuperArea[] area = {new Plain(), new Forest()};

    public static SuperArea getArea(){
        int areaNum = 0;
        for (int i = 0; i < player.areas.length; i ++){
            boolean pAreaNum = player.areas[i];
            if (pAreaNum){
                areaNum = i;
            }
        }
        return area[areaNum];
    }



    public static void main(String[] args) throws IOException {

        Commands[] commands;
        commands = new Commands[8];

        commands[0] = new Commands("help", "Shows this menu, with information of commands.");
        commands[1] = new Commands("quit / q", "To quit the game.(Doesn't save.)");
        commands[2] = new Commands("savegame / ssave", "Save the game.");
        commands[3] = new Commands("loadgame / load", "Loads game from savefiles.");
        commands[4] = new Commands("exp / xp","Shows your xp and xp needed for level up.");
        commands[5] = new Commands("look / l","Look around and gather information of your surroundings.");
        commands[6] = new Commands("goto / go","Move to another area.");
        commands[7] = new Commands("move / m","Move and explorer the area.");



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
            space();
            Pater.say(player.name+" you are finally awake.");
            while (game) {
                print("\n"+ player.name+": Level "+ player.lvl +" | "+ player.hp +"/"+ player.hpMax +" HP | "+player.mp+"/"+player.mpMax+" MP");
                String command = scanner.nextLine();
                command = command.toLowerCase();

                switch (command){
                    case "quit", "q":
                        print("Bye!");
                        game = false;
                        break;
                    case "help", "h":
                        for (Commands value : commands) {
                            value.display();
                        }
                        break;
                    case "savegame", "save":
                        saveGame();
                        break;
                    case "loadgame", "load":
                        loadGame();
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
                    case "move", "m":
                        SuperArea pArea = getArea();
                        print("You move in the "+ pArea.name +".");
                        int encChance = randNum(1, 100);
                        if (encChance <= pArea.spawnRate){
                            int enemyLen = pArea.enemy.length - 1;
                            int enemyNum = randNum(0, enemyLen);
                            SuperEnemy enemy = pArea.enemy[enemyNum];
                            encounter(enemy);
                        }
                        break;
                    case "look", "l":
                        print(getArea().desc);
                        break;
                    case "go", "goto":
                        print("Where to?");
                        boolean loc = true;
                        while (loc) {
                            String location = scanner.nextLine();
                            location = location.toLowerCase();
                            switch (location){
                                case "plain", "plains":
                                    Arrays.fill(player.areas, false);
                                    player.areas[0] = true;
                                    loc = false;
                                    break;
                                case "forest":
                                    Arrays.fill(player.areas, false);
                                    player.areas[1] = true;
                                    loc = false;
                                    break;
                                default:
                                    print("No such Area!");
                            }
                        }
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

    static class Commands {
        public String name;
        public String info;

        Commands( String name, String info)
        {
            this.name = name;
            this.info = info;

        }

        public void display()
        {
            print(name+":\n"+info+"\n\n");
        }
    }

}