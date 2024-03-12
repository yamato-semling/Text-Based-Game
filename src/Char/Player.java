package Char;

import java.io.Serializable;

public class Player implements Serializable {

    public String name;
    public int xp;
    public int nextXp;
    public int lvl;
    public int atk;
    public int def;
    public int hpMax;
    public int hp;
    public int mpMax;
    public int mp;
    public int gold;
    public boolean[] skills = {true, false};

}
