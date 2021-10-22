package lobExtendMod.power;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.monster.BigBird_m;
import lobExtendMod.vfx.EnchantMarkEffect;

/**
 * @author hoykj
 */
public class BigBirdEnchantPower extends AbstractPower {
    public static final String POWER_ID = "BigBirdEnchantPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("BigBirdEnchantPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private BigBird_m bird;
    private EnchantMarkEffect effect;

    public BigBirdEnchantPower(AbstractCreature owner, BigBird_m bird) {
        this.name = NAME;
        this.ID = "BigBirdEnchantPower";
        this.owner = owner;
        this.amount = 3;
        this.img = LobExtendImageMaster.BIGBIRD_MARK;
        this.type = PowerType.DEBUFF;
        updateDescription();
        this.bird = bird;
        this.effect = new EnchantMarkEffect(this.owner);
        AbstractDungeon.effectsQueue.add(this.effect);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        if (this.amount <= 1){
            this.bird.eat(this.owner);
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        this.amount --;
        if (this.amount <= 1){
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new WeakPower(this.owner, 1, true), 1));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new VulnerablePower(this.owner, 1, true), 1));
            this.effect.changeColor();
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        this.effect.end();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (this.bird.isDeadOrEscaped()){
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            this.effect.end();
        }
        if (this.owner.isDeadOrEscaped()){
            this.effect.end();
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
    }

    public void updateDescription()
    {
        this.description = DESCRIPTIONS[0];
    }
}
