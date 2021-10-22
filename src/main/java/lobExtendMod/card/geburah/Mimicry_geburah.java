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
public class Mimicry_geburah extends AbstractGeburahCard {
    public static final String ID = "Mimicry_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public Mimicry_geburah() {
        super("Mimicry_geburah", Mimicry_geburah.NAME, 1, Mimicry_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
        this.baseDamage = 35;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_02_Attack_01", false);
            p.state.addAnimation(0, "Phase_02_Default", true, 0.0F);
        }));
        this.addToBot(new LatterAction(()->{
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }, 1.0F));
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(10);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Mimicry_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("Mimicry_geburah");
        NAME = Mimicry_geburah.cardStrings.NAME;
        DESCRIPTION = Mimicry_geburah.cardStrings.DESCRIPTION;
    }
}
