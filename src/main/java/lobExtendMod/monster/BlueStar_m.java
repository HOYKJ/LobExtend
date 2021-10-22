package lobExtendMod.monster;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.relic.SoundOfStar_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.rareCard.BlueStar;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class BlueStar_m extends AbstractFriendlyMonster {
    public static final String ID = "BlueStar_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BlueStar_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    public BlueStar_m(float x, float y, int strength) {
        super(NAME, "BlueStar_m", 220, 0.0F, -4.0F, 400.0F, 540.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/BlueStar/skeleton.atlas", "lobExtendMod/images/monsters/BlueStar/skeleton.json", 1.2F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Escape_inside", false);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.state.addAnimation(0, "Escape_out", false, 0.0F);
        this.state.addAnimation(0, "Default_out", true, 0.0F);
        this.stateData.setMix("Default_out", "Casting", 0.1F);
        this.stateData.setMix("Default_out", "Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 12));
        this.drawX = x;
        this.drawY = y;
        this.powers.add(new StrengthPower(this, strength));
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BlueStar_Bgm.mp3");
    }

    protected void getMove(int num) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAllAction(this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                }, 2.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        if (this.stateNormal() && this.state.getTracks().get(0).getAnimation().getName().equals("Escape_out")){
            this.drawX = Settings.WIDTH / 2.0F;
        }
    }

    public void die() {
        super.die();
        this.deathTimer += 3.0F;
        this.changeState("DIE");
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, SoundOfStar_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new BlueStar());
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK":
                this.state.setAnimation(0, "Casting", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Cating attack", false, 0.0F);
                this.state.addAnimation(0, "Default_out", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
