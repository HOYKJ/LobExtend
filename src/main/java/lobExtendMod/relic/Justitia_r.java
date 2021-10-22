package lobExtendMod.relic;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class Justitia_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "Justitia_r";

    public Justitia_r()
    {
        super("Justitia_r",  RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
        if (target == null){
            return;
        }
        int d = 4;
        for (AbstractPower p : target.powers){
            if (p.type == AbstractPower.PowerType.DEBUFF){
                d ++;
            }
        }
        float damage = target.maxHealth / 100.0F * d;
        AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(AbstractDungeon.player, (int) damage, DamageInfo.DamageType.HP_LOSS)));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Justitia_r();
    }
}
