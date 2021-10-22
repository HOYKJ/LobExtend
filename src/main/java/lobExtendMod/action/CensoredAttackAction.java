package lobExtendMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import lobExtendMod.LobExtendMod;
import lobExtendMod.monster.CENSOREDChild;
import lobExtendMod.monster.MeltingLoveSmallSlime;

/**
 * @author hoykj
 */
public class CensoredAttackAction extends AbstractGameAction {
    private DamageInfo info;

    public CensoredAttackAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        this.info = info;
        setValues(target, info);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 1.5F;
    }

    public void update() {
        if ((this.duration == 1.5F) && (this.target != null)) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));

            this.target.damage(this.info);
//            if (((this.target.isDying) || (this.target.currentHealth <= 0)) && (!this.target.halfDead)) {
//                AbstractDungeon.getMonsters().monsters.remove(this.target);
//                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new CENSOREDChild(this.target.drawX, this.target.drawY), false));
//            }
//            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
//                AbstractDungeon.actionManager.clearPostCombatActions();
//            }
        }
        tickDuration();
        if (this.isDone){
            if (((this.target.isDying) || (this.target.currentHealth <= 0)) && (!this.target.halfDead)) {
                LobExtendMod.logger.info("target died");
                AbstractDungeon.getMonsters().monsters.remove(this.target);
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new CENSOREDChild(this.target.drawX, this.target.drawY), false));
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
