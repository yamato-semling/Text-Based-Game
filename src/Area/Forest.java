package Area;

import Enemy.LivingTree;
import Enemy.Slime;

public class Forest extends SuperArea{

    public Forest(){
        name = "Forest";
        desc = "Forest Area, greater chance to encounter Mobs.";
        spawnRate = 20;
        enemy = new Enemy.SuperEnemy[]{new Slime(), new LivingTree()};
    }

}
