package lobExtendMod.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import lobExtendMod.monster.HeroicMonk_m;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.npc.AbstractNPC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class HeroicMonk_npc extends AbstractNPC {
    public AnimateEventNpc anim;
    private AnimationState state;
    private int counter;

    public HeroicMonk_npc(){
        this.ID = "hmn";
        this.needRemove = false;
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if(room == null){
            return;
        }

        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
        }
        this.counter = 3;
    }

    public void init(){
        float x = AbstractDungeon.player.drawX - 200.0F * Settings.scale;
        float y = AbstractDungeon.floorY;
        this.anim = new AnimateEventNpc(x, y, "lobExtendMod/images/monsters/HeroicMonk/TreeTree.atlas", "lobExtendMod/images/monsters/HeroicMonk/TreeTree.json", "0_Default", 2.0F);
        try {
            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/HeroicMonk/TreeTree.atlas", "lobExtendMod/images/monsters/HeroicMonk/TreeTree.json", 2.0F);

            this.anim.skeleton.setPosition(x, y);
            Field state_t = this.anim.getClass().getDeclaredField("state");
            Field stateData = this.anim.getClass().getDeclaredField("stateData");
            state_t.setAccessible(true);
            stateData.setAccessible(true);
            this.state = (AnimationState)state_t.get(this.anim);
            this.state.setAnimation(0, "0_Default", true);
            ((AnimationStateData)stateData.get(this.anim)).setMix("0_Default", "1_Transformation", 0.1F);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        this.anim.flipHorizontal = true;
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        this.needRemove = false;
        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        this.counter --;
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        if (this.counter <= 0){
            this.counter = 99;
            AbstractDungeon.actionManager.addToBottom(new LatterAction(this::changeAnim));
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                this.needRemove = true;
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new HeroicMonk_m(AbstractDungeon.player.drawX - 200.0F * Settings.scale, 0), false));
            }, 3.03F));
        }
    }

    private void changeAnim(){
        this.state.setAnimation(0, "1_Transformation", false);
        this.state.setTimeScale(1F);
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
