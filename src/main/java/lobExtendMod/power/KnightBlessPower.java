package lobExtendMod.power;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lobExtendMod.npc.DespairKnight_npc;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.helper.LobotomyImageMaster;

/**
 * @author hoykj
 */
public class KnightBlessPower extends AbstractPower {
    public static final String POWER_ID = "KnightBlessPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("KnightBlessPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private DespairKnight_npc dk;

    public KnightBlessPower(AbstractCreature owner, DespairKnight_npc dk) {
        this.name = NAME;
        this.ID = "KnightBlessPower";
        this.owner = owner;
        this.amount = owner.maxHealth / 3;
        this.img = ImageMaster.loadImage("lobotomyMod/images/powers/32/DespairBlessPower.png");
        this.type = PowerType.BUFF;
        updateDescription();
        this.dk = dk;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        return super.atDamageReceive(damage, damageType) / 2;
    }

//    @Override
//    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
//        return super.onAttackedToChangeDamage(info, damageAmount) / 2;
//    }

    @Override
    public int onLoseHp(int damageAmount) {
        this.amount -= damageAmount;
        if (this.amount <= 0){
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        return super.onLoseHp(damageAmount);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
            this.dk.change();
        }));
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        sb.setColor(Color.WHITE.cpy());
        sb.draw(LobotomyImageMaster.DESPAIR_BLESS, this.owner.hb.cX - 32.0F, this.owner.hb.y + this.owner.hb.height, 64.0F, 64.0F);
    }

    public void updateDescription()
    {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
