package lobExtendMod.relic;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class MagicBullet_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "MagicBullet_r";
    private boolean active;

    public MagicBullet_r()
    {
        super("MagicBullet_r",  RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        this.counter = 1;
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
            if (this.counter < 7){
                AbstractDungeon.getRandomMonster().damage(new DamageInfo(AbstractDungeon.player, 20, DamageInfo.DamageType.THORNS));
            }
            else if (this.counter == 7){
                AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 20, DamageInfo.DamageType.THORNS));
                this.counter = 0;
            }
            this.counter ++;
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new MagicBullet_r();
    }
}
