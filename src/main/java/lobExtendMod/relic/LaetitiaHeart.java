package lobExtendMod.relic;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import lobExtendMod.monster.friendlyMonster.WitchMonster_f;
import lobotomyMod.character.LobotomyHandler;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class LaetitiaHeart extends AbstractLobotomyAbnRelic {
    public static final String ID = "LaetitiaHeart";
    private boolean first;

    public LaetitiaHeart()
    {
        super("LaetitiaHeart",  RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss){
            this.broken();
            float x = MathUtils.random(Settings.WIDTH * 0.25F, Settings.WIDTH * 0.75F);
            AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new WitchMonster_f(x, 0), false));
        }
    }

    public void broken(){
        this.img = ImageMaster.loadImage(LobotomyHandler.lobotomyRelicImage("LaetitiaHeart_b"));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new LaetitiaHeart();
    }
}
