package lobExtendMod.helper;

import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.monster.BigBird_m;
import lobExtendMod.monster.DeadButterflies_m;
import lobExtendMod.monster.LongBird_m;
import lobExtendMod.monster.MeltingLove_m;
import lobExtendMod.npc.*;
import lobExtendMod.relic.*;
import lobExtendMod.vfx.CensoredFearAll;
import lobotomyMod.action.unique.RecallAbnormalityAction;
import lobotomyMod.card.rareCard.BlueStar;
import lobotomyMod.card.rareCard.BodiesMountain;
import lobotomyMod.card.rareCard.CENSORED;
import lobotomyMod.card.rareCard.MeltingLove;
import lobotomyMod.card.uncommonCard.*;
import lobotomyMod.npc.AbstractNPC;
import lobotomyMod.relic.CogitoBucket;

/**
 * @author hoykj
 */
public class LobExtendRecallHelper {

    public static void addRecallAbnormality(){
        RecallAbnormalityAction.recallMap.put(QueenOfHatred.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            addNpc(new QueenOfHatred_npc());
            removeCard(QueenOfHatred.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(InTheNameOfLoveAndHate_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(Funeral.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new DeadButterflies_m(0, 0), false));
            removeCard(Funeral.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(SolemnVow_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(Mercenary.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            addNpc(new Mercenary_npc());
            removeCard(Mercenary.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(WantedRequest.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(MeltingLove.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            addNpc(new MeltingLove_npc());
            removeCard(MeltingLove.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(Adoration_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(CENSORED.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            addNpc(new CENSORED_npc());
            removeCard(CENSORED.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(CENSORED_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(BodiesMountain.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            addNpc(new BodiesMountain_npc());
            removeCard(BodiesMountain.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(TheSmile_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(DespairKnight.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            addNpc(new DespairKnight_npc());
            removeCard(DespairKnight.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(SwordSharpenedByTears_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(LongBird.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new LongBird_m(0, 0), false));
            removeCard(LongBird.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(Justitia_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(BigBird.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new BigBird_m(0, 0), false));
            removeCard(BigBird.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(Lamp_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(BlueStar.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            addNpc(new BlueStar_npc());
            removeCard(BlueStar.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(SoundOfStar_r.ID);
            }
        });

        RecallAbnormalityAction.recallMap.put(Woodsman.ID, new RecallAbnormalityAction.relicAndRunnable(()->{
            addNpc(new Woodsman_npc());
            removeCard(Woodsman.ID);
        }){
            @Override
            public boolean canUse(AbstractRelic relic) {
                return !AbstractDungeon.player.hasRelic(Logging_r.ID);
            }
        });
    }

    private static void removeCard(String card){
        AbstractDungeon.player.masterDeck.removeCard(card);
        AbstractDungeon.player.drawPile.removeCard(card);
        AbstractDungeon.player.hand.removeCard(card);
        AbstractDungeon.player.discardPile.removeCard(card);
    }

    private static void addNpc(AbstractNPC npc){
        if (!CogitoBucket.hasNPC(npc.ID)){
            CogitoBucket.npcs.add(npc);
        }
    }
}
