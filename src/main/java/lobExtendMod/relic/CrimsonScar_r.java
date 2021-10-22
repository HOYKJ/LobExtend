package lobExtendMod.relic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.power.BleedPower;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class CrimsonScar_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "CrimsonScar_r";

    public CrimsonScar_r()
    {
        super("CrimsonScar_r",  RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.type == AbstractCard.CardType.ATTACK){
            if (c.target == AbstractCard.CardTarget.ENEMY || c.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new BleedPower(m, 1), 1));
            }
            else if (c.target == AbstractCard.CardTarget.ALL_ENEMY || c.target == AbstractCard.CardTarget.ALL) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
                    if (!monster.isDeadOrEscaped() && monster.hasPower(BleedPower.POWER_ID)){
                        AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(AbstractDungeon.player, 4, DamageInfo.DamageType.THORNS)));
                    }
                }
            }
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new CrimsonScar_r();
    }
}
