package lobExtendMod.relic;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.monster.MeltingLoveSmallSlime;
import lobExtendMod.monster.friendlyMonster.MeltingLoveSmallSlime_f;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class Adoration_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "Adoration_r";

    public Adoration_r()
    {
        super("Adoration_r",  RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        this.counter = 0;
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        this.counter ++;
        if (this.counter >= 3){
            this.counter = 0;
            float x = MathUtils.random(0, Settings.WIDTH - 180);
            float y = MathUtils.random(AbstractDungeon.floorY - 20, AbstractDungeon.floorY + 20);
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new MeltingLoveSmallSlime_f(x, y), false));
        }
    }

    @Override
    public void onVictory() {
        super.onVictory();
        this.counter = 0;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Adoration_r();
    }
}
