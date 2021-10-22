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
import lobExtendMod.monster.QueenOfHatred_m;
import lobExtendMod.monster.friendlyMonster.QueenOfHatred_f;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.QueenOfHatred;
import lobotomyMod.npc.AbstractNPC;
import lobotomyMod.vfx.action.LatterEffect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class QueenOfHatred_npc extends AbstractNPC {
    public Hitbox hb = new Hitbox(200.0F * Settings.scale, 240.0F * Settings.scale);
    public AnimatedNpc anim;
    private int animState;
    private float animTimer;
    private AnimationState state;
    private int QC;

    public QueenOfHatred_npc(){
        this.ID = QueenOfHatred.ID;
        this.needRemove = false;
        this.QC = 0;
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if(room == null){
            return;
        }

        if(room instanceof MonsterRoom){
            if(room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss){
                this.needRemove = true;
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new QueenOfHatred_f(0, 0, this), false));
            }
            else {
                init();
                this.animState = 0;
                AbstractDungeon.effectsQueue.add(new RenderEffect(this));
            }
        }
    }

    public void init(){
        float x = AbstractDungeon.player.drawX - 240.0F * Settings.scale;
        float y = AbstractDungeon.player.drawY;
        this.anim = new AnimatedNpc(x, y, "lobExtendMod/images/monsters/QueenOfHatred/normal/Magic_circle.atlas", "lobExtendMod/images/monsters/QueenOfHatred/normal/Magic_circle.json", "0_Default_escape_too");
        try {
            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/QueenOfHatred/normal/Magic_circle.atlas", "lobExtendMod/images/monsters/QueenOfHatred/normal/Magic_circle.json", 2.4F);

            this.anim.skeleton.setPosition(x, y);
            Field state_t = this.anim.getClass().getDeclaredField("state");
            Field stateData = this.anim.getClass().getDeclaredField("stateData");
            state_t.setAccessible(true);
            stateData.setAccessible(true);
            this.state = (AnimationState)state_t.get(this.anim);
            this.state.setAnimation(0, "0_Default_escape_too", true);
            ((AnimationStateData)stateData.get(this.anim)).setMix("0_Default_escape_too", "0_Default_4_special", 0.1F);
            ((AnimationStateData)stateData.get(this.anim)).setMix("0_Default_4_special", "0_Default_escape_too", 0.2F);
            ((AnimationStateData)stateData.get(this.anim)).setMix("0_Default_4_special", "0_Default_2", 0.1F);
            ((AnimationStateData)stateData.get(this.anim)).setMix("0_Default_escape_too", "0_Default_3", 0.1F);
            ((AnimationStateData)stateData.get(this.anim)).setMix("0_Default_escape_too", "1_Default_to_Panic_Default", 0.1F);
            ((AnimationStateData)stateData.get(this.anim)).setMix("1_Default_to_Panic_Default", "2_Panic_Default", 0.1F);
            ((AnimationStateData)stateData.get(this.anim)).setMix("2_Panic_Default", "3_Panic_Default_to_default", 0.1F);
            ((AnimationStateData)stateData.get(this.anim)).setMix("2_Panic_Default", "4_transform", 0.1F);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        this.hb.move(x, y + 120.0F * Settings.scale);
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        this.needRemove = false;
        if(room instanceof MonsterRoom){
            if(room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss){
                AbstractDungeon.effectsQueue.add(new LatterEffect(()->{
                    this.needRemove = true;
                    AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new QueenOfHatred_f(0, 0, this), false));
                }));
            }
            else {
                //init();
                AbstractDungeon.effectsQueue.add(new RenderEffect(this));
            }
        }
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if(this.QC == 2){
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                this.changeAnim(4);
            }));
        }
        else if(this.QC == 3){
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                this.changeAnim(99);
            }));
        }
    }

    @Override
    public void onLoseHp(int damageAmount) {
        super.onLoseHp(damageAmount);
        this.QC = 0;
        if(this.animState == 4){
            this.changeAnim(5);
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
            this.changeAnim(3);
        }));
    }

    @Override
    public void onVictory() {
        super.onVictory();
        this.QC ++;
    }

    @Override
    public void update() {
        this.hb.update();
        //LobExtendMod.logger.info("update");
        if(animTimer > 0){
            this.animTimer -= Gdx.graphics.getDeltaTime();
            return;
        }
        if(this.animState == 3){
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 6));
            this.animState = 0;
        }
        else if(this.animState == 5){
            this.animState = 0;
        }
        else if(this.animState == 99){
            this.needRemove = true;
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new QueenOfHatred_m(-240, 20), false));
        }
        if(this.hb.hovered){
            if(this.animState < 1){
                this.changeAnim(1);
            }
            if (InputHelper.justClickedLeft) {
                InputHelper.justClickedLeft = false;
                this.hb.clickStarted = true;
            }
            if (this.hb.clicked) {
                this.changeAnim(2);
                LobExtendMod.logger.info("clicked");
                this.hb.clicked = false;
                //this.hb.hovered = false;
            }
        }
        else {
            if(this.animState > 0){
                this.changeAnim(0);
            }
        }
    }

    private void changeAnim(int code){
        if(this.animState == 4 && (code != 5 && code != 99)){
            return;
        }
        if(this.animTimer > 0){
            return;
        }
        LobExtendMod.logger.info("change animation: " + code);
        this.animState = code;
        switch (code){
            case 0:
                this.state.setAnimation(0, "0_Default_escape_too", true);
                this.state.setTimeScale(1F);
                break;
            case 1:
                this.state.setAnimation(0, "0_Default_4_special", true);
                this.state.setTimeScale(1F);
                break;
            case 2:
                this.state.setAnimation(0, "0_Default_2", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default_4_special", true, 0.0F);
                this.animTimer = 2;
                break;
            case 3:
                this.state.setAnimation(0, "0_Default_3", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default_escape_too", true, 0.0F);
                this.animTimer = 7;
                break;
            case 4:
                this.anim.skeleton.findSlot("mouth/smile").setAttachment(null);
                this.state.setAnimation(0, "1_Default_to_Panic_Default", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "2_Panic_Default", true, 0.0F);
                break;
            case 5:
                this.state.setAnimation(0, "3_Panic_Default_to_default", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default_escape_too", true, 0.0F);
                this.animTimer = 4.7F;
                break;
            case 99:
                this.state.setAnimation(0, "4_transform", false);
                this.state.setTimeScale(1F);
                this.animTimer = 7.7F;
                break;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.anim != null) {
            this.anim.render(sb);
        }
        this.hb.render(sb);
    }

    @Override
    public void dispose() {
        if (this.anim != null) {
            this.anim.dispose();
        }
    }
}
