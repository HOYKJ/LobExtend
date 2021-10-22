package lobExtendMod.monster;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import lobExtendMod.action.HatredLaserAction_m;
import lobExtendMod.relic.CENSORED_r;
import lobExtendMod.relic.CrimsonScar_r;
import lobExtendMod.relic.InTheNameOfLoveAndHate_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.QueenOfHatred;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.relic.ApostleMask;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class QueenOfHatred_m extends AbstractFriendlyMonster {
    public static final String ID = "QueenOfHatred_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("QueenOfHatred_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private int turns;

    public QueenOfHatred_m(float x, float y) {
        super(NAME, "QueenOfHatred_m", 120, 20.0F, -8.0F, 340.0F, 280.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/QueenOfHatred/monster/skeleton.atlas", "lobExtendMod/images/monsters/QueenOfHatred/monster/skeleton.json", 1.6F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "0_Default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("0_Default", "1_Default_to_Casting", 0.1F);
        this.stateData.setMix("2_Casting_loop", "3_Casting_to_Casting_attack", 0.1F);
        this.stateData.setMix("4_Casting_attack_loop", "5_Casting_attack_to_Delay", 0.1F);
        this.stateData.setMix("6_Delay_loop", "7_Delay_to_Default", 0.1F);
        this.stateData.setMix("0_Default", "0_Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 2));
        this.turns = 1;
    }

    protected void getMove(int num) {
        switch (this.turns){
            case 1:
                setMove((byte) 1, Intent.UNKNOWN);
                break;
            case 2:
                setMove((byte) 2, Intent.ATTACK_BUFF, this.damage.get(0).base, 12, true);
                break;
            case 3:
                setMove((byte) 3, Intent.UNKNOWN);
                break;
        }
        this.turns ++;
        if(this.turns > 3){
            this.turns = 1;
        }
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST"));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST_ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToBottom(new HatredLaserAction_m(this.skeleton, this.state, this.damage.get(0), this));
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DELAY"));
                }));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "NORMAL"));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        if (this.intent == Intent.DEBUG){
            this.createIntent();
        }
    }

    public void die() {
        super.die();
        this.changeState("DIE");
        this.deathTimer += 18F;
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, InTheNameOfLoveAndHate_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new QueenOfHatred());
    }

    public void changeState(String key) {
        switch (key)
        {
            case "CAST":
                this.state.setAnimation(0, "1_Default_to_Casting", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "2_Casting_loop", true, 0.0F);
                break;
            case "CAST_ATTACK":
                this.state.setAnimation(0, "3_Casting_to_Casting_attack", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "4_Casting_attack_loop", true, 0.0F);
                break;
            case "DELAY":
                this.state.setAnimation(0, "5_Casting_attack_to_Delay", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "6_Delay_loop", true, 0.0F);
                break;
            case "NORMAL":
                this.state.setAnimation(0, "7_Delay_to_Default", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "0_Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
