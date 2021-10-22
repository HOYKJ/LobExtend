package lobExtendMod.npc.event;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lobExtendMod.event.isolate.AbstractIsolate;
import lobExtendMod.event.isolate.ArmorCreature;
import lobExtendMod.relic.InspiredRelic;

/**
 * @author hoykj
 */
public class ArmorSoul extends AbstractActionNpc {
    private boolean checked, actioned;

    public ArmorSoul(){
        this.checked = false;
        this.actioned = false;
    }

    @Override
    public void onEnterRoom(AbstractIsolate isolate) {
        super.onEnterRoom(isolate);
        this.checked = false;
        this.actioned = false;
    }

    @Override
    public void onCheck(AbstractIsolate isolate) {
        super.onCheck(isolate);
        this.checked = true;
    }

    @Override
    public void onAction(AbstractIsolate isolate) {
        super.onAction(isolate);
        if (isolate instanceof ArmorCreature){
            return;
        }
        this.actioned = true;
        if (!this.checked){
            if (AbstractDungeon.player.hasRelic(InspiredRelic.ID)){
                if (AbstractDungeon.player.getRelic(InspiredRelic.ID).counter < 4) {
                    ((InspiredRelic) AbstractDungeon.player.getRelic(InspiredRelic.ID)).increaseCounter();
                    switch (AbstractDungeon.player.getRelic(InspiredRelic.ID).counter) {
                        case 2:
                            AbstractDungeon.player.maxHealth -= 5;
                            break;
                        case 3:
                            AbstractDungeon.player.maxHealth -= 5;
                            break;
                        case 4:
                            AbstractDungeon.player.maxHealth -= 10;
                            break;
                    }

                    if (AbstractDungeon.player.currentHealth > AbstractDungeon.player.maxHealth){
                        AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
                    }
                }
            }
        }
    }

    @Override
    public void onLeave(AbstractIsolate isolate) {
        super.onLeave(isolate);
        if (isolate instanceof ArmorCreature){
            return;
        }
        if (!this.checked && !this.actioned){
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, AbstractDungeon.player.maxHealth));
        }
    }
}
