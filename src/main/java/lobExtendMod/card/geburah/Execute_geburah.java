package lobExtendMod.card.geburah;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class Execute_geburah extends AbstractGeburahCard {
    public static final String ID = "Execute_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public Execute_geburah() {
        super("Execute_geburah", Execute_geburah.NAME, -2, Execute_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.NONE);
        this.baseDamage = 45;
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_04_Attack_02", false);
            p.state.addAnimation(0, "Phase_04_Default", true, 0.0F);
        }));
        this.addToBot(new LatterAction(()->{
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            if (AbstractDungeon.cardRng.randomBoolean()) {
                this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
            }
            else {
                this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
            }
        }, 0.8F));
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {

    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(10);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Execute_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("Execute_geburah");
        NAME = Execute_geburah.cardStrings.NAME;
        DESCRIPTION = Execute_geburah.cardStrings.DESCRIPTION;
    }
}
