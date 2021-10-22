package lobExtendMod.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import lobExtendMod.monster.CENSOREDChild;
import lobExtendMod.monster.CENSORED_m;
import lobExtendMod.vfx.CensoredFearAll;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.card.rareCard.CENSORED;
import lobotomyMod.card.rareCard.MeltingLove;
import lobotomyMod.npc.AbstractNPC;

/**
 * @author hoykj
 */
public class CENSORED_npc extends AbstractNPC {
    public AnimatedNpc anim;
    private AnimationState state;
    private int turns;
    private boolean entered;

    public CENSORED_npc(){
        this.ID = CENSORED.ID;
        this.needRemove = false;
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if(room == null){
            return;
        }

        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
            AbstractDungeon.effectsQueue.add(new CensoredFearAll());
        }
        this.turns = 0;
        this.entered = false;
    }

    public void init(){
        float x = Settings.WIDTH / 2.0F - 120 * Settings.scale;
        float y = AbstractDungeon.player.drawY;
        this.anim = new AnimatedNpc(x, y, "lobExtendMod/images/monsters/CENSORED/censored.atlas", "lobExtendMod/images/monsters/CENSORED/censored.json", "Default");
//        try {
//            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
//            loadAnimation.setAccessible(true);
//            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/CENSORED/censored.atlas", "lobExtendMod/images/monsters/MeltingLove/CENSORED/censored.json", 1.0F);
//
//            this.anim.skeleton.setPosition(x, y);
//            Field state_t = this.anim.getClass().getDeclaredField("state");
//            Field stateData = this.anim.getClass().getDeclaredField("stateData");
//            state_t.setAccessible(true);
//            stateData.setAccessible(true);
//            this.state = (AnimationState)state_t.get(this.anim);
//            this.state.setAnimation(0, "Default", true);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        this.needRemove = false;
        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
            this.entered = false;
        }
        this.turns = 0;
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        if (this.turns == 0 && this.entered){
            AbstractDungeon.effectsQueue.add(new CensoredFearAll());
        }
        this.turns ++;
        if (this.turns >= 3){
            this.needRemove = true;
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new CENSORED_m(Settings.WIDTH / 2.0F - 120 * Settings.scale, AbstractDungeon.player.drawY), false));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.anim != null) {
            this.anim.render(sb);
        }
    }

    @Override
    public void dispose() {
        if (this.anim != null) {
            this.anim.dispose();
        }
    }
}
