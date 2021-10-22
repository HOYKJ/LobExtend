package lobExtendMod.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import lobExtendMod.LobExtendMod;
import lobExtendMod.monster.CENSORED_m;
import lobExtendMod.monster.DespairKnight_m;
import lobExtendMod.monster.Mercenary_m;
import lobExtendMod.power.KnightBlessPower;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.DespairKnight;
import lobotomyMod.card.uncommonCard.Mercenary;
import lobotomyMod.npc.AbstractNPC;
import lobotomyMod.vfx.action.LatterEffect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class DespairKnight_npc extends AbstractNPC {
    public AnimatedNpc anim;
    private AnimationState state;
    private int lostHP;
    private boolean active;

    public DespairKnight_npc(){
        this.ID = DespairKnight.ID;
        this.needRemove = false;
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if(room == null){
            return;
        }

        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this));
            this.active = true;
            this.lostHP = 1;
        }
    }

    public void init(){
        float x = AbstractDungeon.player.drawX - 240.0F * Settings.scale;
        float y = AbstractDungeon.player.drawY;
        this.anim = new AnimatedNpc(x, y, "lobExtendMod/images/monsters/DespairKnight/magicalGirl3.atlas", "lobExtendMod/images/monsters/DespairKnight/magicalGirl3.json", "Default_Normal");
        try {
            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/DespairKnight/magicalGirl3.atlas", "lobExtendMod/images/monsters/DespairKnight/magicalGirl3.json", 2.0F);

            this.anim.skeleton.setPosition(x, y);
            Field state_t = this.anim.getClass().getDeclaredField("state");
            Field stateData = this.anim.getClass().getDeclaredField("stateData");
            state_t.setAccessible(true);
            stateData.setAccessible(true);
            this.state = (AnimationState)state_t.get(this.anim);
            this.state.setAnimation(0, "Default_Normal", true);
            //((AnimationStateData)stateData.get(this.anim)).setMix("Default_Normal", "Default_Special", 0.1F);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        this.needRemove = false;
        if(room instanceof MonsterRoom){
            AbstractDungeon.effectsQueue.add(new RenderEffect(this));
            this.active = true;
            this.lostHP = 1;
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        if (this.lostHP == 0 && this.active){
            this.active = false;
            AbstractDungeon.actionManager.addToBottom(new LatterAction(this::changeAnim));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new KnightBlessPower(AbstractDungeon.player, this)));
        }
        this.lostHP = 0;
    }

    @Override
    public void onLoseHp(int damageAmount) {
        super.onLoseHp(damageAmount);
        this.lostHP += damageAmount;
    }

    public void change(){
        this.needRemove = true;
        AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new DespairKnight_m(AbstractDungeon.player.drawX - 240.0F * Settings.scale, AbstractDungeon.player.drawY), false));
    }

    private void changeAnim(){
        this.state.setAnimation(0, "Default_Special", false);
        this.state.setTimeScale(1F);
        this.state.addAnimation(0, "Default_Normal", true, 0.0F);
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
