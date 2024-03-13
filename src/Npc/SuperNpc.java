package Npc;

public class SuperNpc {

    static String name;
    static String desc;

    public static void say(String input){
        System.out.println(name+": "+input);
    }
    public static String description(){
        return desc;
    }

}
