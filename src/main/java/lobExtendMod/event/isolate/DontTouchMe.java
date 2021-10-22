package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.npc.event.ArmorSoul;
import lobExtendMod.relic.InspiredRelic;
import lobExtendMod.vfx.DontTouchEffect;

/**
 * @author hoykj
 */
public class DontTouchMe extends AbstractIsolate {
    public static final String ID = "DontTouchMe";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("DontTouchMe");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private Texture npc;

    private static enum CurScreen {
        INTRO, LEAVE
    }

    public DontTouchMe(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = ImageMaster.loadImage("lobExtendMod/images/monsters/DontTouch/DontTouch.png");
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
                    this.screen = CurScreen.LEAVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[0]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    AbstractDungeon.effectsQueue.add(new DontTouchEffect(true));
                    if (MathUtils.randomBoolean()){
                        CardCrawlGame.sound.play("touch_dead1");
                    }
                    else {
                        CardCrawlGame.sound.play("touch_dead2");
                    }
                    this.root.stay(1);
                    this.root.onCheck();
                }
                else if (buttonPressed == 1){
                    this.screen = CurScreen.LEAVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[0]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    AbstractDungeon.effectsQueue.add(new DontTouchEffect(false));
                    CardCrawlGame.sound.play("touch_moodDown");
                    this.root.onAction();
                }
                else {
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
            return LobExtendImageMaster.DONT_TOUCH_ME;
        }
        else {
            return LobExtendImageMaster.HIDDEN;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        sb.setColor(Color.WHITE.cpy());
        sb.draw(this.npc, Settings.WIDTH * 0.75F - this.npc.getWidth() / 2.0F * 0.75F, AbstractDungeon.floorY, this.npc.getWidth() * 0.75F, this.npc.getHeight() * 0.75F);
        this.root.renderNpc(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.npc.dispose();
    }
}
