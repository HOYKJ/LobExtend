package lobExtendMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

/**
 * @author hoykj
 */
public class BigBirdEatAction extends AbstractGameAction {
    private DamageInfo info;

    public BigBirdEatAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        this.info = info;
        setValues(target, info);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
    }

    public void update() {
        if ((this.duration == 0.1F) && (this.target != null)) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));

            this.target.damage(this.info);
            if (((this.target.isDying) || (this.target.currentHealth <= 0)) && (!this.target.halfDead)) {
                AbstractCreature bird = this.info.owner;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(bird, bird, new StrengthPower(bird, 10), 10));
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        tickDuration();
    }
}
