package lobExtendMod.card.geburah;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
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
public class TheMovementOfCalm_geburah extends AbstractGeburahCard {
    public static final String ID = "TheMovementOfCalm_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public TheMovementOfCalm_geburah() {
        super("TheMovementOfCalm_geburah", TheMovementOfCalm_geburah.NAME, 1, TheMovementOfCalm_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ALL_ENEMY);
        this.baseDamage = 45;
        this.isMultiDamage = true;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_04_Attack_End", false);
            p.state.addAnimation(0, "Phase_04_Default", true, 0.0F);
        }));
        this.addToBot(new LatterAction(()->{
            this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
                if (!monster.isDeadOrEscaped()){
                    if (AbstractDungeon.cardRng.randomBoolean()) {
                        this.addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, this.magicNumber, false), this.magicNumber));
                    }
                    else {
                        this.addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, this.magicNumber, false), this.magicNumber));
                    }
                }
            }
        }, 1.0F));
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(10);
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheMovementOfCalm_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("TheMovementOfCalm_geburah");
        NAME = TheMovementOfCalm_geburah.cardStrings.NAME;
        DESCRIPTION = TheMovementOfCalm_geburah.cardStrings.DESCRIPTION;
    }
}
