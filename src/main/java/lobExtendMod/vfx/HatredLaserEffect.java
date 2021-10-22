package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

/**
 * @author hoykj
 */
public class HatredLaserEffect extends AbstractGameEffect {
    private float sX;
    private float sY;
    private static TextureAtlas.AtlasRegion img;
    private boolean end;

    public HatredLaserEffect(Skeleton skeleton){
        Bone bone, bone1;
        bone = skeleton.findBone("bone121");
        bone1 = skeleton.findBone("Pillar_1");
        float dX, dY;
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThin");
        }
        this.color = Color.WHITE.cpy();
        this.color.a = 0;
        this.sX = skeleton.getX() + bone.getWorldX();
        this.sY = skeleton.getY() + bone.getWorldY() + 88.0F * Settings.scale;
        dX = skeleton.getX() + bone1.getWorldX();
        dY= skeleton.getY() + bone1.getWorldY();

        this.rotation = MathUtils.atan2(dX - sX, dY - sY);
        this.rotation *= 57.295776F;
        //this.rotation = (-this.rotation + 90.0F);
        this.rotation = 0;
        this.end = false;
    }

    public void update(){
        if(!this.end){
            if(this.color.a < 1){
                this.color.a += Gdx.graphics.getDeltaTime() * 4;
            }
            else {
                this.color.a = 1;
            }
        }
        else {
            if(this.color.a > 0){
                this.color.a -= Gdx.graphics.getDeltaTime() * 4;
            }
            else {
                this.color.a = 0;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(img, this.sX, this.sY + 32.0F * Settings.scale, 0.0F, img.packedHeight / 2.0F,
                Settings.WIDTH * 1.5F, 50.0F, this.scale + MathUtils.random(-0.01F, 0.01F), this.scale * 8, this.rotation);
        sb.setColor(new Color(1.0F, 0.4F, 0.7F, this.color.a));
        sb.draw(img, this.sX, this.sY, 0.0F, img.packedHeight / 2.0F,
                Settings.WIDTH * 1.5F, MathUtils.random(50.0F, 60.0F), this.scale + MathUtils.random(-0.02F, 0.02F), this.scale * 8, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void end(){
        this.end = true;
    }

    public void dispose(){}
}
