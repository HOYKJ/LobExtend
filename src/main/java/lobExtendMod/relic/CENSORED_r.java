package lobExtendMod.relic;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.vfx.FearEffect;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class CENSORED_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "CENSORED_r";

    public CENSORED_r()
    {
        super("CENSORED_r",  RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
            if (!monster.isDeadOrEscaped()){
                AbstractDungeon.effectsQueue.add(new FearEffect(monster, 5));
            }
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new CENSORED_r();
    }
}
