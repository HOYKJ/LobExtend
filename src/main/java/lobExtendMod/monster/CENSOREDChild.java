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
import com.megacrit.cardcrawl.localization.MonsterStrings;
import lobExtendMod.action.MeltingSlimeAttackAction;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;

/**
 * @author hoykj
 */
public class CENSOREDChild extends AbstractFriendlyMonster {
    public static final String ID = "CENSOREDChild";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("CENSOREDChild");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean started;

    public CENSOREDChild(float x, float y) {
        super(NAME, "CENSOREDChild", 30, 0.0F, -4.0F, 260.0F, 180.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/CENSORED/Child/CensoredChild.atlas", "lobExtendMod/images/monsters/CENSORED/Child/CensoredChild.json", 1.8F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Default", "Attack", 0.1F);
        this.stateData.setMix("Default", "Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 10));
        this.drawX = x;
        this.drawY = y;
        this.started = false;
        this.identifier = "CENSORED";
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BORN"));
        this.started = true;
    }

    protected void getMove(int num) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        this.randomTarget();
    }

    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
        AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }, 0.67F));
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
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "BORN":
                this.state.setAnimation(0, "Born", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default", true, 0.0F);
                break;
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Default", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
