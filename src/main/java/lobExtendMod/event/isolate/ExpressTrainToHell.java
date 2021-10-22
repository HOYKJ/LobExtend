package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobotomyMod.vfx.HellTrainEffect;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class ExpressTrainToHell extends AbstractIsolate {
    public static final String ID = "ExpressTrainToHell";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("ExpressTrainToHell");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;
    private int time;

    private static enum CurScreen {
        INTRO, CHOOSE
    }

    public ExpressTrainToHell(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/ExpressTrainToHell/ticketSeller.atlas", "lobExtendMod/images/monsters/ExpressTrainToHell/ticketSeller.json",
                "Default", 1.0F);
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
                    if (this.time == 0){
                        break;
                    }
                    this.changeState("SELL");
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F - 400.0F * Settings.scale, AbstractDungeon.player.drawY);
                    AbstractDungeon.effectList.add(new LatterEffect(()->{
                        CardCrawlGame.sound.play("Train_Sell");
                        int heal = 0;
                        switch (this.time){
                            case 4:
                                heal += 30;
                            case 3:
                                heal += 20;
                            case 2:
                                heal += 20;
                            case 1:
                                heal += 10;
                        }
                        AbstractDungeon.player.heal(heal);
                        this.time = 0;
                        this.npc.skeleton.findSlot("13").setAttachment(null);
                        this.npc.skeleton.findSlot("12").setAttachment(null);
                        this.npc.skeleton.findSlot("11").setAttachment(null);
                        this.npc.skeleton.findSlot("10").setAttachment(null);
                    }, 1.0F));
                    this.root.onAction();
                }
                else {
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.25F, AbstractDungeon.player.drawY);
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    if (this.time == 0){
                        break;
                    }
                    this.changeState("SELL");
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F - 400.0F * Settings.scale, AbstractDungeon.player.drawY);
                    AbstractDungeon.effectList.add(new LatterEffect(()->{
                        CardCrawlGame.sound.play("Train_Sell");
                        int heal = 0;
                        switch (this.time){
                            case 4:
                                heal += 30;
                            case 3:
                                heal += 20;
                            case 2:
                                heal += 20;
                            case 1:
                                heal += 10;
                        }
                        AbstractDungeon.player.heal(heal);
                        this.time = 0;
                        this.npc.skeleton.findSlot("13").setAttachment(null);
                        this.npc.skeleton.findSlot("12").setAttachment(null);
                        this.npc.skeleton.findSlot("11").setAttachment(null);
                        this.npc.skeleton.findSlot("10").setAttachment(null);
                    }, 1.0F));
                    this.root.onAction();
                }
                else {
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.25F, AbstractDungeon.player.drawY);
                    this.root.setIsolate(0);
                }
                break;
        }
    }

    private void changeState(String key){
        switch (key) {
            case "SELL":
                this.npc.state.setAnimation(0, "Sell", false);
                this.npc.state.setTimeScale(1F);
                this.npc.state.addAnimation(0, "Default", true, 0.0F);
                break;
        }
    }

    @Override
    public void stayTime(int time) {
        super.stayTime(time);
        this.time += time;
        if (this.time > 4){
            this.time -= 5;
            AbstractDungeon.effectList.add(new HellTrainEffect());
//            AbstractDungeon.effectList.add(new LatterEffect(()->{
//                this.damage = true;
//            }, 3.0F));
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 50, DamageInfo.DamageType.THORNS));
            this.npc.skeleton.findSlot("13").setAttachment(null);
            this.npc.skeleton.findSlot("12").setAttachment(null);
            this.npc.skeleton.findSlot("11").setAttachment(null);
            this.npc.skeleton.findSlot("10").setAttachment(null);
        }
        switch (this.time){
            case 4:
                this.npc.skeleton.findSlot("10").setAttachment(this.npc.skeleton.getAttachment("10", "10"));
            case 3:
                this.npc.skeleton.findSlot("11").setAttachment(this.npc.skeleton.getAttachment("11", "11"));
            case 2:
                this.npc.skeleton.findSlot("12").setAttachment(this.npc.skeleton.getAttachment("12", "12"));
            case 1:
                this.npc.skeleton.findSlot("13").setAttachment(this.npc.skeleton.getAttachment("13", "13"));
        }
    }

    @Override
    public Texture getImg() {
        if (this.entered) {
            return LobExtendImageMaster.EXPRESS_TRAIN_TO_HELL;
        }
        else {
            return LobExtendImageMaster.HIDDEN;
        }
    }

    @Override
    public void update() {
        super.update();
        this.root.updateNpc();
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
