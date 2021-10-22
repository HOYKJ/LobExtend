package lobExtendMod.npc.event;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lobExtendMod.LobExtendMod;
import lobExtendMod.event.isolate.AbstractIsolate;

/**
 * @author hoykj
 */
public class BunnyNpc extends AbstractActionNpc {
    private AbstractIsolate isolate;
    private AnimateEventNpc npc;

    public BunnyNpc(AbstractIsolate isolate){
        this.isolate = isolate;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.7F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/Bunny/escape/Bunny_Escaped.atlas", "lobExtendMod/images/monsters/Bunny/escape/Bunny_Escaped.json",
                "Default", 1.0F);
        this.npc.addListener(new AnimationState.AnimationStateAdapter() {
            public void event(int trackIndex, Event event) {
                if (event.getData().getName().equals("Damage")) {
                    AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 50));
                }
            }
        });
        LobExtendMod.logger.info(this.isolate.getTitle());
    }

    @Override
    public void onCheck(AbstractIsolate isolate) {
        super.onCheck(isolate);
        if (isolate == this.isolate){
            this.npc.state.setAnimation(0, "Start", false);
            this.npc.state.setTimeScale(1F);
            this.npc.state.addAnimation(0, "Default", true, 0.0F);
        }
    }

    @Override
    public void onAction(AbstractIsolate isolate) {
        super.onAction(isolate);
        if (isolate == this.isolate){
            this.npc.state.setAnimation(0, "Start", false);
            this.npc.state.setTimeScale(1F);
            this.npc.state.addAnimation(0, "Default", true, 0.0F);
        }
    }

    @Override
    public void render(SpriteBatch sb, AbstractIsolate isolate) {
        super.render(sb, isolate);
        if (isolate == this.isolate){
            if (this.npc != null){
                this.npc.render(sb);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.npc != null){
            this.npc.dispose();
        }
    }
}
