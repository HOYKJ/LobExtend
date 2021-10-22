package lobExtendMod.relic;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.PurificationShrine;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import lobotomyMod.relic.toolAbnormality.AbstractLobotomyAbnRelic;

/**
 * @author hoykj
 */
public class Amita_r extends AbstractLobotomyAbnRelic {
    public static final String ID = "Amita_r";
    private boolean purge;

    public Amita_r()
    {
        super("Amita_r",  RelicTier.UNCOMMON, LandingSound.FLAT);
        this.purge = false;
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        if (m.isDeadOrEscaped() && !m.hasPower(MinionPower.POWER_ID)){
            AbstractDungeon.player.maxHealth += 1;
        }
    }

    @Override
    protected void onRightClick() {
        super.onRightClick();
        if (AbstractDungeon.getCurrRoom() instanceof EventRoom){
            return;
        }
        if (AbstractDungeon.player.maxHealth <= 10){
            return;
        }
        AbstractDungeon.player.maxHealth /= 2;
        if (AbstractDungeon.player.currentHealth > AbstractDungeon.player.maxHealth){
            AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
        }
        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1,
                PurificationShrine.OPTIONS[2], false, false, false, true);
        this.purge = true;
    }

    @Override
    public void update() {
        super.update();
        if (this.purge){
            if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                CardCrawlGame.sound.play("CARD_EXHAUST");
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0), (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
                AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.purge = false;
            }
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new Amita_r();
    }
}
