package lobExtendMod.card.geburah;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class Penitence_geburah extends AbstractGeburahCard {
    public static final String ID = "Penitence_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public Penitence_geburah() {
        super("Penitence_geburah", Penitence_geburah.NAME, 1, Penitence_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
        this.baseDamage = 12;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_01_Attack_02", false);
            p.state.addAnimation(0, "Phase_01_Default", true, 0.0F);
        }));
        this.addToBot(new LatterAction(()->{
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
        }, 0.8F));
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(6);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Penitence_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("Penitence_geburah");
        NAME = Penitence_geburah.cardStrings.NAME;
        DESCRIPTION = Penitence_geburah.cardStrings.DESCRIPTION;
    }
}
