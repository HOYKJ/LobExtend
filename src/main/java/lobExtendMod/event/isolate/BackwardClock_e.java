package lobExtendMod.event.isolate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.npc.event.ArmorSoul;
import lobExtendMod.relic.InspiredRelic;
import lobExtendMod.relic.LetterFromLob;
import lobotomyMod.vfx.BackwardEffect;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class BackwardClock_e extends AbstractIsolate {
    public static final String ID = "BackwardClock_e";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("BackwardClock_e");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;
    private float duration;
    private boolean start;

    private static enum CurScreen {
        INTRO, CHOOSE, LEAVE
    }

    public BackwardClock_e(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/BackwardClock/skeleton.atlas", "lobExtendMod/images/monsters/BackwardClock/skeleton.json",
                "0_default", 2.0F);
        this.duration = 4.5F;
        this.start = false;
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        this.title = NAME;
        this.body = INTRO_MSG;
        this.roomEventText.addDialogOption(MainRoom.OPTIONS[6]);
        this.roomEventText.addDialogOption(MainRoom.OPTIONS[7]);
        this.roomEventText.addDialogOption(MainRoom.OPTIONS[5]);

        this.hasDialog = true;
        this.hasFocus = true;

        this.root.stay(1);
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                if (buttonPressed == 0) {
                    this.screen = CurScreen.CHOOSE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[0]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[7]);
                    this.roomEventText.clearRemainingOptions();
                    this.roomEventText.addDialogOption(MainRoom.OPTIONS[5]);
                    this.root.stay(1);
                    this.root.onCheck();
                }
                else if (buttonPressed == 1){
                    this.changeState();
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    this.changeState();
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case LEAVE:

                break;
        }
    }

    private void changeState(){
        if (this.start){
            return;
        }
        this.npc.state.setAnimation(0, "1_clockwork_move", false);
        this.npc.state.addAnimation(0, "2_clockwork_move_loop", false, 0.0F);
        this.npc.state.addAnimation(0, "3_clockwork_put", false, 0.0F);
        this.npc.state.addAnimation(0, "4_clock_work_work", true, 0.0F);
        this.start = true;
    }

    @Override
    public void update() {
        super.update();
        this.root.updateNpc();
        if (!this.start){
            return;
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0){
            CardCrawlGame.sound.play("WarpClock_Skill_Start");
            AbstractDungeon.topLevelEffects.add(new BackwardEffect());
            if (this.duration <= -1){
                AbstractDungeon.player.loseRelic(LetterFromLob.ID);
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
            }
        }
    }

    @Override
    public Texture getImg() {
        if (this.entered) {
            return LobExtendImageMaster.BACKWARD_CLOCK;
        }
        else {
            return LobExtendImageMaster.HIDDEN;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.npc != null){
            this.npc.render(sb);
        }
        this.root.renderNpc(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.npc != null){
            this.npc.dispose();
        }
    }
}
