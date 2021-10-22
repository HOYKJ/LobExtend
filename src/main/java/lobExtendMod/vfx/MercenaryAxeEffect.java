package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import lobExtendMod.LobExtendMod;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.monster.Mercenary_m;
import lobotomyMod.action.common.DelayDamageAction;
import lobotomyMod.helper.LobotomyImageMaster;
import lobotomyMod.monster.sephirah.Geburah;

/**
 * @author hoykj
 */
public class MercenaryAxeEffect extends AbstractGameEffect {
    private Mercenary_m mercenary;
    private DelayDamageAction delay;
    private float x, y;
    private boolean out, attacked;
    private DamageInfo info;
    private Texture img;
    private boolean right;
    private AbstractCreature target;

    public MercenaryAxeEffect(Mercenary_m mercenary, DamageInfo info, AbstractCreature target){
        this.mercenary = mercenary;
        this.delay = new DelayDamageAction(target);
        AbstractDungeon.actionManager.addToBottom(this.delay);
        this.out = false;
        this.attacked = false;
        this.info = info;
        this.img = LobExtendImageMaster.MERCENARY_AXE;
        this.target = target;
        this.scale = 1 / 2.4F;
    }

    public void update(){
        if(this.mercenary.getAttachment("axe_1") == null && this.x != 0){
            this.out = true;
        }

        if(!this.out){
            this.x = this.mercenary.getSkeleton().getX() + this.mercenary.getBone("bone9").getWorldX();
            this.y = this.mercenary.getSkeleton().getY() + this.mercenary.getBone("bone9").getWorldY();
            this.right = this.x < this.target.hb.cX;
        }
        else {
            if(this.right){
                this.x += 2000 * Gdx.graphics.getDeltaTime();
                this.rotation -= 720 * Gdx.graphics.getDeltaTime();
            }
            else {
                this.x -= 2000 * Gdx.graphics.getDeltaTime();
                this.rotation += 720 * Gdx.graphics.getDeltaTime();
            }
        }

        if(!this.attacked && this.out && (this.right? this.x > this.target.hb.cX: this.x < this.target.hb.cX)){
            AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, false));
            this.delay.damage(this.info);
            this.attacked = true;
        }

        if((this.right? this.x > Settings.WIDTH : this.x < -this.img.getWidth() / 2.4F)){
            this.isDone = true;
            this.delay.laterEnd();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(this.out){
            sb.setColor(Color.WHITE.cpy());
            //sb.draw(this.region, this.x - this.region.getRegionWidth() * 0.58F, this.y - this.region.getRegionHeight() * 0.58F, this.region.getRegionWidth() * 1.16F, this.region.getRegionHeight() * 1.16F);
            sb.draw(this.img, this.x, this.y, this.img.getWidth() / 2.0F, this.img.getHeight() / 2.0F, this.img.getWidth(), this.img.getHeight(), this.scale, this.scale,
                    this.rotation, 0, 0, 222, 217, !this.right, false);
        }
    }

    public void dispose(){}
}
