package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

/**
 * @author hoykj
 */
public class ButterflyCastRange extends AbstractGameEffect {
    private AnimationState state;
    private Skeleton skeleton;
    private float startX, endX;
    private float startY, endY;
    private float vX;
    private float x;
    private boolean right, start, end;
    private float timer;

    public ButterflyCastRange(AnimationState state, Skeleton skeleton, boolean right){
        this.state = state;
        this.skeleton = skeleton;
        this.startX = 0;
        this.right = right;
        this.vX = 400;
        this.start = false;
        this.end = false;
        this.timer = 0.1F;
    }

    public void update(){
        if(this.state.getTracks().get(0).getAnimation().getName().equals("speical2") && !this.start){
            CardCrawlGame.sound.play("Butterfly_Skill");
            this.start = true;
        }
        else if(this.state.getTracks().get(0).getAnimation().getName().equals("speical3")){
            if(!this.end) {
                CardCrawlGame.sound.play("Butterfly_Close");
                this.end = true;
            }
        }

        if(this.start){
            if(this.startX == 0){
                this.startX = this.skeleton.getX() + this.skeleton.findBone("coffin_side_cover").getWorldX();
                this.endX = this.startX;
                this.startY = this.skeleton.getY() + this.skeleton.findBone("coffin_side_cover").getWorldY() - 20;
                this.endY = this.startY + 200;
            }
            if(this.right) {
                this.endX += this.vX * Gdx.graphics.getDeltaTime();
                if(this.endX > Settings.WIDTH){
                    this.endX = Settings.WIDTH;
                }
            }
            else {
                this.endX -= this.vX * Gdx.graphics.getDeltaTime();
                if(this.endX < 0){
                    this.endX = 0;
                }
            }

            this.timer -= Gdx.graphics.getDeltaTime();
            if(this.timer < 0) {
                this.timer += 0.1F;
                this.x = this.startX;
                if (this.right) {
                    while (this.x < this.endX) {
                        this.x += 20;
                        float y = MathUtils.random(this.startY, this.endY);
                        AbstractDungeon.effectsQueue.add(new ButterflyCastEffect(this.x, y, this.right));
                    }
                } else {
                    while (this.x > this.endX) {
                        this.x -= 20;
                        float y = MathUtils.random(this.startY, this.endY);
                        AbstractDungeon.effectsQueue.add(new ButterflyCastEffect(this.x, y, this.right));
                    }
                }
            }
        }

        if(this.end){
            if(this.right) {
                this.startX += this.vX * Gdx.graphics.getDeltaTime();
                if(this.startX > this.endX){
                    this.isDone = true;
                }
            }
            else {
                this.startX -= this.vX * Gdx.graphics.getDeltaTime();
                if(this.startX < this.endX){
                    this.isDone = true;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    public void dispose(){}
}
