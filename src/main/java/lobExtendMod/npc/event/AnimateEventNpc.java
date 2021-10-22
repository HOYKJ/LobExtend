package lobExtendMod.npc.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

/**
 * @author hoykj
 */
public class AnimateEventNpc {
    private TextureAtlas atlas = null;
    public Skeleton skeleton;
    public AnimationState state;
    public AnimationStateData stateData;
    public boolean flipHorizontal = false;
    public boolean flipVertical = false;

    public AnimateEventNpc(float x, float y, String atlasUrl, String skeletonUrl, String trackName, float scale) {
        this.loadAnimation(atlasUrl, skeletonUrl, scale);
        this.skeleton.setPosition(x, y);
        AnimationState.TrackEntry e = this.state.setAnimation(0, trackName, true);
        this.state.setTimeScale(1.0F);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    protected void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        this.atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.scale / scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
    }

    public void render(SpriteBatch sb) {
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setColor(Color.WHITE);
        this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
        sb.end();
        CardCrawlGame.psb.begin();
        AbstractCreature.sr.draw(CardCrawlGame.psb, this.skeleton);
        CardCrawlGame.psb.end();
        sb.begin();
        sb.setBlendFunction(770, 771);
    }

    public void render(SpriteBatch sb, Color color) {
        this.state.update(Gdx.graphics.getDeltaTime());
        this.state.apply(this.skeleton);
        this.skeleton.updateWorldTransform();
        this.skeleton.setFlip(false, false);
        this.skeleton.setColor(color);
        sb.end();
        CardCrawlGame.psb.begin();
        AbstractCreature.sr.draw(CardCrawlGame.psb, this.skeleton);
        CardCrawlGame.psb.end();
        sb.begin();
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
        this.atlas.dispose();
    }

    public void setTimeScale(float setScale) {
        this.state.setTimeScale(setScale);
    }

    public void addListener(AnimationState.AnimationStateAdapter listener) {
        this.state.addListener(listener);
    }
}
