package lobExtendMod.power;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/**
 * @author hoykj
 */
public class DeepSleepMonster extends AbstractPower {
    public static final String POWER_ID = "DeepSleepMonster";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("DeepSleepMonster");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private byte moveByte;
    private AbstractMonster.Intent moveIntent;
    private Color intentColor;
    private float intentAlpha;
    private float intentAlphaTarget;
    private float intentSize;
    private float intentSizeTarget;
    private Texture intentImg;

    public DeepSleepMonster(AbstractCreature owner)
    {
        this.name = NAME;
        this.ID = "DeepSleepMonster";
        this.owner = owner;
        this.amount = -1;
        updateDescription();

        this.type = AbstractPower.PowerType.BUFF;
        this.img = ImageMaster.loadImage("lobotomyMod/images/powers/32/DeepSleep.png");

        this.moveByte = 1;
        this.moveIntent = AbstractMonster.Intent.UNKNOWN;

        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)owner;

            this.moveByte = Byte.valueOf(m.nextMove).byteValue();
            this.moveIntent = AbstractMonster.Intent.valueOf(m.intent.name());

            byte a = 127;
            m.setMove(a, AbstractMonster.Intent.SLEEP);
            m.createIntent();
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(m, a, AbstractMonster.Intent.SLEEP));
        }
        this.priority = 100;
        this.intentAlpha = 0.0F;
        this.intentAlphaTarget = 1.0F;
        this.intentSize = 1.0F;
        this.intentSizeTarget = 1.2F;
        this.intentImg = ImageMaster.INTENT_SLEEP;
    }

    public void updateDescription()
    {
        this.description = DESCRIPTIONS[0];
    }

    public void atEndOfRound()
    {
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (this.owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)this.owner;

            m.setMove(this.moveByte, this.moveIntent);
            m.createIntent();
            AbstractDungeon.actionManager.addToBottom(new SetMoveAction(m, this.moveByte, this.moveIntent));
            m.updatePowers();
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        sb.setColor(c);
        this.intentColor = c;

        if (this.intentAlpha != this.intentAlphaTarget && this.intentAlphaTarget == 1.0F) {
            this.intentAlpha += Gdx.graphics.getDeltaTime() / 5;
            if (this.intentAlpha > this.intentAlphaTarget) {
                this.intentAlpha = this.intentAlphaTarget;
                this.intentAlphaTarget = 0.0F;
            }
        }
        else if (this.intentAlpha != this.intentAlphaTarget && this.intentAlphaTarget == 0.0F){
            this.intentAlpha -= Gdx.graphics.getDeltaTime() / 5;
            if (this.intentAlpha < this.intentAlphaTarget) {
                this.intentAlpha = this.intentAlphaTarget;
                this.intentAlphaTarget = 1.0F;
            }
        }

        if (this.intentSize != this.intentSizeTarget && this.intentSizeTarget == 1.2F) {
            this.intentSize += Gdx.graphics.getDeltaTime() / 5;
            if (this.intentSize > this.intentSizeTarget) {
                this.intentSize = this.intentSizeTarget;
                this.intentSizeTarget = 1.0F;
            }
        }
        else if (this.intentSize != this.intentSizeTarget && this.intentSizeTarget == 1.0F){
            this.intentSize -= Gdx.graphics.getDeltaTime() / 8;
            if (this.intentSize < this.intentSizeTarget) {
                this.intentSize = this.intentSizeTarget;
                this.intentSizeTarget = 1.2F;
            }
        }

        this.intentColor.a = this.intentAlpha;
        if (this.intentImg != null) {

            sb.setColor(this.intentColor);
            sb.draw(this.intentImg, this.owner.hb.cX - 80.0F, this.owner.hb.cY + 40.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * this.intentSize, Settings.scale * this.intentSize, 0.0F, 0, 0, 128, 128, false, false);
        }

        sb.setColor(c);
    }
}
