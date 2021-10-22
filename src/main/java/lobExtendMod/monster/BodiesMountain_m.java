package lobExtendMod.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.LobExtendMod;
import lobExtendMod.action.CensoredAttackAction;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.npc.BodiesMountain_npc_tmp;
import lobExtendMod.npc.CENSORED_npc;
import lobExtendMod.relic.CENSORED_r;
import lobExtendMod.relic.Lamp_r;
import lobExtendMod.relic.TheSmile_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.rareCard.BodiesMountain;
import lobotomyMod.card.rareCard.CENSORED;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.relic.CogitoBucket;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class BodiesMountain_m extends AbstractFriendlyMonster {
    public static final String ID = "BodiesMountain_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BodiesMountain_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private int escapeTurns, turns, bodies, stage;
    private boolean comeToEat, running;
    private float targetX;
    private AbstractMonster eatTarget;

    public BodiesMountain_m(float x, float y, AbstractMonster eatTarget) {
        super(NAME, "BodiesMountain_m", 50, 0.0F, -4.0F, 280.0F, 300.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/BodiesMountain/mouth monster.atlas", "lobExtendMod/images/monsters/BodiesMountain/mouth monster.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Level1_Walk", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Level1_Walk", "Level1_Attack", 0.1F);
        this.stateData.setMix("Level1_Walk", "Level2_Change", 0.1F);
        this.stateData.setMix("Level1_Walk", "Dead", 0.1F);
        this.stateData.setMix("Level2_Walk", "Level2_Attack", 0.1F);
        this.stateData.setMix("Level2_Walk", "Level3_Change", 0.1F);
        this.stateData.setMix("Level3_Walk", "Level3_Attack_01", 0.1F);
        this.stateData.setMix("Level3_Walk", "Level3_Attack_02", 0.1F);
        this.stateData.setMix("Level3_Walk", "Level3_Attack_03", 0.1F);
        this.damage.add(new DamageInfo(this, 6));
        this.damage.add(new DamageInfo(this, 20));
        this.damage.add(new DamageInfo(this, 15));
        this.damage.add(new DamageInfo(this, 40));
        this.drawX = x;
        this.drawY = y;
        this.escapeTurns = 1;
        this.comeToEat = true;
        this.running = true;
        this.eatTarget = eatTarget;
        this.stage = 1;
    }

    protected void getMove(int num) {
        LobExtendMod.logger.info("get move: " + this.bodies);
        if (this.bodies >= 6){
            this.stage = 3;
            if (num > 60){
                setMove((byte) 32, Intent.ATTACK, this.damage.get(3).base);
            }
            else {
                setMove((byte) 31, Intent.ATTACK, this.damage.get(2).base);
            }
        }
        else if (this.bodies >= 3){
            this.stage = 2;
            setMove((byte) 21, Intent.ATTACK, this.damage.get(1).base);
        }
        else {
            setMove((byte) 11, Intent.ATTACK, this.damage.get(0).base);
        }

        if (this.turns >= this.escapeTurns){
            setMove((byte) 0, Intent.ESCAPE);
        }
        this.turns ++;
        this.randomTarget();
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 0:
                this.hideHealthBar();
                this.isEscaping = true;
                break;
            case 11:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    CardCrawlGame.sound.play("Danggo_Lv1_Atk1");
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }, 0.5F));
                break;
            case 21:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
                AbstractDungeon.effectList.add(new LatterEffect(()->{
                    CardCrawlGame.sound.play("Danggo_Lv2");
                }, 0.7F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }, 2.3F));
                break;
            case 31:
                if (MathUtils.randomBoolean()) {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK3_1"));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK3_2"));
                }
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    CardCrawlGame.sound.play("Danggo_Lv3_Atk");
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }, 0.8F));
                break;
            case 32:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK3_3"));
                AbstractDungeon.effectList.add(new LatterEffect(()->{
                    CardCrawlGame.sound.play("Danggo_Lv3_Special");
                }, 0.5F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }, 1.6F));
                break;
        }
        this.eatBody();
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped() || this.target == this) {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.randomTarget();
            }
        }
        super.update();
        if (this.isEscaping){
            this.flipHorizontal = true;
            this.drawX += Gdx.graphics.getDeltaTime() * 400.0F;
            if (!this.escaped) {
                if (this.drawX >= Settings.WIDTH) {
                    CogitoBucket.npcs.add(new BodiesMountain_npc_tmp(this));
                    this.escaped = true;
                    if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
                        AbstractDungeon.getCurrRoom().endBattle();
                    }
                }
            }
        }
        else {
            this.flipHorizontal = this.target.drawX > this.drawX;
        }

        if (this.running){
            if (this.comeToEat){
                this.eatBody();
                this.comeToEat = false;
            }
            this.flipHorizontal = this.targetX > this.drawX;
            if (this.flipHorizontal){
                this.drawX += Gdx.graphics.getDeltaTime() * 400.0F;
                if (this.drawX >= this.targetX){
                    this.running = false;
                }
            }
            else {
                this.drawX -= Gdx.graphics.getDeltaTime() * 400.0F;
                if (this.drawX <= this.targetX){
                    this.running = false;
                }
            }
        }

        if (this.stage == 3){
            if (this.currentHealth <= 100){
                this.maxHealth = 100;
                this.bodies -= 3;
                this.stage --;
                this.changeState("BACK_TO_2");
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
            }
        }
        else if (this.stage == 2){
            if (this.currentHealth <= 50){
                this.maxHealth = 50;
                this.bodies -= 3;
                this.stage --;
                this.changeState("BACK_TO_1");
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
            }
        }
    }

    private void eatBody(){
        if (this.bodies >= 6){
            return;
        }
        if (this.eatTarget != null){
            this.targetX = this.eatTarget.drawX;
            AbstractDungeon.effectList.add(new LatterEffect(()->{
                AbstractDungeon.getMonsters().monsters.remove(this.eatTarget);
            }));
            this.eatTarget = null;

            this.bodies++;
            if (this.bodies == 3) {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CHANGE_TO_2"));
                this.maxHealth = 100;
                this.currentHealth = this.maxHealth;
                this.heal(this.maxHealth);
            } else if (this.bodies == 6) {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CHANGE_TO_3"));
                this.maxHealth = 200;
                this.currentHealth = this.maxHealth;
                this.heal(this.maxHealth);
            }
        }
        else {
            boolean flag = false;
            for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
                if (AbstractDungeon.getMonsters().monsters.get(i).isDead) {
                    this.targetX = AbstractDungeon.getMonsters().monsters.get(i).drawX;
                    //LobExtendMod.logger.info(this.targetX);
                    AbstractDungeon.getMonsters().monsters.remove(i);
                    flag = true;
                    break;
                }
            }
            if (flag) {
                this.bodies++;
                if (this.bodies == 3) {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CHANGE_TO_2"));
                    this.maxHealth = 100;
                    this.currentHealth = this.maxHealth;
                    this.heal(this.maxHealth);
                } else if (this.bodies == 6) {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CHANGE_TO_3"));
                    this.maxHealth = 200;
                    this.currentHealth = this.maxHealth;
                    this.heal(this.maxHealth);
                }
            }
        }
        LobExtendMod.logger.info(this.bodies);
    }

    public void restart(AbstractMonster eatTarget, BodiesMountain_m bm){
        if (bm.stage == 2){
            this.changeState("CHANGE_TO_2");
            this.maxHealth = 100;
        }
        else if (bm.stage == 3){
            this.changeState("CHANGE_TO_3");
            this.maxHealth = 200;
        }
        this.bodies = bm.bodies;
        this.stage = bm.stage;
        this.escapeTurns = bm.escapeTurns + 1;
        this.currentHealth = this.maxHealth;
        this.turns = 0;
        this.comeToEat = true;
        this.running = true;
        this.eatTarget = eatTarget;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void die() {
        super.die();
        this.deathTimer += 0.5F;
        this.changeState("DIE");
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, TheSmile_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new BodiesMountain());
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "CHANGE_TO_2":
                this.state.setAnimation(0, "Level2_Change", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Level2_Walk", true, 0.0F);
                break;
            case "CHANGE_TO_3":
                this.state.setAnimation(0, "Level3_Change", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Level3_Walk", true, 0.0F);
                break;
            case "ATTACK1":
                this.state.setAnimation(0, "Level1_Attack", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Level1_Walk", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "Level2_Attack", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Level2_Walk", true, 0.0F);
                break;
            case "ATTACK3_1":
                this.state.setAnimation(0, "Level3_Attack_01", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Level3_Walk", true, 0.0F);
                break;
            case "ATTACK3_2":
                this.state.setAnimation(0, "Level3_Attack_03", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Level3_Walk", true, 0.0F);
                break;
            case "ATTACK3_3":
                this.state.setAnimation(0, "Level3_Attack_02", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Level3_Walk", true, 0.0F);
                break;
            case "BACK_TO_1":
                this.state.setAnimation(0, "Level1_Walk", true);
                this.state.setTimeScale(1F);
                break;
            case "BACK_TO_2":
                this.state.setAnimation(0, "Level2_Walk", true);
                this.state.setTimeScale(1F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
