package lobExtendMod.relic;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class SolemnVow_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "SolemnVow_r";

    public SolemnVow_r()
    {
        super("SolemnVow_r",  RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if(info.type == DamageInfo.DamageType.NORMAL){
            AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(info.owner, 2, DamageInfo.DamageType.THORNS)));
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SolemnVow_r();
    }
}
