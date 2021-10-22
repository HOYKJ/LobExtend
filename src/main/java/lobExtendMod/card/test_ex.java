package lobExtendMod.card;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import lobExtendMod.npc.TestNpc;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.relic.CogitoBucket;
import lobotomyMod.room.NewVictoryRoom;
import lobotomyMod.vfx.action.LatterEffect;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class test_ex extends AbstractCard {
    public static final String ID = "test_ex";
    private static final int COST = 0;

    public test_ex() {
        super("test_ex", "test_ex", "red/attack/reaper",  0, "test_ex", CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.exhaust = true;
    }

    public void use(final AbstractPlayer p, final AbstractMonster m) {
        AbstractDungeon.effectList.add(new LatterEffect(()->{
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                CardCrawlGame.dungeon = new Exordium(AbstractDungeon.player, new ArrayList<>());
                AbstractDungeon.floorNum = 0;
                AbstractDungeon.firstRoomChosen = false;
                MapRoomNode node = new MapRoomNode(-1, -1);
                node.room = new EmptyRoom();
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.nextRoomTransitionStart();
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                AbstractDungeon.dungeonMapScreen.open(true);
            }));
        }, 1.5F));
    }

    public AbstractCard makeCopy() {
        return new test_ex();
    }

    public void upgrade() {
    }

    static {
    }
}
