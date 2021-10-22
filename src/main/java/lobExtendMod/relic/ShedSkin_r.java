package lobExtendMod.relic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.vfx.WantedRequestEffect;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class ShedSkin_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "ShedSkin_r";

    public ShedSkin_r()
    {
        super("ShedSkin_r",  RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        this.counter = 1;
        this.tips.clear();
        this.description = getUpdatedDescription();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.counter = 1;
    }

    @Override
    public void onLoseHp(int damageAmount) {
        super.onLoseHp(damageAmount);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, this.counter), this.counter));
        //AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.counter));
        this.counter ++;
        this.tips.clear();
        this.description = getUpdatedDescription();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (this.counter > 1) {
            this.counter -= 1;
        }
        this.tips.clear();
        this.description = getUpdatedDescription();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + this.counter + this.DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new ShedSkin_r();
    }
}
