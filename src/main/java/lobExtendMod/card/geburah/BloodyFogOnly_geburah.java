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
import lobExtendMod.action.geburah.GeburahDash_self;
import lobExtendMod.vfx.geburah.GeburahGreed_self;
import lobotomyMod.action.common.LatterAction;

/**
 * @author hoykj
 */
public class BloodyFogOnly_geburah extends AbstractGeburahCard {
    public static final String ID = "BloodyFogOnly_geburah";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public BloodyFogOnly_geburah() {
        super("BloodyFogOnly_geburah", BloodyFogOnly_geburah.NAME, 2, BloodyFogOnly_geburah.DESCRIPTION, CardType.ATTACK, CardTarget.ALL_ENEMY);
        this.baseDamage = 50;
        this.isMultiDamage = true;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.addToBot(new LatterAction(()->{
            p.state.setAnimation(0, "Phase_04_Attack_Start", false);
            p.state.addAnimation(0, "Phase_04_Attack_Run", true, 0.0F);
        }));
        AbstractDungeon.effectList.add(new GeburahGreed_self());
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new GeburahDash_self(true));
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
        return new BloodyFogOnly_geburah();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings("BloodyFogOnly_geburah");
        NAME = BloodyFogOnly_geburah.cardStrings.NAME;
        DESCRIPTION = BloodyFogOnly_geburah.cardStrings.DESCRIPTION;
    }
}
