package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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
public class PromiseAndFaith extends AbstractIsolate {
    public static final String ID = "PromiseAndFaith";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("PromiseAndFaith");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;

    private static enum CurScreen {
        INTRO, CHOOSE
    }

    public PromiseAndFaith(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, 400.0F * Settings.scale,
                "lobExtendMod/images/monsters/OldBeliefAndPromise/skeleton.atlas", "lobExtendMod/images/monsters/OldBeliefAndPromise/skeleton.json",
                "1_Default", 0.8F);
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
                    CardGroup upgradable = AbstractDungeon.player.masterDeck.getUpgradableCards();
                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard card : upgradable.group){
                        if (card.rarity != AbstractCard.CardRarity.BASIC){
                            group.addToBottom(card);
                        }
                    }
                    if (group.size() != 0) {
                        AbstractDungeon.gridSelectScreen.open(group, 1, "", false, false, true, false);
                    }
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F - 380.0F * Settings.scale, AbstractDungeon.player.drawY);
                    this.root.onAction();
                }
                else {
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.25F, AbstractDungeon.player.drawY);
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    CardGroup upgradable = AbstractDungeon.player.masterDeck.getUpgradableCards();
                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard card : upgradable.group){
                        if (card.rarity != AbstractCard.CardRarity.BASIC){
                            group.addToBottom(card);
                        }
                    }
                    if (group.size() != 0) {
                        AbstractDungeon.gridSelectScreen.open(group, 1, "", false, false, true, false);
                    }
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F - 380.0F * Settings.scale, AbstractDungeon.player.drawY);
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
            case "START":
                this.npc.state.setAnimation(0, "2_Get_marble", false);
                this.npc.state.setTimeScale(1F);
                this.npc.state.addAnimation(0, "2to3", false, 0.0F);
                this.npc.state.addAnimation(0, "3_Doing", false, 0.0F);
                break;
            case "FAIL":
                this.npc.state.addAnimation(0, "3_Doing_to_4_fail", false, 0.0F);
                this.npc.state.addAnimation(0, "4_Fail", false, 0.0F);
                this.npc.state.addAnimation(0, "4_Fail_to_1", false, 0.0F);
                this.npc.state.addAnimation(0, "1_Default", true, 0.0F);
                break;
            case "SUCCESS":
                this.npc.state.addAnimation(0, "3_Doing_to_4_suceess", false, 0.0F);
                this.npc.state.addAnimation(0, "4_Sucess", false, 0.0F);
                this.npc.state.addAnimation(0, "5_Sucess_give_marble", false, 0.0F);
                this.npc.state.addAnimation(0, "5_Sucess_give_marble_to_1", false, 0.0F);
                this.npc.state.addAnimation(0, "1_Default", true, 0.0F);
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            this.changeState("START");
            if (AbstractDungeon.eventRng.random(100) >= 25){
                this.changeState("SUCCESS");
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
            }
            else {
                this.changeState("FAIL");
                int index = 0;
                while (AbstractDungeon.player.masterDeck.group.get(index) != c) {
                    index++;
                }
                AbstractDungeon.player.masterDeck.group.set(index, new Dazed());
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.root.updateNpc();
    }

    @Override
    public Texture getImg() {
        if (this.entered) {
            return LobExtendImageMaster.PROMISE_AND_FAITH;
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
