package lobExtendMod.event;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

/**
 * @author hoykj
 */
public class Shelter extends AbstractEvent {
    public static final String ID = "Shelter";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Shelter");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = DESCRIPTIONS[0];
    private CurScreen screen = CurScreen.INTRO;
    private int stayTime;

    private enum CurScreen
    {
        INTRO, NEXT, LEAVE;

        CurScreen() {}
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        AbstractDungeon.player.drawX += 200;
    }

    public Shelter(){
        this.body = INTRO_MSG;
        this.roomEventText.addDialogOption(OPTIONS[0]);
        this.roomEventText.addDialogOption(OPTIONS[1]);

        this.hasDialog = true;
        this.hasFocus = true;
        this.stayTime = 0;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen)
        {
            case INTRO:
                if (buttonPressed == 0){
                    this.screen = CurScreen.NEXT;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.stayTime ++;
                    AbstractDungeon.player.heal(4);
                }
                else {
                    this.screen = CurScreen.LEAVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[8]);
                    this.roomEventText.updateDialogOption(0, OPTIONS[1]);
                    this.roomEventText.clearRemainingOptions();
                }
                break;
            case NEXT:
                if (buttonPressed == 0){
                    switch (this.stayTime){
                        case 1:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                            break;
                        case 2:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[3]);
                            break;
                        case 3:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[4]);
                            break;
                        case 4:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[5]);
                            break;
                        case 5:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[6]);
                            break;
                        default:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[7]);
                            break;
                    }
                    for (int i = 1; i < this.stayTime; i ++){
                        if (AbstractDungeon.player.masterDeck.size() == 0){
                            break;
                        }
                        AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.player.masterDeck.getRandomCard(true));
                    }
                    this.stayTime ++;
                    AbstractDungeon.player.heal(4);
                }
                else {
                    this.screen = CurScreen.LEAVE;
                    switch (this.stayTime){
                        case 1: case 2:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[9]);
                            break;
                        case 3: case 4:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[10]);
                            break;
                        case 5: case 6:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[11]);
                            break;
                        default:
                            this.roomEventText.updateBodyText(DESCRIPTIONS[12]);
                            break;
                    }
                    this.roomEventText.updateDialogOption(0, OPTIONS[1]);
                    this.roomEventText.clearRemainingOptions();
                }
                break;
            case LEAVE:
                openMap();
                break;
        }
    }

    public void render(SpriteBatch sb) {
        super.render(sb);
    }
}
