package Area;

import Enemy.Slime;

public class Plain extends SuperArea{

    public Plain(){
        name = "Plain";
        desc = "Plains Area, has some Mobs but they are easy to hunt.";
        spawnRate = 20;
        enemy = new Enemy.SuperEnemy[]{new Slime()};
    }

}
