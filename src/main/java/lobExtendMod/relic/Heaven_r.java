package lobExtendMod.relic;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class Heaven_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "Heaven_r";
    private boolean attacked;

    public Heaven_r()
    {
        super("Heaven_r",  RelicTier.UNCOMMON, LandingSound.SOLID);
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
        if (c.type == AbstractCard.CardType.ATTACK){
            this.attacked = true;
            this.stopPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (!this.attacked){
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
                if (!monster.isDeadOrEscaped()) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(AbstractDungeon.player, 15, DamageInfo.DamageType.THORNS)));
                }
            }
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Heaven_r();
    }
}
