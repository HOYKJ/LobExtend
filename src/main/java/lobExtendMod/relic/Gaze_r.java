package lobExtendMod.relic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.card.AbstractLobotomyCard;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class Gaze_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "Gaze_r";
    private boolean attacked;

    public Gaze_r()
    {
        super("Gaze_r",  RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        this.attacked = false;
        this.beginPulse();
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (c.type == AbstractCard.CardType.SKILL && !(c instanceof AbstractLobotomyCard)){
            this.attacked = true;
            this.stopPulse();
        }
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (this.attacked){
            return super.onAttackedToChangeDamage(info, damageAmount);
        }
        return (int) (super.onAttackedToChangeDamage(info, damageAmount) * 0.4F);
    }

    @Override
    public void onVictory() {
        super.onVictory();
        this.attacked = true;
        this.stopPulse();
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Gaze_r();
    }
}
