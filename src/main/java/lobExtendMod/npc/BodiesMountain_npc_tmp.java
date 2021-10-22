package lobExtendMod.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.monster.BodiesMountain_m;
import lobotomyMod.card.rareCard.BodiesMountain;
import lobotomyMod.npc.AbstractNPC;

import java.util.Iterator;

/**
 * @author hoykj
 */
public class BodiesMountain_npc_tmp extends AbstractNPC {
    private BodiesMountain_m monster;

    public BodiesMountain_npc_tmp(BodiesMountain_m monster){
        this.ID = BodiesMountain.ID;
        this.needRemove = false;
        this.monster = monster;
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        if (this.needRemove){
            return;
        }
//        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()){
//            return;
//        }
        this.needRemove = true;
//        this.monster.restart(m);
        BodiesMountain_m bm = new BodiesMountain_m(this.monster.drawX, this.monster.drawY, m);
        bm.restart(m, this.monster);
        //AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(this.monster, false));

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onSpawnMonster(bm);
        }

        bm.init();
        bm.applyPowers();
        int position = 0;

        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (bm.drawX > mo.drawX) {
                ++position;
            }
        }

        AbstractDungeon.getCurrRoom().monsters.addMonster(position, bm);

        bm.showHealthBar();
        if (ModHelper.isModEnabled("Lethality")) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(bm, bm, new StrengthPower(bm, 3), 3));
        }

        if (ModHelper.isModEnabled("Time Dilation")) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(bm, bm, new SlowPower(bm, 0)));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {
    }
}
