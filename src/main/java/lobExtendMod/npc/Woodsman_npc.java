package lobExtendMod.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import lobExtendMod.monster.Woodsman_m;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.card.uncommonCard.Woodsman;
import lobotomyMod.npc.AbstractNPC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class Woodsman_npc extends AbstractNPC {
    public AnimatedNpc anim;
    private AnimationState state;

    public Woodsman_npc(){
        this.ID = Woodsman.ID;
        this.needRemove = false;
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if(room == null){
            return;
        }

        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this));
        }
    }

    public void init(){
        float x = 240.0F * Settings.scale;
        float y = AbstractDungeon.player.drawY;
        this.anim = new AnimatedNpc(x, y, "lobExtendMod/images/monsters/Woodsman/Lumberjack.atlas", "lobExtendMod/images/monsters/Woodsman/Lumberjack.json", "Room_Default");
        try {
            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/Woodsman/Lumberjack.atlas", "lobExtendMod/images/monsters/Woodsman/Lumberjack.json", 1.4F);

            this.anim.skeleton.setPosition(x, y);
            Field state_t = this.anim.getClass().getDeclaredField("state");
            Field stateData = this.anim.getClass().getDeclaredField("stateData");
            state_t.setAccessible(true);
            stateData.setAccessible(true);
            this.state = (AnimationState)state_t.get(this.anim);
            this.state.setAnimation(0, "Room_Default", true);
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
            AbstractDungeon.effectsQueue.add(new RenderEffect(this));
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
            if (monster.currentHealth <= 10){
                this.needRemove = true;
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new Woodsman_m(240.0F * Settings.scale, AbstractDungeon.player.drawY, monster), false));
                break;
            }
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
