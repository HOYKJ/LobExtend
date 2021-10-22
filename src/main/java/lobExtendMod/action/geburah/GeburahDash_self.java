package lobExtendMod.action.geburah;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * @author hoykj
 */
public class GeburahDash_self extends AbstractGameAction {
    private AbstractPlayer geburah;
    private boolean end, strong;

    public GeburahDash_self(boolean strong){
        this.geburah = AbstractDungeon.player;
        this.end = false;
        this.strong = strong;
    }

    public void update(){
        if(!this.strong) {
            if (this.geburah.state.getTracks().get(0).getAnimation().getName().equals("Phase_All_Teleport_02")) {
                this.geburah.animX += 2000.0F * Settings.scale * Gdx.graphics.getDeltaTime();
            }
        }
        else {
            if (this.geburah.state.getTracks().get(0).getAnimation().getName().equals("Phase_04_Attack_Run") ||
                    this.geburah.state.getTracks().get(0).getAnimation().getName().equals("Phase_04_Attack_Run_Attack")) {
                this.geburah.animX += 2000.0F * Settings.scale * Gdx.graphics.getDeltaTime();
            }
        }

        if (this.geburah.drawX + this.geburah.animX >= Settings.WIDTH) {
            this.end = true;
            this.geburah.animX = -80 * Settings.scale - this.geburah.drawX;
        }

        if(this.end && this.geburah.animX >= 0){
            this.geburah.animX = 0;
            if(!this.strong){
                this.geburah.state.setAnimation(0, "Phase_All_Teleport_03", false);
                this.geburah.state.addAnimation(0, "Phase_01_Default", true, 0.0F);
            }
            else {
                this.geburah.state.setAnimation(0, "Phase_04_Attack_End", false);
                this.geburah.state.addAnimation(0, "Phase_04_Default", true, 0.0F);
            }
            this.isDone = true;
        }
    }
}
