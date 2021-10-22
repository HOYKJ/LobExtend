package lobExtendMod.vfx.geburah;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.attachments.Attachment;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobotomyMod.action.common.DelayAction;
import lobotomyMod.helper.LobotomyImageMaster;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author hoykj
 */
public class GeburahGreed_self extends AbstractGameEffect {
    private AbstractPlayer geburah;
    private DelayAction delay;
    private float x, y;
    private boolean out, start;
    private TextureRegion region;

    public GeburahGreed_self(){
        this.geburah = AbstractDungeon.player;
        this.delay = new DelayAction();
        AbstractDungeon.actionManager.addToBottom(this.delay);
        this.out = false;
        this.start = false;
        this.region = LobotomyImageMaster.GEBURAH_GREED;
    }

    public void update(){
        if(this.getAttachment("Weapon_Unique_Greed") == null){
            if(this.start) {
                this.out = true;
            }
        }
        else {
            this.start = true;
        }

        if(!this.out){
            this.x = Objects.requireNonNull(this.getSkeleton()).getX() + this.getBone("bone15").getWorldX();
            this.y = this.getSkeleton().getY() + this.getBone("bone15").getWorldY();
        }
        else {
            this.x += 2000 * Gdx.graphics.getDeltaTime();
        }

        if(this.out && this.x > Settings.WIDTH){
            this.delay.end();
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(this.out){
            sb.setColor(Color.WHITE.cpy());
            sb.draw(this.region, this.x - this.region.getRegionWidth() * 0.18F, this.y - this.region.getRegionHeight() * 0.18F, this.region.getRegionWidth() * 0.18F,
                    this.region.getRegionHeight() * 0.36F, this.region.getRegionWidth() * 0.36F, this.region.getRegionHeight() * 0.36F, -1, 1, -90);
        }
    }

    private Attachment getAttachment(String name){
        return Objects.requireNonNull(this.getSkeleton()).findSlot(name).getAttachment();
    }

    private Bone getBone(String name){
        return Objects.requireNonNull(this.getSkeleton()).findBone(name);
    }

    private Skeleton getSkeleton(){
        try {
            Field skeleton = SuperclassFinder.getSuperclassField(this.geburah.getClass(), "skeleton");
            skeleton.setAccessible(true);
            return (Skeleton)skeleton.get(this.geburah);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dispose(){}
}
