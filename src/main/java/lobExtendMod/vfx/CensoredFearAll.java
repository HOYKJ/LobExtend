package lobExtendMod.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobotomyMod.npc.AbstractNPC;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class CensoredFearAll extends AbstractGameEffect {

    public CensoredFearAll(){
        AbstractDungeon.effectsQueue.add(new CensoredFearEffect(AbstractDungeon.player));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
            if (!monster.isDeadOrEscaped()){
                AbstractDungeon.effectsQueue.add(new CensoredFearEffect(monster));
            }
        }
    }

    public void update(){
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    public void dispose(){}
}
