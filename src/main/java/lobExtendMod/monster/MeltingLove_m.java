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
import lobExtendMod.action.MeltingSlimeAttackAction;
import lobExtendMod.relic.Adoration_r;
import lobExtendMod.relic.CENSORED_r;
import lobExtendMod.relic.Justitia_r;
import lobExtendMod.relic.SolemnVow_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.rareCard.MeltingLove;
import lobotomyMod.card.uncommonCard.Funeral;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.vfx.action.LatterEffect;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class MeltingLove_m extends AbstractFriendlyMonster {
    public static final String ID = "MeltingLove_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("MeltingLove_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean fierce;

    public MeltingLove_m(float x, float y) {
        super(NAME, "MeltingLove_m", 150, 0.0F, -4.0F, 300.0F, 340.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/MeltingLove/BigSlime/skeleton.atlas", "lobExtendMod/images/monsters/MeltingLove/BigSlime/skeleton.json", 1.4F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "0_default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("0_default", "1_attack", 0.1F);
        this.stateData.setMix("0_default", "1_attack_2", 0.1F);
        this.stateData.setMix("0_default", "1_spit", 0.1F);
        this.stateData.setMix("0_default", "1_absorb_start", 0.1F);
        this.stateData.setMix("2_absorb_loop", "3_abosrb_end", 0.1F);
        this.stateData.setMix("0_default", "0_dead", 0.1F);
        this.damage.add(new DamageInfo(this, 15));
        this.damage.add(new DamageInfo(this, 20));
        this.damage.add(new DamageInfo(this, 40));
        this.drawX = x;
        this.drawY = y;
        this.fierce = false;
        this.identifier = "MeltingLove";
    }

    protected void getMove(int num) {
        if(num < 60F) {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
        else if(num < 90F) {
            setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base);
        }
        else {
            setMove((byte) 3, Intent.ATTACK, this.damage.get(2).base);
        }
        if (!this.fierce && this.currentHealth < this.maxHealth * 0.3F){
            setMove((byte) 4, Intent.UNKNOWN);
        }
        this.randomTarget();
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new MeltingSlimeAttackAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }, 1.0F));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new MeltingSlimeAttackAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }, 1.0F));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SPIT"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new MeltingSlimeAttackAction(this.target, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }, 2.0F));
                break;
            case 4:
                ArrayList<AbstractMonster> tmp = new ArrayList<>();
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
                    if (m instanceof MeltingLoveSmallSlime && !m.isDeadOrEscaped()){
                        tmp.add(m);
                    }
                }
                AbstractMonster slime = tmp.get(AbstractDungeon.aiRng.random(tmp.size() - 1));
                slime.drawX = this.drawX + (this.flipHorizontal? this.hb.width + 33 - slime.hb.width / 2.0F: -33 - slime.hb.width / 2.0F);
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ABSORB"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, 20));
                    AbstractDungeon.actionManager.addToBottom(new LatterAction(slime::die));
                }, 3.1F));
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
        if (!this.fierce){
            boolean flag = true;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
                if (m instanceof MeltingLoveSmallSlime && !m.isDeadOrEscaped()){
                    flag = false;
                    break;
                }
            }
            if (flag){
                this.fierce = true;
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, 20));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
            }
        }
        this.flipHorizontal = this.target.drawX > this.drawX;
    }

    public void die() {
        super.die();
        this.deathTimer += 2.7F;
        this.changeState("DIE");
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
            if (monster instanceof MeltingLoveSmallSlime){
                monster.die();
            }
        }
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, Adoration_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new MeltingLove());
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "1_attack", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_default", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "1_attack_2", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_default", true, 0.0F);
                break;
            case "SPIT":
                this.state.setAnimation(0, "1_spit", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_default", true, 0.0F);
                break;
            case "ABSORB":
                this.state.setAnimation(0, "1_absorb_start", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "2_absorb_loop", false, 0.0F);
                this.state.addAnimation(0, "3_abosrb_end", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "0_dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
