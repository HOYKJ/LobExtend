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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class BlackSmile_geburah extends AbstractGeburahCard {
    public static final String ID = "BlackSmile_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public BlackSmile_geburah() {
        super("BlackSmile_geburah", BlackSmile_geburah.NAME, 1, BlackSmile_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
        this.baseDamage = 45;
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_03_Attack_01", false);
            p.state.addAnimation(0, "Phase_03_Default", true, 0.0F);
        }));
        this.addToBot(new LatterAction(()->{
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
        }, 3.0F));
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(10);
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackSmile_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("BlackSmile_geburah");
        NAME = BlackSmile_geburah.cardStrings.NAME;
        DESCRIPTION = BlackSmile_geburah.cardStrings.DESCRIPTION;
    }
}
