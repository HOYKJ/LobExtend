package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.npc.event.ArmorSoul;
import lobExtendMod.npc.event.FieryBirdNpc;
import lobExtendMod.relic.InspiredRelic;
import lobExtendMod.vfx.FieryHealEffect;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class FieryBird_e extends AbstractIsolate {
    public static final String ID = "FieryBird_e";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("FieryBird_e");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;
    private int counter;

    private static enum CurScreen {
        INTRO, CHOOSE, LEAVE
    }

    public FieryBird_e(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/FieryBird/skeleton.atlas", "lobExtendMod/images/monsters/FieryBird/skeleton.json",
                "0_Default_inside", 1.2F);
        this.counter = 0;
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
                    if (this.counter == 2 || AbstractDungeon.player.maxHealth * 0.2F > AbstractDungeon.player.currentHealth){
                        this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.changeState("HEAL");
                        AbstractDungeon.effectList.add(new FieryHealEffect());
                    }
                    else {
                        this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                    }
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    this.screen = CurScreen.LEAVE;
                    if (this.counter == 2 || AbstractDungeon.player.maxHealth * 0.2F > AbstractDungeon.player.currentHealth){
                        this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.changeState("HEAL");
                        AbstractDungeon.effectList.add(new FieryHealEffect());
                    }
                    else {
                        this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                    }
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
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
    public void totalTime(int time) {
        super.totalTime(time);
        this.counter = time;
        if (this.counter == 3){
            this.root.addNpc(new FieryBirdNpc(this));
            this.changeState("ESCAPE");
        }
    }

    public void changeState(String key){
        switch (key) {
            case "HEAL":
                this.npc.state.setAnimation(0, "1_healing", false);
                this.npc.state.setTimeScale(1F);
                this.npc.state.addAnimation(0, "0_Default_inside", true, 0.0F);
                break;
            case "ESCAPE":
                this.npc.state.setAnimation(0, "0_escape_tree", true);
                break;
            case "BACK":
                this.npc.state.setAnimation(0, "0_Default_inside", true);
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
            return LobExtendImageMaster.FIERY_BIRD;
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
