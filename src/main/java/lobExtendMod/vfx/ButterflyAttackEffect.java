package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.helper.LobExtendImageMaster;
import lobotomyMod.monster.WhiteNightMonster;

/**
 * @author hoykj
 */
public class ButterflyAttackEffect extends AbstractGameEffect {
    private Texture img;
    private float x, y, vX, vY;
    private float timer;
    private int imgNum;

    public ButterflyAttackEffect(AbstractCreature target){
        this.imgNum = MathUtils.random(2);
        this.img = LobExtendImageMaster.BUTTERFLY_ATTACK[this.imgNum];
        this.duration = MathUtils.random(0.8F, 1.4F);
        this.startingDuration = this.duration;
        this.timer = 0.2F;

        this.x = target.hb.cX + MathUtils.random(-4.0F, 4.0F);
        this.y = target.hb.cY + MathUtils.random(-6.0F, 6.0F);
        this.vX = MathUtils.random(-120.0F, 120.0F);
        this.vY = MathUtils.random(10.0F, 120.0F);
        this.color = Color.WHITE.cpy();
        this.scale = MathUtils.random(0.3F, 0.5F);
    }

    public void update(){
        this.timer -= Gdx.graphics.getDeltaTime();
        if(this.timer <= 0){
            this.timer += 0.2F;
            this.imgNum ++;
            if(this.imgNum > 2){
                this.imgNum = 0;
            }
            this.img = LobExtendImageMaster.BUTTERFLY_ATTACK[this.imgNum];
        }

        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.color.a = this.duration / this.startingDuration;

        this.duration -= Gdx.graphics.getDeltaTime();
        if(this.duration <= 0.0F){
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, this.img.getWidth() * this.scale, this.img.getHeight() * this.scale);
    }

    public void dispose(){}
}
