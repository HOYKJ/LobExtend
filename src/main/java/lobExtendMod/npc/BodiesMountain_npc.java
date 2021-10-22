package lobExtendMod.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import lobExtendMod.monster.BodiesMountain_m;
import lobExtendMod.monster.CENSORED_m;
import lobExtendMod.vfx.RenderEffect;
import lobotomyMod.card.rareCard.BodiesMountain;
import lobotomyMod.npc.AbstractNPC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class BodiesMountain_npc extends AbstractNPC {
    public AnimatedNpc anim;
    private AnimationState state;
    private int bodies;

    public BodiesMountain_npc(){
        this.ID = BodiesMountain.ID;
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
        float x = Settings.WIDTH / 2.0F - 120 * Settings.scale;
        float y = AbstractDungeon.player.drawY;
        this.anim = new AnimatedNpc(x, y, "lobExtendMod/images/monsters/BodiesMountain/mouth monster.atlas", "lobExtendMod/images/monsters/BodiesMountain/mouth monster.json", "Level0_Default");
        try {
            Method loadAnimation = this.anim.getClass().getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(this.anim, "lobExtendMod/images/monsters/BodiesMountain/mouth monster.atlas", "lobExtendMod/images/monsters/BodiesMountain/mouth monster.json", 1.0F);

            this.anim.skeleton.setPosition(x, y);
            Field state_t = this.anim.getClass().getDeclaredField("state");
            Field stateData = this.anim.getClass().getDeclaredField("stateData");
            state_t.setAccessible(true);
            stateData.setAccessible(true);
            this.state = (AnimationState)state_t.get(this.anim);
            this.state.setAnimation(0, "Level0_Default", true);
            ((AnimationStateData)stateData.get(this.anim)).setMix("Level0_Default", "Level1_Change", 0.1F);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        //this.bodies = 0;
        if (this.bodies >= 1){
            this.changeAnim();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        this.needRemove = false;
        if(room instanceof MonsterRoom){
            init();
            AbstractDungeon.effectsQueue.add(new RenderEffect(this, true));
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        this.bodies ++;
        if (this.bodies == 1){
            this.changeAnim();
        }
        else if (this.bodies >= 2){
//            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()){
//                return;
//            }
            this.needRemove = true;
            //AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new BodiesMountain_m(Settings.WIDTH / 2.0F - 120 * Settings.scale, AbstractDungeon.player.drawY, m), false));

            AbstractMonster monster = new BodiesMountain_m(Settings.WIDTH / 2.0F - 120 * Settings.scale, AbstractDungeon.player.drawY, m);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(monster);
            }

            monster.init();
            monster.applyPowers();
            int position = 0;

            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (monster.drawX > mo.drawX) {
                    ++position;
                }
            }

            AbstractDungeon.getCurrRoom().monsters.addMonster(position, monster);

            monster.showHealthBar();
            if (ModHelper.isModEnabled("Lethality")) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, monster, new StrengthPower(monster, 3), 3));
            }

            if (ModHelper.isModEnabled("Time Dilation")) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, monster, new SlowPower(monster, 0)));
            }
        }
    }

    private void changeAnim(){
        this.state.setAnimation(0, "Level1_Change", false);
        this.state.setTimeScale(1F);
        this.state.addAnimation(0, "Level1_Default", true, 0.0F);
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
