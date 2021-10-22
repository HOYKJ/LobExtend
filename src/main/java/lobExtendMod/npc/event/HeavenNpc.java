package lobExtendMod.npc.event;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import lobExtendMod.event.isolate.AbstractIsolate;
import lobExtendMod.monster.Heaven_m;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class HeavenNpc extends AbstractActionNpc {
    private ArrayList<AnimateEventNpc> npcs = new ArrayList<>();

    public HeavenNpc(){
    }

    @Override
    public void onEnterRoom(AbstractIsolate isolate) {
        super.onEnterRoom(isolate);
        for (int i = 0; i < 8; i ++){
            float x = MathUtils.random(Settings.WIDTH);
            float y = AbstractDungeon.floorY + MathUtils.random(-30.0F, 30.0F);
            switch (MathUtils.random(2)){
                case 0:
                    this.npcs.add(new AnimateEventNpc(x, y,
                            "lobExtendMod/images/monsters/BurrowingHeaven/mustLookDead1.atlas", "lobExtendMod/images/monsters/BurrowingHeaven/mustLookDead1.json",
                            "Default", MathUtils.random(1.8F, 2.2F)));
                    break;
                case 1:
                    this.npcs.add(new AnimateEventNpc(x, y,
                            "lobExtendMod/images/monsters/BurrowingHeaven/mustLook_dead2.atlas", "lobExtendMod/images/monsters/BurrowingHeaven/mustLook_dead2.json",
                            "Default", MathUtils.random(1.0F, 1.2F)));
                    break;
                case 2:
                    this.npcs.add(new AnimateEventNpc(x, y,
                            "lobExtendMod/images/monsters/BurrowingHeaven/mustLook_dead3.atlas", "lobExtendMod/images/monsters/BurrowingHeaven/mustLook_dead3.json",
                            "animation", MathUtils.random(1.0F, 1.2F)));
                    break;
            }
            if (MathUtils.randomBoolean()){
                this.npcs.get(i).flipHorizontal = true;
            }
        }
        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[]{new Heaven_m(-Settings.WIDTH * 0.2F, 0.0F, this)});
        AbstractDungeon.getCurrRoom().rewardAllowed = false;
        isolate.enterCombat();
    }

    @Override
    public void render(SpriteBatch sb, AbstractIsolate isolate) {
        super.render(sb, isolate);
        for (AnimateEventNpc npc : this.npcs){
            npc.render(sb);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (AnimateEventNpc npc : this.npcs){
            npc.dispose();
        }
    }
}
