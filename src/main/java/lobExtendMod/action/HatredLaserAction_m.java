package lobExtendMod.action;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.vfx.HatredLaserEffect;
import lobExtendMod.vfx.HatredLaserEffect_m;

/**
 * @author hoykj
 */
public class HatredLaserAction_m extends AbstractGameAction {
    private Skeleton skeleton;
    private float timer;
    private boolean start;
    private HatredLaserEffect_m effect;
    private AnimationState state;
    private DamageInfo damage;
    private AbstractMonster qoh;

    public HatredLaserAction_m(Skeleton skeleton, AnimationState state, DamageInfo damage, AbstractMonster qoh){
        this.skeleton = skeleton;
        this.state = state;
        this.duration = 3;
        this.timer = 0.25F;
        this.start = false;
        this.damage = damage;
        this.qoh = qoh;
    }

    public void update(){
        if(this.state.getTracks().get(0).getAnimation().getName().equals("4_Casting_attack_loop")){
            this.start = true;
        }
        if(this.start){
            if(this.effect == null) {
                this.effect = new HatredLaserEffect_m(this.skeleton);
                AbstractDungeon.effectsQueue.add(this.effect);
            }

            this.duration -= Gdx.graphics.getDeltaTime();
            this.timer -= Gdx.graphics.getDeltaTime();
            if(this.duration < 0 && this.timer < 0){
                this.isDone = true;
            }
            if(this.timer <= 0){
                AbstractDungeon.player.damage(this.damage);
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if(!m.isDeadOrEscaped()){
                        m.damage(this.damage);
                    }
                }
                this.qoh.heal(2);
                this.timer += 0.25F;
            }
            if(this.duration < 0.25F){
                this.effect.end();
            }
        }
    }
}
