package lobExtendMod.relic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import lobExtendMod.event.LobotomyEvent;
import lobotomyMod.relic.AbstractLobotomyRelic;

/**
 * @author hoykj
 */
public class LetterFromLob extends AbstractLobotomyRelic {
    public static final String ID = "LetterFromLob";
    private int bc;
    private boolean active;

    public LetterFromLob() {
        super("LetterFromLob",  RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        this.counter = 0;
        this.bc = 0;
        this.active = false;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (this.counter > this.bc){
            this.counter = this.bc;
        }
        this.counter ++;
        if (AbstractDungeon.mapRng.random(120) < this.counter){
            this.counter -= 60;
            this.active = true;
        }
        this.bc = this.counter;
        if (this.active) {
            if (AbstractDungeon.getCurrRoom() instanceof EventRoom) {
                this.active = false;
                AbstractDungeon.topLevelEffects.clear();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.currMapNode.room = new EventRoom();
                AbstractDungeon.getCurrRoom().event = new LobotomyEvent();
                AbstractDungeon.getCurrRoom().event.onEnterRoom();
                CardCrawlGame.fadeIn(1.5F);
                AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
                AbstractDungeon.overlayMenu.hideCombatPanels();
            }
        }
    }

    @Override
    public void atPreBattle() {
        super.atPreBattle();
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss || AbstractDungeon.getCurrRoom() instanceof VictoryRoom){
            return;
        }
        if (this.active) {
            this.active = false;
            AbstractDungeon.topLevelEffects.clear();
            AbstractDungeon.effectList.clear();
            AbstractDungeon.currMapNode.room = new EventRoom();
            AbstractDungeon.getCurrRoom().event = new LobotomyEvent();
            AbstractDungeon.getCurrRoom().event.onEnterRoom();
            CardCrawlGame.fadeIn(1.5F);
            AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
            AbstractDungeon.overlayMenu.hideCombatPanels();
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new LetterFromLob();
    }
}
