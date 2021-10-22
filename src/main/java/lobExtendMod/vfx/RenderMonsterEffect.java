package lobExtendMod.vfx;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobotomyMod.npc.AbstractNPC;

import java.lang.reflect.Field;

/**
 * @author hoykj
 */
public class RenderMonsterEffect extends AbstractGameEffect {
    private AbstractMonster monster;

    public RenderMonsterEffect(AbstractMonster monster){
        this.monster = monster;
    }

    public void update(){
        if(this.monster.isDeadOrEscaped()){
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        try {
            Field tmp = SuperclassFinder.getSuperclassField(this.monster.getClass(), "skeleton");
            tmp.setAccessible(true);
            Skeleton skeleton = (Skeleton) tmp.get(this.monster);
            sb.end();
            CardCrawlGame.psb.begin();
            AbstractCreature.sr.draw(CardCrawlGame.psb, skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
            sb.setBlendFunction(770, 771);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void dispose(){}
}
