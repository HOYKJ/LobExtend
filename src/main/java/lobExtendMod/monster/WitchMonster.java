package lobExtendMod.monster;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class WitchMonster extends AbstractMonster {
    public static final String ID = "WitchMonster";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("WitchMonster");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean right;
    private AbstractCreature target;

    public WitchMonster(float x, float y) {
        super(NAME, "WitchMonster", 35, 0.0F, 0.0F, 500.0F, 240.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/Laetitia/monster/WitchMonster.atlas", "lobExtendMod/images/monsters/Laetitia/monster/WitchMonster.json", 1.2F);
        this.state.setAnimation(0, "Born", false);
        this.state.addAnimation(0, "Standing", true, 0.0F);
        this.stateData.setMix("Standing", "Attack_01", 0.1F);
        this.stateData.setMix("Standing", "Attack_02", 0.1F);
        this.stateData.setMix("Standing", "Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 10));
        this.target = AbstractDungeon.player;
        this.right = AbstractDungeon.player.drawX > this.drawX;
    }

    protected void getMove(int num) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
    }

    public void takeTurn() {
        if(this.nextMove == 1){
            if (this.right) {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
            }
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
            }, 0.8F));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void die() {
        super.die();
        this.changeState("DIE");
        this.deathTimer += 2.0F;
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "Attack_01", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Standing", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "Attack_02", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Standing", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
