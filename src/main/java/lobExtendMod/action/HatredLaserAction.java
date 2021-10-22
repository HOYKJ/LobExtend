package lobExtendMod.action;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.attachments.Attachment;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.LobExtendMod;
import lobExtendMod.vfx.HatredLaserEffect;

/**
 * @author hoykj
 */
public class HatredLaserAction extends AbstractGameAction {
    private Skeleton skeleton;
    private float timer;
    private boolean start;
    private HatredLaserEffect effect;
    private AnimationState state;
    private DamageInfo damage;

    public HatredLaserAction(Skeleton skeleton, AnimationState state, DamageInfo damage){
        this.skeleton = skeleton;
        this.state = state;
        this.duration = 3;
        this.timer = 0.25F;
        this.start = false;
        this.damage = damage;
    }

    public void update(){
        this.skeleton.findSlot("mouth/smile").setAttachment(null);
        if(this.state.getTracks().get(0).getAnimation().getName().equals("2_Casting_attack_loop")){
            this.start = true;
        }
        if(this.start){
            if(this.effect == null) {
                this.effect = new HatredLaserEffect(this.skeleton);
                AbstractDungeon.effectsQueue.add(this.effect);
            }

            this.duration -= Gdx.graphics.getDeltaTime();
            this.timer -= Gdx.graphics.getDeltaTime();
            if(this.duration < 0 && this.timer < 0){
                this.isDone = true;
            }
            if(this.timer <= 0){
                AbstractDungeon.player.heal(2);
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if(!m.isDeadOrEscaped()){
                        m.damage(this.damage);
                    }
                }
                this.timer += 0.25F;
            }
            if(this.duration < 0.25F){
                this.effect.end();
            }
        }
    }
}
