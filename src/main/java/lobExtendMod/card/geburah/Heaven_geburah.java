package lobExtendMod.card.geburah;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import lobExtendMod.relic.RedMistMask;
import lobExtendMod.vfx.geburah.GeburahSpear_self;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class Heaven_geburah extends AbstractGeburahCard {
    public static final String ID = "Heaven_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public Heaven_geburah() {
        super("Heaven_geburah", Heaven_geburah.NAME, 2, Heaven_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ALL_ENEMY);
        this.baseDamage = 50;
        this.isMultiDamage = true;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_02_03_Spear", false);
            if (AbstractDungeon.player.hasRelic(RedMistMask.ID) && ((RedMistMask)AbstractDungeon.player.getRelic(RedMistMask.ID)).state == 2){
                p.state.addAnimation(0, "Phase_03_Default", true, 0.0F);
            }
            else {
                p.state.addAnimation(0, "Phase_02_Default", true, 0.0F);
            }
        }));
        AbstractDungeon.effectList.add(new GeburahSpear_self());
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(10);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Heaven_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("Heaven_geburah");
        NAME = Heaven_geburah.cardStrings.NAME;
        DESCRIPTION = Heaven_geburah.cardStrings.DESCRIPTION;
    }
}
