package lobExtendMod.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.attachments.Attachment;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import lobExtendMod.LobExtendMod;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.relic.Adoration_r;
import lobExtendMod.relic.CENSORED_r;
import lobExtendMod.relic.CrimsonScar_r;
import lobExtendMod.relic.WantedRequest;
import lobExtendMod.vfx.GainRelicEffect;
import lobExtendMod.vfx.MercenaryAxeEffect;
import lobotomyMod.action.common.DelayDamageAction;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.Mercenary;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.power.BleedPower;
import lobotomyMod.vfx.action.LatterEffect;

import java.util.Iterator;

/**
 * @author hoykj
 */
public class Mercenary_m extends AbstractFriendlyMonster {
    public static final String ID = "Mercenary_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Mercenary_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean run, running, right;
    private DelayDamageAction delay;
    //private Attachment axe1, axe2;

    public Mercenary_m(float x, float y, AbstractCreature target) {
        super(NAME, "Mercenary_m", 100, 0.0F, -10.0F, 180.0F, 300.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/Mercenary/skeleton.atlas", "lobExtendMod/images/monsters/Mercenary/skeleton.json", 2.4F);
        if(target == null){
            this.run = false;
            this.running = false;
//            this.randomTarget();
            this.target = null;
            this.drawX = x;
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Default_inside", true);
            e.setTime(e.getEndTime() * MathUtils.random());
        }
        else {
            this.run = true;
            this.running = true;
            this.target = target;
            if(MathUtils.randomBoolean()){
                this.drawX = -200;
                this.right = true;
            }
            else {
                this.drawX = Settings.WIDTH + 200;
                this.right = false;
            }
            this.delay = new DelayDamageAction(target);
            AbstractDungeon.actionManager.addToBottom(this.delay);
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Dash", true);
            e.setTime(e.getEndTime() * MathUtils.random());
        }
        this.drawY = y;

        this.stateData.setMix("Default_inside", "Default_inside_ready_out", 0.1F);
        this.stateData.setMix("Default_out", "Attack_axe", 0.1F);
        this.stateData.setMix("Default_out", "Attack_axe_power", 0.1F);
        this.stateData.setMix("Default_out", "Attack_axe_throw", 0.1F);
        this.stateData.setMix("Default_out", "Attack_gun", 0.1F);
        this.stateData.setMix("Default_out", "Attack_gun_move", 0.1F);
        this.stateData.setMix("Default_out", "Dead", 0.1F);
        this.stateData.setMix("Dash", "Dash_attack", 0.1F);
        this.damage.add(new DamageInfo(this, 10));
        this.damage.add(new DamageInfo(this, 12));
        this.damage.add(new DamageInfo(this, 18));
        this.damage.add(new DamageInfo(this, 7));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "START"));
    }

    protected void getMove(int num) {
        if(num < 30) {
            setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        }
        else if(num < 40) {
            setMove((byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
        }
        else if(num < 60) {
            setMove((byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        }
        else if(num < 90) {
            setMove((byte) 4, Intent.ATTACK, this.damage.get(2).base);
        }
        else {
            setMove((byte) 5, Intent.ATTACK_DEFEND, this.damage.get(3).base, 6, true);
        }
        if(!this.run) {
            this.randomTarget();
        }
        //setMove((byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this, new BleedPower(this.target, 2), 2));
                }, 0.6F));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this, new BleedPower(this.target, 1), 1));
                }, 0.8F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this, new BleedPower(this.target, 1), 1));
                }, 0.6F));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK3"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.effectsQueue.add(new MercenaryAxeEffect(this, this.damage.get(1), this.target));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this, new BleedPower(this.target, 2), 2));
                }));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "GUN"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                }, 0.77F));
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "GUN2"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, 12));
                this.delay = new DelayDamageAction(target);
                final int[] counter = {6};
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    this.state.getTracks().get(0).setListener(new AnimationState.AnimationStateAdapter() {
                        public void event(int trackIndex, Event event) {
                            if (event.getData().getName().equals("Damage") && !delay.isDone) {
                                delay.damage(damage.get(3));
                                counter[0]--;
                                if(counter[0] <= 0){
                                    delay.laterEnd();
                                }
                            }
                        }
                    });
                }));
                AbstractDungeon.actionManager.addToBottom(this.delay);
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "STAND"));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        if(this.currentHealth < 1){
            super.update();
            return;
        }
        float moveSpeed = 100.0F;
        float runSpeed = 400.0F;
        if(this.target == null || this.target.isDeadOrEscaped() || this.target == this){
            if(this.run){
                if(this.isEscaping){
                    if (this.escaped){
                        return;
                    }
                    if(this.right? this.drawX > Settings.WIDTH: this.drawX < 0) {
                        this.escaped = true;
                        AbstractDungeon.player.masterDeck.addToBottom(new Mercenary());
                        Iterator var2;
                        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                            AbstractDungeon.overlayMenu.endTurnButton.disable();
                            var2 = AbstractDungeon.player.limbo.group.iterator();

                            while (var2.hasNext()) {
                                AbstractCard c = (AbstractCard) var2.next();
                                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                            }

                            AbstractDungeon.player.limbo.clear();
                        }
                    }
                }
                else {
                    this.hideHealthBar();
                    this.isEscaping = true;
                    this.running = true;
                    this.right = !this.flipHorizontal;
                    this.changeState("DASH");
                }
            }
            else if(!AbstractDungeon.getMonsters().areMonstersBasicallyDead()){
                this.randomTarget();
                LobExtendMod.logger.info(this.target.id);
            }
        }
        super.update();
        if(this.running){
            if(this.right){
                this.drawX += runSpeed * Gdx.graphics.getDeltaTime();
            }
            else {
                this.drawX -= runSpeed * Gdx.graphics.getDeltaTime();
            }
            if(!this.isEscaping){
                if(this.right){
                    if(this.drawX >= this.target.drawX - 100 && this.state.getTracks().get(0).getAnimation().getName().equals("Dash")){
                        this.changeState("DASH_ATTACK");
                    }
                    else if(this.drawX >= this.target.drawX){
                        this.delay.damage(this.damage.get(0));
                        this.delay.laterEnd();
                    }
                    if (this.drawX < this.hb.width / 2) {
                        this.drawX = this.hb.width / 2;
                    } else if (this.drawX > Settings.WIDTH - this.hb.width / 2) {
                        this.drawX = Settings.WIDTH - this.hb.width / 2;
                    }

                    if (this.drawY < 0) {
                        this.drawY = 0;
                    } else if (this.drawY > Settings.HEIGHT - this.hb.height) {
                        this.drawY = Settings.HEIGHT - this.hb.height;
                    }
                }
                else {
                    if(this.drawX <= this.target.drawX + 100 && this.state.getTracks().get(0).getAnimation().getName().equals("Dash")){
                        this.changeState("DASH_ATTACK");
                    }
                    else if(this.drawX <= this.target.drawX){
                        this.delay.damage(this.damage.get(0));
                        this.delay.laterEnd();
                    }
                    if (this.drawX < this.hb.width / 2) {
                        this.drawX = this.hb.width / 2;
                    } else if (this.drawX > Settings.WIDTH - this.hb.width / 2) {
                        this.drawX = Settings.WIDTH - this.hb.width / 2;
                    }

                    if (this.drawY < 0) {
                        this.drawY = 0;
                    } else if (this.drawY > Settings.HEIGHT - this.hb.height) {
                        this.drawY = Settings.HEIGHT - this.hb.height;
                    }
                }
                if(this.state.getTracks().get(0).getAnimation().getName().equals("Default_out")){
                    this.running = false;
                }
            }
            this.flipHorizontal = !this.right;
        }
        else if(this.state.getTracks().get(0).getAnimation().getName().equals("Attack_gun_move")){
            if(this.right){
                this.drawX += moveSpeed * Gdx.graphics.getDeltaTime();
            }
            else {
                this.drawX -= moveSpeed * Gdx.graphics.getDeltaTime();
            }
            if (this.drawX < this.hb.width / 2) {
                this.drawX = this.hb.width / 2;
            } else if (this.drawX > Settings.WIDTH - this.hb.width / 2) {
                this.drawX = Settings.WIDTH - this.hb.width / 2;
            }

            if (this.drawY < 0) {
                this.drawY = 0;
            } else if (this.drawY > Settings.HEIGHT - this.hb.height) {
                this.drawY = Settings.HEIGHT - this.hb.height;
            }
        }
        else {
            this.right = !this.flipHorizontal;
        }
//        if(this.state.getTracks().get(0).getAnimation().getName().equals("Default_out")){
//            this.skeleton.findSlot("axe_1").setAttachment(this.axe1);
//            this.skeleton.findSlot("axe_2").setAttachment(this.axe2);
//        }
//        if(this.getAttachment("axe_1") != null){
//            LobExtendMod.logger.info((this.skeleton.getX() + this.getBone("Axe_spin").getWorldX()) + "  " + (this.skeleton.getY() + this.getBone("Axe_spin").getWorldY()));
////            this.axe1 = this.getAttachment("axe_1");
////            this.axe2 = this.getAttachment("axe_2");
//        }
    }

    public Attachment getAttachment(String name){
        return this.skeleton.findSlot(name).getAttachment();
    }

    public Bone getBone(String name){
        return this.skeleton.findBone(name);
    }

    public Skeleton getSkeleton(){
        return this.skeleton;
    }

    public void die() {
        super.die();
        this.deathTimer += 6.0F;
        this.changeState("DIE");
        if(!this.run) {
            AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, WantedRequest.ID));
            AbstractDungeon.player.masterDeck.addToBottom(new Mercenary());
        }
        else if(this.target == AbstractDungeon.player){
            AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, CrimsonScar_r.ID));
            AbstractDungeon.player.masterDeck.addToBottom(new Mercenary());
        }
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "Attack_axe", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_out", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "Attack_axe_power", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_out", true, 0.0F);
                break;
            case "ATTACK3":
                this.state.setAnimation(0, "Attack_axe_throw", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_out", true, 0.0F);
                break;
            case "GUN":
                this.state.setAnimation(0, "Attack_gun", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_out", true, 0.0F);
                break;
            case "GUN2":
                this.state.setAnimation(0, "Attack_gun_move", true);
                this.state.setTimeScale(1F);
                break;
            case "STAND":
                this.state.setAnimation(0, "Default_out", true);
                this.state.setTimeScale(1F);
                break;
            case "DASH":
                this.state.setAnimation(0, "Dash", true);
                this.state.setTimeScale(1F);
                break;
            case "DASH_ATTACK":
                this.state.setAnimation(0, "Dash_attack", true);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_out", true, 0.0F);
                break;
            case "START":
                this.state.setAnimation(0, "Default_inside_ready_out", true);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_out", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(this.currentHealth > 0) {
            sb.setColor(Color.WHITE.cpy());
            sb.draw(LobExtendImageMaster.MERCENARY_EYE, this.target.hb.cX - 32, this.target.drawY + this.target.hb.height + 20, 64, 64);
        }
        super.render(sb);
    }
}
