package lobExtendMod.relic;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.monster.friendlyMonster.LaLuna_f;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class Moonlight_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "Moonlight_r";
    private boolean active;

    public Moonlight_r()
    {
        super("Moonlight_r",  RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        this.counter = 0;
        this.active = false;
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        this.active = true;
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        if (this.active){
            this.active = false;
            this.counter ++;
            if (this.counter == 3){
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(
                        new LaLuna_f(MathUtils.random(200.0F * Settings.scale, Settings.WIDTH - 350.0F * Settings.scale), AbstractDungeon.floorY), false));
            }
            else if (this.counter > 3){
                this.counter -= 3;
            }
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Moonlight_r();
    }
}
