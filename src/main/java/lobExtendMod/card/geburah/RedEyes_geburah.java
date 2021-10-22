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
public class RedEyes_geburah extends AbstractGeburahCard {
    public static final String ID = "RedEyes_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public RedEyes_geburah() {
        super("RedEyes_geburah", RedEyes_geburah.NAME, 1, RedEyes_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ENEMY);
        this.baseDamage = 12;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_01_Attack_01", false);
            p.state.addAnimation(0, "Phase_01_Default", true, 0.0F);
        }));
        this.addToBot(new LatterAction(()->{
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }, 0.8F));
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(6);
    }

    @Override
    public AbstractCard makeCopy() {
        return new RedEyes_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("RedEyes_geburah");
        NAME = RedEyes_geburah.cardStrings.NAME;
        DESCRIPTION = RedEyes_geburah.cardStrings.DESCRIPTION;
    }
}
