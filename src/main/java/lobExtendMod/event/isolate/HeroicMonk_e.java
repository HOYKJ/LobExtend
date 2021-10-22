package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.PurificationShrine;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.HeroicMonk_npc;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobotomyMod.relic.CogitoBucket;

/**
 * @author hoykj
 */
public class HeroicMonk_e extends AbstractIsolate {
    public static final String ID = "HeroicMonk_e";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("HeroicMonk_e");
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

    public HeroicMonk_e(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/HeroicMonk/TreeTree.atlas", "lobExtendMod/images/monsters/HeroicMonk/TreeTree.json",
                "0_Default", 2.0F);
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
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    if (AbstractDungeon.player.hasRelic(CogitoBucket.ID)){
                        HeroicMonk_npc hmn = new HeroicMonk_npc();
                        if (!CogitoBucket.hasNPC(hmn.ID)){
                            CogitoBucket.npcs_t.add(hmn);
                        }
                    }
                    if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1,
                                PurificationShrine.OPTIONS[2], false, false, false, true);
                    }
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    this.screen = CurScreen.LEAVE;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    if (AbstractDungeon.player.hasRelic(CogitoBucket.ID)){
                        HeroicMonk_npc hmn = new HeroicMonk_npc();
                        if (!CogitoBucket.hasNPC(hmn.ID)){
                            CogitoBucket.npcs_t.add(hmn);
                        }
                    }
                    if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1,
                                PurificationShrine.OPTIONS[2], false, false, false, true);
                    }
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
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            CardCrawlGame.sound.play("CARD_EXHAUST");
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0), (Settings.WIDTH / 2.0F), (Settings.HEIGHT / 2.0F)));
            AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.root.updateNpc();
    }

    @Override
    public Texture getImg() {
        if (this.entered) {
            return LobExtendImageMaster.HEROIC_MONK;
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
