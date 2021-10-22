package lobExtendMod.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lobExtendMod.monster.VoidDream_m;
import lobotomyMod.npc.AbstractNPC;

/**
 * @author hoykj
 */
public class VoidDream_npc extends AbstractNPC {

    public VoidDream_npc(){
        this.ID = "vdn";
        this.needRemove = false;
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        this.needRemove = true;
        float x = MathUtils.random(200, Settings.WIDTH - 200 * Settings.scale);
        AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new VoidDream_m(x, 0), false));
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {
    }
}
