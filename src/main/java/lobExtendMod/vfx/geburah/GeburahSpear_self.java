package lobExtendMod.vfx.geburah;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.attachments.Attachment;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import lobotomyMod.action.common.DelayDamageAction;
import lobotomyMod.helper.LobotomyImageMaster;
import lobotomyMod.monster.sephirah.Geburah;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author hoykj
 */
public class GeburahSpear_self extends AbstractGameEffect {
    private AbstractPlayer geburah;
    private DelayDamageAction delay;
    private float x, y;
    private boolean out, start;
    private TextureRegion region;

    public GeburahSpear_self(){
        this.geburah = AbstractDungeon.player;
        this.out = false;
        this.start = false;
        this.region = LobotomyImageMaster.GEBURAH_SPEAR;
        this.delay = new DelayDamageAction();
        AbstractDungeon.actionManager.addToBottom(this.delay);
    }

    public void update(){
        if(this.getAttachment("R_Weapon") == null || !this.getAttachment("R_Weapon").getName().equals("spear")){
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
            this.x += 3000 * Gdx.graphics.getDeltaTime();
        }

        if(this.out && this.x > Settings.WIDTH){
            this.isDone = true;
            this.delay.end();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(this.out){
            sb.setColor(Color.WHITE.cpy());
            sb.draw(this.region, this.x - this.region.getRegionWidth() * 0.58F, this.y - this.region.getRegionHeight() * 0.58F, this.region.getRegionWidth() * 0.58F, 0,
                    this.region.getRegionWidth() * 1.16F, this.region.getRegionHeight() * 1.16F, -1, 1, 0);
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
