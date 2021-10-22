package lobExtendMod.relic;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class Logging_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "Logging_r";
    private boolean first;

    public Logging_r()
    {
        super("Logging_r",  RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        this.first = true;
        this.beginPulse();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (this.first) {
            this.first = false;
            this.stopPulse();
            AbstractDungeon.actionManager.addToBottom(new LatterAction(() -> {
                if (target.currentHealth <= 0) {
                    return;
                }
                if (target instanceof AbstractMonster && info.type == DamageInfo.DamageType.NORMAL) {
                    target.damage(new DamageInfo(AbstractDungeon.player, 5, DamageInfo.DamageType.THORNS));
                    if (target.currentHealth <= 0) {
                        AbstractDungeon.player.heal(3);
                    }
                }
            }));
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Logging_r();
    }
}
