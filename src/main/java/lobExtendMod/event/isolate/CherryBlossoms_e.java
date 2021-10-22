package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.DeathScreen;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.npc.event.ArmorSoul;
import lobExtendMod.relic.InspiredRelic;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class CherryBlossoms_e extends AbstractIsolate {
    public static final String ID = "CherryBlossoms_e";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("CherryBlossoms_e");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;
    private int counter;
    private boolean stop;

    private static enum CurScreen {
        INTRO, CHOOSE, LEAVE
    }

    public CherryBlossoms_e(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/CherryBlossoms/skeleton.atlas", "lobExtendMod/images/monsters/CherryBlossoms/skeleton.json",
                "1_level_1", 1.0F);
        this.npc.flipHorizontal = true;
        this.counter = 1;
        this.stop = false;
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
        if (this.stop){
            return;
        }
        switch (this.screen) {
            case INTRO:
                if (buttonPressed == 0) {
                    this.screen = CurScreen.CHOOSE;
                    String str = DESCRIPTIONS[1];
                    switch (this.counter){
                        case 2:
                            str = DESCRIPTIONS[2];
                            break;
                        case 3:
                            str = DESCRIPTIONS[3];
                            break;
                        case 4: case 5:
                            str = DESCRIPTIONS[4];
                            break;
                    }
                    this.roomEventText.updateBodyText(DESCRIPTIONS[0] + str);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[7]);
                    this.roomEventText.clearRemainingOptions();
                    this.roomEventText.addDialogOption(MainRoom.OPTIONS[5]);
                    this.root.stay(1);
                    this.root.onCheck();
                }
                else if (buttonPressed == 1){
                    this.action();
                    this.root.onAction();
                }
                else {
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.25F, AbstractDungeon.player.drawY);
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    this.action();
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

    private void action(){
        this.screen = CurScreen.LEAVE;
        if (this.counter != 4) {
            this.roomEventText.updateBodyText(DESCRIPTIONS[5]);
            this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
            this.roomEventText.clearRemainingOptions();
        }
        switch (this.counter){
            case 1:
                AbstractDungeon.player.heal((int) (AbstractDungeon.player.maxHealth * 0.05F));
                break;
            case 2:
                AbstractDungeon.player.heal((int) (AbstractDungeon.player.maxHealth * 0.1F));
                break;
            case 3:
                AbstractDungeon.player.heal((int) (AbstractDungeon.player.maxHealth * 0.25F));
                break;
            case 4:
                this.changeState(99);
                this.stop = true;
                AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F - 640.0F * Settings.scale, AbstractDungeon.player.drawY);
                AbstractDungeon.effectsQueue.add(new LatterEffect(()->{
                    AbstractDungeon.player.isDead = true;
                    AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
                    this.stop = false;
                }, 1.3F));
                break;
            case 5:
                this.changeState(9);
                this.stop = true;
                AbstractDungeon.effectsQueue.add(new LatterEffect(()->{
                    AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                    this.stop = false;
                }, 5.0F));
                break;
        }
    }

    @Override
    public void stayTime(int time) {
        super.stayTime(time);
        if (this.screen != CurScreen.INTRO){
            return;
        }
        this.counter += time;
        if (this.counter > 5){
            this.counter -= 5;
        }
        this.changeState(this.counter);
    }

    private void changeState(int key){
        switch (key) {
            case 1:
                this.npc.state.setAnimation(0, "1_level_1", true);
                this.npc.state.setTimeScale(1F);
                break;
            case 2:
                this.npc.state.setAnimation(0, "1_level_2", true);
                break;
            case 3:
                this.npc.state.setAnimation(0, "1_level_3", true);
                break;
            case 4:
                this.npc.state.setAnimation(0, "1_level_4", true);
                break;
            case 99:
                this.npc.state.setAnimation(0, "2_level_4_take_agent", false);
                this.npc.state.addAnimation(0, "1_level_4", true, 0.0F);
                break;
            case 9:
                this.npc.state.setAnimation(0, "2_level_4_explode", false);
                this.npc.state.addAnimation(0, "1_level_1", true, 0.0F);
                break;
            default:
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
            return LobExtendImageMaster.CHERRY_BLOSSOMS;
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
