package lobExtendMod.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import lobExtendMod.LobExtendMod;
import lobExtendMod.relic.LetterFromLob;
import lobExtendMod.relic.RedMistMask;

/**
 * @author hoykj
 */
public class LetterPatch {
    @SpirePatch(
            clz= CombatRewardScreen.class,
            method="setupItemReward"
    )
    public static class setupItemReward {
        @SpireInsertPatch(rloc = 0)
        public static void Insert(CombatRewardScreen _inst){
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom){
                if (!AbstractDungeon.player.hasRelic(LetterFromLob.ID) && AbstractDungeon.player.damagedThisCombat == 0){
                    boolean flag = true;
                    for (RewardItem ri : AbstractDungeon.getCurrRoom().rewards){
                        if (ri.relic != null && ri.relic.relicId.equals(LetterFromLob.ID)){
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(new LetterFromLob()));
                    }
                }
                if (!AbstractDungeon.player.hasRelic(RedMistMask.ID) && AbstractDungeon.player.damagedThisCombat == 0){
                    if (MathUtils.random(1000) <= 4){
                        boolean flag = true;
                        for (RewardItem ri : AbstractDungeon.getCurrRoom().rewards){
                            if (ri.relic != null && ri.relic.relicId.equals(RedMistMask.ID)){
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(new RedMistMask()));
                        }
                    }
                }
            }
        }
    }
}
