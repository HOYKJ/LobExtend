package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.helper.LobExtendImageMaster;

/**
 * @author hoykj
 */
public class ButterflyCastEffect extends AbstractGameEffect {
    private Texture img;
    private float x, y, vX, vY;
    private float timer;
    private int imgNum;
    private boolean right;

    public ButterflyCastEffect(float x, float y, boolean right){
        this.imgNum = MathUtils.random(2);
        this.img = LobExtendImageMaster.BUTTERFLY_CAST[this.imgNum];
        this.duration = MathUtils.random(0.8F, 1.4F);
        this.startingDuration = this.duration;
        this.timer = 0.2F;

        this.x = x;
        this.y = y;
        this.vX = MathUtils.random(80.0F, 160.0F);
        this.vY = MathUtils.random(-80.0F, 80.0F);
        if(!right){
            this.vX = -this.vX;
        }
        this.right = right;
        this.color = Color.WHITE.cpy();
        this.scale = MathUtils.random(0.2F, 0.25F);
        this.rotation = MathUtils.random(-45.0F, 45.0F);
    }

    public void update(){
        this.timer -= Gdx.graphics.getDeltaTime();
        if(this.timer <= 0){
            this.timer += 0.2F;
            this.imgNum ++;
            if(this.imgNum > 2){
                this.imgNum = 0;
            }
            this.img = LobExtendImageMaster.BUTTERFLY_CAST[this.imgNum];
        }

        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();

        this.duration -= Gdx.graphics.getDeltaTime();
        if(this.duration <= 0.0F){
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, this.img.getWidth() / 2.0F, this.img.getHeight() / 2.0F, this.img.getWidth(), this.img.getHeight(), this.scale, this.scale,
                this.rotation, 0, 0, 128, 128, !this.right, false);
    }

    public void dispose(){}
}
