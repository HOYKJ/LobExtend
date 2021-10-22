package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.helper.LobExtendImageMaster;

/**
 * @author hoykj
 */
public class EnchantMarkEffect extends AbstractGameEffect {
    private float x, y;

    public EnchantMarkEffect(AbstractCreature target) {
        this.duration = 1.0F;
        this.color = Color.WHITE.cpy();
        this.x = target.hb.cX;
        this.y = target.hb.y + target.hb.height;
    }

    public void update() {

    }

    public void changeColor(){
        this.color = Color.RED.cpy();
    }

    public void end(){
        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        Texture img = LobExtendImageMaster.BIGBIRD_MARK;
        sb.draw(img, this.x - img.getWidth() / 6.0F, this.y, img.getWidth() / 3.0F, img.getHeight() / 3.0F);
    }

    public void dispose() {}
}
