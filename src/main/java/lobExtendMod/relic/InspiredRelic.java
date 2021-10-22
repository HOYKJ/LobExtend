package lobExtendMod.relic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.vfx.WantedRequestEffect;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class InspiredRelic extends AbstractLobotomyAbnRelic {
    public static final String ID = "InspiredRelic";

    public InspiredRelic()
    {
        super("InspiredRelic",  RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        this.counter = 0;
        this.increaseCounter();
    }

    public void increaseCounter(){
        this.counter ++;
        this.tips.clear();
        this.description = getUpdatedDescription();
        if (this.counter >= 2){
            this.tips.add(new PowerTip(this.DESCRIPTIONS[2], this.description));
        }
        else {
            this.tips.add(new PowerTip(this.name, this.description));
        }
        this.initializeTips();

        switch (this.counter){
            case 2:
                this.flavorText = this.DESCRIPTIONS[3];
                break;
            case 3:
                this.flavorText = this.DESCRIPTIONS[4];
                break;
            case 4:
                this.flavorText = this.DESCRIPTIONS[5];
                break;
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + this.counter + this.DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new InspiredRelic();
    }
}
