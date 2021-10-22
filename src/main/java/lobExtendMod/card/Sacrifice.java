package lobExtendMod.card;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobotomyMod.card.deriveCard.AbstractDeriveCard;

/**
 * @author hoykj
 */
public class Sacrifice extends AbstractDeriveCard {
    public static final String ID = "Sacrifice";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public Sacrifice() {
        super("Sacrifice", Sacrifice.NAME, 0, Sacrifice.DESCRIPTION, CardColor.COLORLESS, CardType.SKILL, CardTarget.NONE);
        this.selfRetain = true;
        this.purgeOnUse = true;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {

    }

    @Override
    public AbstractCard makeCopy() {
        return new Sacrifice();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("Sacrifice");
        NAME = Sacrifice.cardStrings.NAME;
        DESCRIPTION = Sacrifice.cardStrings.DESCRIPTION;
    }
}
