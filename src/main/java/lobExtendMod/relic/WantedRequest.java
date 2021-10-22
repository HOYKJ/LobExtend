package lobExtendMod.relic;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.vfx.WantedRequestEffect;
import lobotomyMod.card.uncommonCard.Mercenary;
import lobotomyMod.relic.AbstractLobotomyRelic;

/**
 * @author hoykj
 */
public class WantedRequest extends AbstractLobotomyRelic {
    public static final String ID = "WantedRequest";

    public WantedRequest()
    {
        super("WantedRequest",  RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    protected void onRightClick() {
        super.onRightClick();
        if (AbstractDungeon.player.masterDeck.findCardById(Mercenary.ID) != null) {
            AbstractDungeon.effectsQueue.add(new WantedRequestEffect());
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new WantedRequest();
    }
}
