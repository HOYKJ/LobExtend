package lobExtendMod.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.relic.SoundOfStar_r;
import lobotomyMod.npc.AbstractNPC;

/**
 * @author hoykj
 */
public class GainRelicEffect extends AbstractGameEffect {
    private AbstractRelic relic;
    private float x, y;

    public GainRelicEffect(float x, float y, String id){
        this.x = x;
        this.y = y;
        this.relic = RelicLibrary.getRelic(id).makeCopy();
    }

    public void update(){
        if (AbstractDungeon.player.hasRelic(this.relic.relicId)){
            this.isDone = true;
            return;
        }
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.x, this.y,  this.relic);
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    public void dispose(){}
}
