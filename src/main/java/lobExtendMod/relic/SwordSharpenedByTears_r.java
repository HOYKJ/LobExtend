package lobExtendMod.relic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class SwordSharpenedByTears_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "SwordSharpenedByTears_r";

    public SwordSharpenedByTears_r()
    {
        super("SwordSharpenedByTears_r",  RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        for (AbstractPower power : AbstractDungeon.player.powers){
            if (power.type == AbstractPower.PowerType.DEBUFF){
                return super.atDamageModify(damage, c) * 1.4F;
            }
        }
        return super.atDamageModify(damage, c);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SwordSharpenedByTears_r();
    }
}
