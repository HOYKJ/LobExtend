package lobExtendMod.card.geburah;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class Justitia_geburah extends AbstractGeburahCard {
    public static final String ID = "Justitia_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public Justitia_geburah() {
        super("Justitia_geburah", Justitia_geburah.NAME, 1, Justitia_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
        this.baseMagicNumber = 25;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_03_Attack_02", false);
            p.state.addAnimation(0, "Phase_03_Default", true, 0.0F);
        }));
        this.addToBot(new LatterAction(()->{
            this.addToBot(new DamageAction(m, new DamageInfo(p, (int) (m.maxHealth * 0.01F * this.magicNumber), DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
        }, 0.8F));
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeMagicNumber(10);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Justitia_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("Justitia_geburah");
        NAME = Justitia_geburah.cardStrings.NAME;
        DESCRIPTION = Justitia_geburah.cardStrings.DESCRIPTION;
    }
}
