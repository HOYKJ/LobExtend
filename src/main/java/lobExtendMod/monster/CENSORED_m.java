package lobExtendMod.monster;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lobExtendMod.action.CensoredAttackAction;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.action.MeltingSlimeAttackAction;
import lobExtendMod.relic.Adoration_r;
import lobExtendMod.relic.CENSORED_r;
import lobExtendMod.relic.TheSmile_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.rareCard.CENSORED;
import lobotomyMod.card.rareCard.MeltingLove;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.vfx.action.LatterEffect;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class CENSORED_m extends AbstractFriendlyMonster {
    public static final String ID = "CENSORED_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("CENSORED_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean coolDown;
    private CENSOREDChild child;
    private boolean started;

    public CENSORED_m(float x, float y) {
        super(NAME, "CENSORED_m", 150, 0.0F, -4.0F, 480.0F, 300.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/CENSORED/censored.atlas", "lobExtendMod/images/monsters/CENSORED/censored.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Default", "Change", 0.1F);
        this.stateData.setMix("Default_Escape", "Attack_01", 0.1F);
        this.stateData.setMix("Default_Escape", "Attack_02", 0.1F);
        this.stateData.setMix("Default_Escape", "Attack_03", 0.1F);
        this.stateData.setMix("Default_Escape", "DeadScene", 0.1F);
        this.stateData.setMix("Default_Escape", "Daed", 0.1F);
        this.damage.add(new DamageInfo(this, 24));
        this.drawX = x;
        this.drawY = y;
        this.coolDown = true;
        this.started = false;
        this.identifier = "CENSORED";
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CHANGE"));
        this.started = true;
    }

    protected void getMove(int num) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        this.randomTarget();
        if (this.coolDown){
            this.coolDown = false;
        }
        else {
            for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
                AbstractMonster monster = AbstractDungeon.getMonsters().monsters.get(i);
                if (monster.isDead){
                    this.child = new CENSOREDChild(monster.drawX, monster.drawY);
                    setMove((byte) 2, Intent.UNKNOWN);
                    AbstractDungeon.getMonsters().monsters.remove(i);
                    break;
                }
            }
        }
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                if (this.target.currentHealth <= this.damage.get(0).base){
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "KILL"));
                    AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                        AbstractDungeon.actionManager.addToTop(new CensoredAttackAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    }, 1.0F));
//                    AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
//                        if (this.target instanceof AbstractMonster && (this.target.isDead || this.target.isDying)){
//                            AbstractDungeon.getMonsters().monsters.remove(this.target);
//                            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new CENSOREDChild(this.target.drawX, this.target.drawY), false));
//                        }
//                    }, 1.5F));
                }
                else {
                    if (MathUtils.randomBoolean()) {
                        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
                    }
                    AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                        AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    }, 1.0F));
                }
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SPECIAL"));
                this.coolDown = true;
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAllAction(this, 12, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
                    AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(this.child, false));
                    AbstractDungeon.actionManager.addToTop(new HealAction(this, this, 20));
                }, 2.0F));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        if(this.target == null || this.target.isDeadOrEscaped() || this.target == this){
            if(!AbstractDungeon.getMonsters().areMonstersBasicallyDead()){
                this.randomTarget();
            }
        }
        super.update();
        if (!this.started){
            this.usePreBattleAction();
        }
    }

    public void die() {
        super.die();
        this.deathTimer += 0.5F;
        this.changeState("DIE");
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, CENSORED_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new CENSORED());
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "CHANGE":
                this.state.setAnimation(0, "Change", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_Escape", true, 0.0F);
                break;
            case "ATTACK1":
                this.state.setAnimation(0, "Attack_01", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_Escape", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "Attack_03", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_Escape", true, 0.0F);
                break;
            case "KILL":
                this.state.setAnimation(0, "DeadScene", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_Escape", true, 0.0F);
                break;
            case "SPECIAL":
                this.state.setAnimation(0, "Attack_02", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default_Escape", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Daed", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
