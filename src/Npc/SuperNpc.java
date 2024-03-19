package Npc;

public class SuperNpc {

    public static String name;
    public static String desc;

    public static void say(String input){
        System.out.println(name+": "+input);
    }
    public static String description(){
        return desc;
    }

}
