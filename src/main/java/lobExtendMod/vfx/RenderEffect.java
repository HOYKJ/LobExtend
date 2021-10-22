package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.LobExtendMod;
import lobotomyMod.LobotomyMod;
import lobotomyMod.npc.AbstractNPC;

/**
 * @author hoykj
 */
public class RenderEffect extends AbstractGameEffect {
    private AbstractNPC npc;

    public RenderEffect(AbstractNPC npc){
        this.npc = npc;
    }

    public RenderEffect(AbstractNPC npc, boolean renderBehind){
        this.npc = npc;
        this.renderBehind = renderBehind;
    }

    public void update(){
        if(this.npc.needRemove){
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        //LobExtendMod.logger.info("render");
        this.npc.render(sb);
    }

    public void dispose(){}
}
