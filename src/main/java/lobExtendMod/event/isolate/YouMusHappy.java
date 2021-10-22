package lobExtendMod.event.isolate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;

/**
 * @author hoykj
 */
public class YouMusHappy extends AbstractIsolate {
    public static final String ID = "YouMusHappy";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("YouMusHappy");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;
    private int usedTime, changeTime;
    private boolean yes, active;
    private float timer;

    private static enum CurScreen {
        INTRO, CHOOSE, ACTIVE
    }

    public YouMusHappy(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/YouMusHappy/chungBaek.atlas", "lobExtendMod/images/monsters/YouMusHappy/chungBaek.json",
                "Default", 1.0F);
        this.usedTime = 0;
        this.yes = true;
        this.timer = 2.5F;
        this.changeTime = 0;
        this.active = false;
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
                    this.screen = CurScreen.ACTIVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.roomEventText.updateDialogOption(0, OPTIONS[0]);
                    this.roomEventText.clearRemainingOptions();
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F, AbstractDungeon.player.drawY + 80.0F * Settings.scale);
                    this.changeState("ACTIVE");
                    this.active = true;
                    this.npc.skeleton.findSlot("no").setAttachment(this.npc.skeleton.getAttachment("no", "yes"));
                    this.usedTime ++;
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    this.screen = CurScreen.ACTIVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.roomEventText.updateDialogOption(0, OPTIONS[0]);
                    this.roomEventText.clearRemainingOptions();
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F, AbstractDungeon.player.drawY + 80.0F * Settings.scale);
                    this.changeState("ACTIVE");
                    this.active = true;
                    this.npc.skeleton.findSlot("no").setAttachment(this.npc.skeleton.getAttachment("no", "yes"));
                    this.usedTime ++;
                    this.root.onAction();
                }
                else {
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.25F, AbstractDungeon.floorY);
                    this.root.setIsolate(0);
                }
                break;
            case ACTIVE:
                this.screen = CurScreen.CHOOSE;
                this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[7]);
                this.roomEventText.clearRemainingOptions();
                this.roomEventText.addDialogOption(MainRoom.OPTIONS[5]);
                AbstractDungeon.player.movePosition(Settings.WIDTH * 0.25F, AbstractDungeon.floorY);

                if (this.yes){
                    this.changeState("SUCCESS");
                    AbstractDungeon.player.maxHealth += this.changeTime / 6;
                }
                else {
                    this.changeState("FAIL");
                    AbstractDungeon.player.maxHealth -= this.changeTime / 2;
                    if (AbstractDungeon.player.maxHealth < 1){
                        AbstractDungeon.player.maxHealth = 1;
                    }
                    if (AbstractDungeon.player.currentHealth > AbstractDungeon.player.maxHealth){
                        AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
                    }
                }

                this.npc.skeleton.findSlot("no").setAttachment(null);
                this.yes = true;
                this.timer = 2.5F;
                this.changeTime = 0;
                this.active = false;
                break;
        }
    }

    private void changeState(String key){
        switch (key) {
            case "ACTIVE":
                this.npc.state.setAnimation(0, "Active_Start", false);
                this.npc.state.setTimeScale(1F);
                this.npc.state.addAnimation(0, "Active", true, 0.0F);
                break;
            case "FAIL":
                this.npc.state.setAnimation(0, "Fail", false);
                this.npc.state.setTimeScale(1F);
                this.npc.state.addAnimation(0, "Default", true, 0.0F);
                break;
            case "SUCCESS":
                this.npc.state.setAnimation(0, "Success", false);
                this.npc.state.setTimeScale(1F);
                this.npc.state.addAnimation(0, "Default", true, 0.0F);
                break;
            case "KILL":
                this.npc.state.setAnimation(0, "Dead", false);
                this.npc.state.setTimeScale(1F);
                this.npc.state.getTracks().get(0).setListener(new AnimationState.AnimationStateAdapter() {
                    public void event(int trackIndex, Event event) {
                        if (event.getData().getName().equals("Effect")) {
                            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, AbstractDungeon.player.maxHealth));
                        }
                    }
                });
                this.npc.state.addAnimation(0, "Default", true, 0.0F);
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
            return LobExtendImageMaster.YOU_MUS_HAPPY;
        }
        else {
            return LobExtendImageMaster.HIDDEN;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.active) {
            if (this.usedTime >= 5){
                this.active = false;
                this.changeState("KILL");
                return;
            }
            this.timer -= Gdx.graphics.getDeltaTime();
            if (this.timer <= 0) {
                this.yes = !this.yes;
                this.changeTime ++;
                if (this.yes) {
                    this.npc.skeleton.findSlot("no").setAttachment(this.npc.skeleton.getAttachment("no", "yes"));
                } else {
                    this.npc.skeleton.findSlot("no").setAttachment(this.npc.skeleton.getAttachment("no", "no"));
                }
                this.timer += Math.pow(0.9F, this.changeTime) * 2.5F;
                if (this.timer < 0.05F){
                    this.timer = 0.05F;
                }
            }
        }
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
