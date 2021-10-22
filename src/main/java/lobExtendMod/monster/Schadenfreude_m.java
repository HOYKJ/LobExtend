package lobExtendMod.monster;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.relic.Gaze_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class Schadenfreude_m extends AbstractMonster {
    public static final String ID = "Schadenfreude_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Schadenfreude_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    public Schadenfreude_m(float x, float y) {
        super(NAME, "Schadenfreude_m", 80, 0.0F, 0.0F, 400.0F, 300.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/Schadenfreude/LookAtMe.atlas", "lobExtendMod/images/monsters/Schadenfreude/LookAtMe.json", 1.2F);
        this.state.setAnimation(0, "default_box", false);
        this.state.setTimeScale(1F);
        this.state.addAnimation(0, "transform", false, 0.0F);
        this.state.addAnimation(0, "default", true, 0.0F);

        this.stateData.setMix("default", "attack1", 0.1F);
        this.stateData.setMix("default", "attack2", 0.1F);
        this.stateData.setMix("default", "special1", 0.1F);
        this.stateData.setMix("special2", "special3", 0.1F);
        this.stateData.setMix("default", "dead", 0.1F);
        this.damage.add(new DamageInfo(this, 10));
        this.damage.add(new DamageInfo(this, 3));
        this.flipHorizontal = true;
    }

    protected void getMove(int num) {
        if (num > 70){
            setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base, 9, true);
        }
        else {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
    }

    public void takeTurn() {
        if(this.nextMove == 1){
            if (MathUtils.randomBoolean()) {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
            }
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            }, 0.63F));
        }
        else if(this.nextMove == 2){
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST"));
            for (int i = 0; i < 9; i ++){
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
            }
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST_END"));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void die() {
        super.die();
        this.changeState("DIE");
        this.deathTimer += 1.7F;
        if (!AbstractDungeon.player.hasRelic(Gaze_r.ID)) {
            AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, Gaze_r.ID));
        }
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "attack1", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "default", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "attack2", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "default", true, 0.0F);
                break;
            case "CAST":
                this.state.setAnimation(0, "special1", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "special2", true, 0.0F);
                break;
            case "CAST_END":
                this.state.setAnimation(0, "special3", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "default", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
