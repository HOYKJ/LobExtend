package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

/**
 * @author hoykj
 */
public class FieryFlameEffect extends AbstractGameEffect {
    private Texture img;
    private boolean strong;

    public FieryFlameEffect(){
        this.img = ImageMaster.loadImage("lobExtendMod/images/texture/IsolateFilter.png");

        this.color = Color.WHITE.cpy();
        this.color.a = 0.6F;
        this.strong = true;
    }

    public void update(){
        if (this.strong){
            this.color.a += Gdx.graphics.getDeltaTime() * 0.05F;
            if (this.color.a >= 0.8F){
                this.color.a = 0.8F;
                this.strong = false;
            }
        }
        else {
            this.color.a -= Gdx.graphics.getDeltaTime() * 0.05F;
            if (this.color.a <= 0.6F){
                this.color.a = 0.6F;
                this.strong = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }

    public void dispose(){}
}
