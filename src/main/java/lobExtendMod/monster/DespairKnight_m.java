package lobExtendMod.monster;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import lobExtendMod.action.CensoredAttackAction;
import lobExtendMod.relic.CENSORED_r;
import lobExtendMod.relic.SolemnVow_r;
import lobExtendMod.relic.SwordSharpenedByTears_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.DespairKnight;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class DespairKnight_m extends AbstractFriendlyMonster {
    public static final String ID = "DespairKnight_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("DespairKnight_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean started;

    public DespairKnight_m(float x, float y) {
        super(NAME, "DespairKnight_m", 80, 0.0F, 0.0F, 220.0F, 320.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/DespairKnight/magicalGirl3.atlas", "lobExtendMod/images/monsters/DespairKnight/magicalGirl3.json", 2.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Default_Normal", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Default_Normal", "Default_Change", 0.1F);
        this.stateData.setMix("Change_Default", "Change_Attack_01", 0.1F);
        this.stateData.setMix("Change_Default", "Change_Attack_02", 0.1F);
        this.stateData.setMix("Change_Default", "Change_Attack_03", 0.1F);
        this.stateData.setMix("Change_Default", "Change_Attack_04", 0.1F);
        this.stateData.setMix("Change_Default", "Change_Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 40));
        this.drawX = x;
        this.drawY = y;
        this.started = false;
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
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK" + MathUtils.random(1, 4)));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }, 0.2F));
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
        this.deathTimer += 1.5F;
        this.changeState("DIE");
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, SwordSharpenedByTears_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new DespairKnight());
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "CHANGE":
                this.state.setAnimation(0, "Default_Change", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Change_Default", true, 0.0F);
                break;
            case "ATTACK1":
                this.state.setAnimation(0, "Change_Attack_01", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Change_Default", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "Change_Attack_02", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Change_Default", true, 0.0F);
                break;
            case "ATTACK3":
                this.state.setAnimation(0, "Change_Attack_03", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Change_Default", true, 0.0F);
                break;
            case "ATTACK4":
                this.state.setAnimation(0, "Change_Attack_04", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Change_Default", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Change_Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
