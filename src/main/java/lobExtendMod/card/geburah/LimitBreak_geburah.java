package lobExtendMod.card.geburah;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.relic.RedMistMask;

/**
 * @author hoykj
 */
public class LimitBreak_geburah extends AbstractGeburahCard {
    public static final String ID = "LimitBreak_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public LimitBreak_geburah() {
        super("LimitBreak_geburah", LimitBreak_geburah.NAME, 4, LimitBreak_geburah.DESCRIPTION, CardType.SKILL, CardTarget.SELF);
        this.purgeOnUse = true;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        if (p.hasRelic(RedMistMask.ID)) {
            ((RedMistMask)p.getRelic(RedMistMask.ID)).changeState();
        }
    }

    @Override
    public void upgrade() {
        this.upgradeName();
    }

    @Override
    public AbstractCard makeCopy() {
        return new LimitBreak_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("LimitBreak_geburah");
        NAME = LimitBreak_geburah.cardStrings.NAME;
        DESCRIPTION = LimitBreak_geburah.cardStrings.DESCRIPTION;
    }
}
