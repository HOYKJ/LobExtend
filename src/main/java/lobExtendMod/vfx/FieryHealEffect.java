package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

/**
 * @author hoykj
 */
public class FieryHealEffect extends AbstractGameEffect {
    private Texture img;
    private boolean heal;

    public FieryHealEffect(){
        this.img = ImageMaster.loadImage("lobExtendMod/images/texture/IsolateFilter.png");
        this.duration = -1.4F;

        this.color = Color.WHITE.cpy();
        this.color.a = 0;
        this.heal = true;
    }

    public void update(){
        this.duration += Gdx.graphics.getDeltaTime();
        if (this.duration >= 4.0F){
            this.isDone = true;
            this.color.a = 0;
        }
        else if (this.duration >= 2.0F){
            this.color.a = (4.0F - this.duration) / 2;
            if (this.heal){
                this.heal = false;
                AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth / 2);
            }
        }
        else if (this.duration >= 0F){
            this.color.a = this.duration / 2;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }

    public void dispose(){}
}
