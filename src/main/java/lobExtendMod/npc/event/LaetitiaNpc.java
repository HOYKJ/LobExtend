package lobExtendMod.npc.event;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import lobExtendMod.event.isolate.AbstractIsolate;
import lobExtendMod.monster.WitchMonster;
import lobExtendMod.relic.LaetitiaHeart;

/**
 * @author hoykj
 */
public class LaetitiaNpc extends AbstractActionNpc {

    public LaetitiaNpc(){
    }

    @Override
    public void onEnterRoom(AbstractIsolate isolate) {
        super.onEnterRoom(isolate);
        if (AbstractDungeon.player.hasRelic(LaetitiaHeart.ID)){
            ((LaetitiaHeart)AbstractDungeon.player.getRelic(LaetitiaHeart.ID)).broken();
            AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new AbstractMonster[]{new WitchMonster(0.0F, 0.0F)});
            AbstractDungeon.getCurrRoom().rewardAllowed = false;
            isolate.enterCombat();
        }
    }
}
