package lobExtendMod.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import lobExtendMod.LobExtendMod;
import lobExtendMod.monster.Mercenary_m;
import lobExtendMod.monster.QueenOfHatred_m;
import lobExtendMod.monster.friendlyMonster.QueenOfHatred_f;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.Mercenary;
import lobotomyMod.card.uncommonCard.QueenOfHatred;
import lobotomyMod.npc.AbstractNPC;
import lobotomyMod.vfx.action.LatterEffect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class Mercenary_npc extends AbstractNPC {
    public AnimatedNpc anim;
    private AnimationState state;

    public Mercenary_npc(){
        this.ID = Mercenary.ID;
        this.needRemove = false;
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if(room == null){
            return;
        }

        if(room instanceof MonsterRoom){
            if(room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss){
                this.needRemove = true;
                AbstractMonster m = new Mercenary_m(100.0F * Settings.scale, AbstractDungeon.player.drawY, null);
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(m, false));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(m::usePreBattleAction));
            }
            else {
                init();
                AbstractDungeon.effectsQueue.add(new RenderEffect(this));
            }
        }
    }

    public void init(){
        float x = 100.0F * Settings.scale;
        float y = AbstractDungeon.player.drawY;
        this.anim = new AnimatedNpc(x, y, "lobExtendMod/images/monsters/Mercenary/skeleton.atlas", "lobExtendMod/images/monsters/Mercenary/skeleton.json", "Default_inside");
        try {
            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/Mercenary/skeleton.atlas", "lobExtendMod/images/monsters/Mercenary/skeleton.json", 2.4F);

            this.anim.skeleton.setPosition(x, y);
            Field state_t = this.anim.getClass().getDeclaredField("state");
            Field stateData = this.anim.getClass().getDeclaredField("stateData");
            state_t.setAccessible(true);
            stateData.setAccessible(true);
            this.state = (AnimationState)state_t.get(this.anim);
            this.state.setAnimation(0, "Default_inside", true);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        this.needRemove = false;
        if(room instanceof MonsterRoom){
            if(room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss){
                AbstractDungeon.effectsQueue.add(new LatterEffect(()->{
                    this.needRemove = true;
                    AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new Mercenary_m(100.0F * Settings.scale, AbstractDungeon.player.drawY, null), false));
                }));
            }
            else {
                //init();
                AbstractDungeon.effectsQueue.add(new RenderEffect(this));
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
