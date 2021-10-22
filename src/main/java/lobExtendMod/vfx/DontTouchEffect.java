package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.helper.LobExtendImageMaster;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class DontTouchEffect extends AbstractGameEffect {
    private boolean dead;
    private int imgNum;
    private boolean playSfx;

    public DontTouchEffect(boolean dead){
        this.imgNum = 0;
        this.duration = 0.05F;
        this.startingDuration = this.duration;
        this.dead = dead;
        if (!this.dead){
            this.imgNum ++;
        }
        this.color = Color.WHITE.cpy();
        this.playSfx = true;
    }

    public void update(){
        this.duration -= Gdx.graphics.getDeltaTime();
        if(this.duration <= 0.0F){
            if (this.dead){
                if (this.imgNum >= 84){
                    this.isDone = true;
                    AbstractDungeon.player.maxHealth = 1;
                    AbstractDungeon.player.currentHealth = 1;
                }
                else {
                    this.imgNum ++;
                    this.duration += this.startingDuration;
                }
            }
            else {
                if (this.imgNum >= 100){
                    this.isDone = true;
                    for (AbstractCard card : AbstractDungeon.player.masterDeck.group){
                        AbstractDungeon.effectsQueue.add(new LatterEffect(()->{
                            AbstractDungeon.player.masterDeck.group.remove(card);
                        }));
                    }
                }
                else {
                    this.imgNum ++;
                    this.duration += this.startingDuration;
                }
            }
        }

        if (this.playSfx){
            this.playSfx = false;
            if (this.dead){
                if (MathUtils.randomBoolean()){
                    CardCrawlGame.sound.play("touch_dead1");
                }
                else {
                    CardCrawlGame.sound.play("touch_dead2");
                }
            }
            else {
                CardCrawlGame.sound.play("touch_moodDown");
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        if (this.dead){
            sb.draw(LobExtendImageMaster.DONT_TOUCH_DEAD[this.imgNum], 0, 0, Settings.WIDTH, Settings.HEIGHT);
        }
        else {
            sb.draw(LobExtendImageMaster.DONT_TOUCH_DOWN[this.imgNum], 0, 0, Settings.WIDTH, Settings.HEIGHT);
        }
    }

    public void dispose(){}
}
