package main;

import Enemy.SuperEnemy;
import Skills.SuperSkill;

import java.util.Scanner;

import static main.Main.*;

public class Battle {

    public static int formulaSF(int num){
        int max = 100;
        int min = 75;
        int range = max - min + 1;

        float random = (int) ((Math.random() * range) + min);
        float res = ((float) num / 100 * random);
        return (int) res;
    }

    public static int dmgCalc(int atk, int def){
        int max = 255;
        int min = 0;
        int range = max - min + 1;

        float random = (int) ((Math.random() * range) + min);

        float a = atk - def / 2;
        float b = (atk -def / 2 + 1) * random;
        float c = b / 256;
        float d = a + c;

        return (int) (d / 4);
    }

    public static void encounter(SuperEnemy enemy){
        print("You see a random "+ enemy.name +" do you want to attack it?\n(yes or no?)");

        Scanner scanner = new Scanner(System.in);
        boolean qstate = true;

        while (qstate){
            String q = scanner.nextLine();
            q = q.toLowerCase();

            if (q.equals("yes") || q.equals("y")){
                qstate = false;
                battle(enemy);
            }else if (q.equals("no") ||q.equals("n")){
                qstate = false;
                print("You decided to not attack the "+ enemy.name +".");
            }else {
                print("Answer with yes or no!");
            }
        }

    }

    public static void battle(SuperEnemy enemy){
        String ename = enemy.name;
        int eatk = enemy.atk;
        int edef = enemy.def;
        int ehpMax = formulaSF(enemy.hp);
        int ehp = ehpMax;
        int empMax = formulaSF(enemy.mp);
        int emp = empMax;

        int pdmg;

        Scanner scanner = new Scanner(System.in);
        boolean battlState = true;
        boolean res = false;
        boolean cF = false;
        print("You decided to attack the "+ ename);

        while (battlState){
            print("\nWhat is your move?");
            String command = scanner.nextLine();
            command = command.toLowerCase();

            switch (command){
                case "move", "moves":
                    print("You can use atk to attack the enemy.");
                    break;
                case "showskills", "showskill", "ss":
                    for (int i = 0; i < skill.length; i++){
                        if (player.skills[i]){
                            SuperSkill s = skill[i];
                            print("\nID:"+(i+1)+"\n"+ s.name+":\n"+s.desc+"\nCost: "+s.consume+" mp.");
                        }
                    }
                    cF = true;
                    break;
                case "atk", "a":
                    pdmg = dmgCalc(player.atk, edef);
                    ehp = ehp - pdmg;
                    print("\nYou dealt "+ pdmg +" damage to the enemy! It has "+ ehp +" hp left.");
                    break;
                case "concentratedattack", "catk", "ca":
                    if (player.mp > 2){
                        int atkBoost = formulaSF(player.atk);
                        int atk = player.atk + atkBoost;
                        pdmg = dmgCalc(atk, edef);
                        ehp = ehp - pdmg;
                        print("You used 2mp to perform a concentrated attack! "+player.mp+"/"+player.mpMax);
                        print("\nYou dealt "+ pdmg +" damage to the enemy! It has "+ ehp +" hp left.");
                    }else {
                        print("You don't have enough mp to do that!");
                    }
                    break;
                case "skill", "s":
                    boolean skillSelect = true;
                    while (skillSelect){
                        print("What skill do you want to use?");
                        int skillId = Integer.parseInt(scanner.nextLine());
                        skillId = skillId - 1;
                        if (!(skillId > (skill.length - 1))){
                            if(player.skills[skillId]){
                                SuperSkill s = skill[skillId];
                                switch (s.type){
                                    case 1:
                                        if(s.consume >= player.mp){
                                            player.mp = player.mp - s.consume;
                                            pdmg = dmgCalc(s.val, edef);
                                            ehp = ehp - pdmg;
                                            print("\nYou used "+s.name+".\nYou dealt "+ pdmg +" damage to the enemy! It has "+ ehp +" hp left.");
                                        }else{
                                            print("You don't have enough mana to use "+ s.name +". "+s.consume+"/"+player.mp+"\nWhat is your move?");
                                            cF = true;
                                        }
                                        break;
                                    default:
                                        print("Error");
                                        cF = true;
                                        break;
                                }
                                skillSelect = false;
                            }else {
                                print("You haven't learnt the skill "+ skill[skillId].name +" yet.\nWhat is your move?");
                                cF = true;
                                skillSelect = false;
                            }
                        }else {
                            print("There is no such skill.\nWhat is your move?");
                            cF = true;
                            skillSelect = false;
                        }
                    }
                    break;
                default:
                    print("No such command as "+ command +".\nWhat is your move?");
                    cF = true;
                    break;
            }
            if(ehp < 1){
                res = true;
                battlState = false;
                ehp = 0;
            }
            if (!res && !cF){
                int edmg = dmgCalc(eatk, player.def);
                player.hp = player.hp - edmg;
                print("The enemy dealt "+ edmg +" damage to you! You have "+ player.hp +" hp left.");
            }
            print("\n"+ player.name+": Level "+ player.lvl +" "+ player.hp +"/"+ player.hpMax +" HP");
            print(ename + ": "+ ehp +"/"+ ehpMax +" HP");
            cF = false;
        }
        if(res){
            print("\nYou won!");
            int xp = formulaSF(ehpMax);
            xp = xp + xp / 3;
            int gold = formulaSF(ehpMax);
            String lvlUp = levelUp(xp) ? "You reached the next level! You are now level "+ player.lvl : "";
            player.gold = player.gold + gold;
            print("Drops:\nExp: "+ xp + " -> " + player.xp +"/"+ player.nextXp +"xp.\nGold: " + gold +" -> "+ player.gold +" Gold.");
            print(lvlUp);
        }else if(player.hp < 1){
            print("\nYou lose!");
        }
    }
}
