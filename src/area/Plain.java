package area;

import Enemy.Slime;

public class Plain extends SuperArea{

    Plain(){
        name = "Plain";
        desc = "Plains Area, has some Mobs but there is to hunt.";
        spawnRate = 2;
        enemy = new Enemy.SuperEnemy[]{new Slime()};
    }

}
