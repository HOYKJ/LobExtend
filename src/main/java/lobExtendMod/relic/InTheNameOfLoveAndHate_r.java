package lobExtendMod.relic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class InTheNameOfLoveAndHate_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "InTheNameOfLoveAndHate_r";
    private int tmp;

    public InTheNameOfLoveAndHate_r()
    {
        super("InTheNameOfLoveAndHate_r",  RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        this.counter = 0;
        this.tmp = 0;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.counter = 0;
        this.tmp = 0;
    }

    @Override
    public void onLoseHp(int damageAmount) {
        super.onLoseHp(damageAmount);
        this.tmp += damageAmount;
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        return damage + this.counter;
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        this.counter = this.tmp;
        this.tmp = 0;
    }

    @Override
    public void onVictory() {
        super.onVictory();
        this.counter = 0;
        this.tmp = 0;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new InTheNameOfLoveAndHate_r();
    }
}
