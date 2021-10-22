package lobExtendMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import lobExtendMod.monster.MeltingLoveSmallSlime;

import java.util.UUID;

/**
 * @author hoykj
 */
public class MeltingSlimeAttackAction extends AbstractGameAction {
    private DamageInfo info;

    public MeltingSlimeAttackAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
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
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new MeltingLoveSmallSlime(this.target.drawX, this.target.drawY), false));
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        tickDuration();
    }
}
