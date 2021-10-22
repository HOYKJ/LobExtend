package lobExtendMod.relic;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class Lamp_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "Lamp_r";

    public Lamp_r()
    {
        super("Lamp_r",  RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        for (AbstractPower power : target.powers){
            if (power instanceof VulnerablePower){
                if(info.type == DamageInfo.DamageType.NORMAL){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(info.owner, 11, DamageInfo.DamageType.THORNS)));
                }
                break;
            }
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Lamp_r();
    }
}
