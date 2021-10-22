package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.ui.button.ChooseRoomButton;
import lobExtendMod.ui.button.TutorialButton;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class MainRoom extends AbstractIsolate {
    public static final String ID = "MainRoom";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("LobotomyEvent");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = DESCRIPTIONS[0];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private TutorialButton tutorialButton;
    private ChooseRoomButton chooseRoomButton;

    private static enum CurScreen {
        INTRO, INTRO2, CHOOSE
    }

    public MainRoom(LobotomyEvent root){
        super(NAME, INTRO_MSG, "images/events/winding.jpg");
//        this.imageEventText.setDialogOption(OPTIONS[0]);
//        this.imageEventText.setDialogOption(OPTIONS[1]);
        this.root = root;
        this.tutorialButton = new TutorialButton();
        this.chooseRoomButton = new ChooseRoomButton(this.root);
        this.tutorialButton.show();
        this.chooseRoomButton.show();
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        this.title = NAME;
        this.body = INTRO_MSG;
        this.imageEventText.loadImage("images/events/winding.jpg");
        this.noCardsInRewards = false;
        this.imageEventText.show(this.title, this.body);
        if (this.screen == CurScreen.CHOOSE){
            this.updateBodyText();
            this.imageEventText.setDialogOption(OPTIONS[4]);
            this.imageEventText.setDialogOption(OPTIONS[5]);
        }
        else {
            this.imageEventText.setDialogOption(OPTIONS[0]);
            this.imageEventText.setDialogOption(OPTIONS[1]);
        }
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                if (buttonPressed == 0) {
                    this.screen = CurScreen.INTRO2;
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.imageEventText.clearRemainingOptions();
                }
                else {
                    this.openMap();
                }
                break;
            case INTRO2:
                this.screen = CurScreen.CHOOSE;
                this.updateBodyText();
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.setDialogOption(OPTIONS[5]);
                break;
            case CHOOSE:
                if (buttonPressed == 0) {
                    this.root.unlockInfo();
                    this.root.stay(1);
                    this.updateBodyText();
                }
                else {
                    this.openMap();
                }
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.screen == CurScreen.CHOOSE){
            this.tutorialButton.update();
            this.chooseRoomButton.update();
        }
    }

    @Override
    public void renderText(SpriteBatch sb) {
        super.renderText(sb);
        if (this.screen == CurScreen.CHOOSE){
            this.tutorialButton.render(sb);
            this.chooseRoomButton.render(sb);
        }
    }

    public void updateBodyText(){
        StringBuilder str = new StringBuilder(DESCRIPTIONS[2] + DESCRIPTIONS[3] + this.root.stayTime + DESCRIPTIONS[4] + " ");
        int no = 0;
        ArrayList<AbstractIsolate> list = this.root.getRandomIsolates();
        for (AbstractIsolate isolate : list){
            if (isolate == this){
                continue;
            }
            if (isolate.info){
                no ++;
                str.append(no).append(". ").append(isolate.getTitle()).append(" NL ");
            }
        }
        if (no == 0){
            str.append(DESCRIPTIONS[5]);
        }
        this.imageEventText.updateBodyText(str.toString());
    }

    @Override
    public Texture getImg() {
        return null;
    }
}
