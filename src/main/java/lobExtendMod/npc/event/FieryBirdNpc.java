package lobExtendMod.npc.event;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import lobExtendMod.event.isolate.AbstractIsolate;
import lobExtendMod.event.isolate.FieryBird_e;
import lobExtendMod.monster.FieryBird_m;

/**
 * @author hoykj
 */
public class FieryBirdNpc extends AbstractActionNpc {
    private FieryBird_e event;

    public FieryBirdNpc(FieryBird_e event){
        this.event = event;
    }

    @Override
    public void onEnterRoom(AbstractIsolate isolate) {
        super.onEnterRoom(isolate);
        this.remove = true;
        AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[]{new FieryBird_m(0.0F, 0.0F, this.event)});
        AbstractDungeon.getCurrRoom().rewardAllowed = false;
        isolate.enterCombat();
    }
}
