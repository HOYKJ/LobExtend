package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.npc.event.HeavenNpc;

/**
 * @author hoykj
 */
public class BurrowingHeaven extends AbstractIsolate {
    public static final String ID = "BurrowingHeaven";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("BurrowingHeaven");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;

    private static enum CurScreen {
        INTRO, CHOOSE, LEAVE
    }

    public BurrowingHeaven(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/BurrowingHeaven/mustLook.atlas", "lobExtendMod/images/monsters/BurrowingHeaven/mustLook.json",
                "Default", 1.2F);
        this.npc.flipHorizontal = true;
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
                    this.screen = CurScreen.LEAVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    this.root.addNpc(new HeavenNpc());
                    this.root.onAction();
                }
                else {
                    this.root.addNpc(new HeavenNpc());
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    this.screen = CurScreen.LEAVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    this.root.onAction();
                }
                else {
                    this.root.addNpc(new HeavenNpc());
                    this.root.setIsolate(0);
                }
                break;
            case LEAVE:
                this.root.setIsolate(0);
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        this.root.updateNpc();
    }

    @Override
    public Texture getImg() {
        if (this.entered) {
            return LobExtendImageMaster.BURROWING_HEAVEN;
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
