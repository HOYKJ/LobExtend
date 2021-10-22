package lobExtendMod.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import lobExtendMod.monster.MeltingLoveSmallSlime;
import lobExtendMod.monster.MeltingLove_m;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.card.rareCard.MeltingLove;
import lobotomyMod.npc.AbstractNPC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author hoykj
 */
public class MeltingLove_npc extends AbstractNPC {
    public AnimatedNpc anim;
    private AnimationState state;
    private int counter;
    private ArrayList<MeltingLoveSmallSlime> slimes = new ArrayList<>();

    public MeltingLove_npc(){
        this.ID = MeltingLove.ID;
        this.needRemove = false;
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if(room == null){
            return;
        }

        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
        }
    }

    public void init(){
        float x = AbstractDungeon.player.drawX + 240.0F * Settings.scale;
        float y = AbstractDungeon.player.drawY;
        this.anim = new AnimatedNpc(x, y, "lobExtendMod/images/monsters/MeltingLove/Normal/templer_transform.atlas", "lobExtendMod/images/monsters/MeltingLove/Normal/templer_transform.json", "0_default");
        try {
            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/MeltingLove/Normal/templer_transform.atlas", "lobExtendMod/images/monsters/MeltingLove/Normal/templer_transform.json", 1.8F);

            this.anim.skeleton.setPosition(x, y);
            Field state_t = this.anim.getClass().getDeclaredField("state");
            Field stateData = this.anim.getClass().getDeclaredField("stateData");
            state_t.setAccessible(true);
            stateData.setAccessible(true);
            this.state = (AnimationState)state_t.get(this.anim);
            this.state.setAnimation(0, "0_default", true);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        this.needRemove = false;
        this.counter = 0;
        this.slimes.clear();
        if(room instanceof MonsterRoom){
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        this.counter ++;
        if (this.counter >= 3){
            this.counter -= 1;
            float x = MathUtils.random(0, Settings.WIDTH - 180);
            float y = MathUtils.random(AbstractDungeon.floorY - 20, AbstractDungeon.floorY + 20);
            this.slimes.add(new MeltingLoveSmallSlime(x, y));
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(this.slimes.get(this.slimes.size() - 1), false));
        }

        for (int i = 0; i < this.slimes.size(); i ++){
            if (this.slimes.get(i).isDeadOrEscaped()){
                this.slimes.remove(i);
            }
        }

        if (this.slimes.size() >= 5){
            this.needRemove = true;
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new MeltingLove_m(AbstractDungeon.player.drawX + 240.0F * Settings.scale, AbstractDungeon.player.drawY), false));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.anim != null) {
            this.anim.render(sb);
        }
    }

    @Override
    public void dispose() {
        if (this.anim != null) {
            this.anim.dispose();
        }
    }
}
