package lobExtendMod.card.geburah;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.action.geburah.GeburahDash_self;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class GoldRush_geburah extends AbstractGeburahCard {
    public static final String ID = "GoldRush_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public GoldRush_geburah() {
        super("GoldRush_geburah", GoldRush_geburah.NAME, 2, GoldRush_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ALL_ENEMY);
        this.baseDamage = 50;
        this.isMultiDamage = true;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_All_Teleport_01", false);
            p.state.addAnimation(0, "Phase_All_Teleport_02", true, 0.0F);
        }));
        this.addToBot(new GeburahDash_self(false));
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(10);
    }

    @Override
    public AbstractCard makeCopy() {
        return new GoldRush_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("GoldRush_geburah");
        NAME = GoldRush_geburah.cardStrings.NAME;
        DESCRIPTION = GoldRush_geburah.cardStrings.DESCRIPTION;
    }
}
