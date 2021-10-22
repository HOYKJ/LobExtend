package lobExtendMod.relic;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class SoundOfStar_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "SoundOfStar_r";

    public SoundOfStar_r()
    {
        super("SoundOfStar_r",  RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (AbstractDungeon.player.currentHealth > 50){
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            if (AbstractDungeon.player.currentHealth > 100){
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            }
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SoundOfStar_r();
    }
}
