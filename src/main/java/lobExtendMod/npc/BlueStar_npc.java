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
import lobExtendMod.monster.BlueStar_m;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.card.rareCard.BlueStar;
import lobotomyMod.npc.AbstractNPC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class BlueStar_npc extends AbstractNPC {
    public AnimatedNpc anim;
    private AnimationState state;
    private int counter, turns;

    public BlueStar_npc(){
        this.ID = BlueStar.ID;
        this.needRemove = false;
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if(room == null){
            return;
        }

        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
        }
        this.counter = 0;
        this.turns = 0;
    }

    public void init(){
        float x = AbstractDungeon.player.drawX - 160.0F * Settings.scale;
        float y = AbstractDungeon.player.drawY;
        this.anim = new AnimatedNpc(x, y, "lobExtendMod/images/monsters/BlueStar/skeleton.atlas", "lobExtendMod/images/monsters/BlueStar/skeleton.json", "Default_inside");
        try {
            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/BlueStar/skeleton.atlas", "lobExtendMod/images/monsters/BlueStar/skeleton.json", 1.2F);

            this.anim.skeleton.setPosition(x, y);
            Field state_t = this.anim.getClass().getDeclaredField("state");
            Field stateData = this.anim.getClass().getDeclaredField("stateData");
            state_t.setAccessible(true);
            stateData.setAccessible(true);
            this.state = (AnimationState)state_t.get(this.anim);
            this.state.setAnimation(0, "Default_inside", true);
            ((AnimationStateData)stateData.get(this.anim)).setMix("Default_inside", "Escape_inside", 0.1F);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        this.needRemove = false;
        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
        }
        this.turns = 0;
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        this.counter ++;
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        this.turns ++;
        if (this.turns >= 6){
            this.needRemove = true;
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new BlueStar_m(AbstractDungeon.player.drawX - 160.0F * Settings.scale, AbstractDungeon.player.drawY, this.counter), false));
        }
    }

    private void changeAnim(){
        this.state.setAnimation(0, "Escape_inside", false);
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
