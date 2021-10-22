package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.npc.event.LaetitiaNpc;
import lobExtendMod.relic.LaetitiaHeart;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class Laetitia_e extends AbstractIsolate {
    public static final String ID = "Laetitia_e";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Laetitia_e");
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

    public Laetitia_e(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/Laetitia/wich.atlas", "lobExtendMod/images/monsters/Laetitia/wich.json",
                "Default", 2.6F);
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

        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        CardCrawlGame.music.playTempBgmInstantly("Witch_Work.mp3", true);
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
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    this.changeState();
                    AbstractDungeon.effectList.add(new LatterEffect(()->{
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,  RelicLibrary.getRelic(LaetitiaHeart.ID).makeCopy());
                    }, 1));
                    this.root.addNpc(new LaetitiaNpc());
                    this.root.onAction();
                }
                else {
                    CardCrawlGame.music.silenceTempBgmInstantly();
                    CardCrawlGame.music.unsilenceBGM();
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    this.screen = CurScreen.LEAVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    this.changeState();
                    AbstractDungeon.effectList.add(new LatterEffect(()->{
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,  RelicLibrary.getRelic(LaetitiaHeart.ID).makeCopy());
                    }, 1));
                    this.root.addNpc(new LaetitiaNpc());
                    this.root.onAction();
                }
                else {
                    CardCrawlGame.music.silenceTempBgmInstantly();
                    CardCrawlGame.music.unsilenceBGM();
                    this.root.setIsolate(0);
                }
                break;
            case LEAVE:
                CardCrawlGame.music.silenceTempBgmInstantly();
                CardCrawlGame.music.unsilenceBGM();
                this.root.setIsolate(0);
                break;
        }
    }

    private void changeState(){
        this.npc.state.setAnimation(0, "Special", false);
        this.npc.state.setTimeScale(1F);
        this.npc.state.addAnimation(0, "Default", true, 0.0F);
    }

    @Override
    public Texture getImg() {
        if (this.entered) {
            return LobExtendImageMaster.LAETITIA;
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
