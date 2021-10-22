package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.relic.Moonlight_r;
import lobExtendMod.relic.ShedSkin_r;
import lobotomyMod.relic.ApostleMask;

/**
 * @author hoykj
 */
public class LaLuna_e extends AbstractIsolate {
    public static final String ID = "LaLuna_e";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("LaLuna_e");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;
    private AnimateEventNpc piano;
    private Texture chair;
    private boolean checked;

    private static enum CurScreen {
        INTRO, NEXT1, NEXT2, NEXT3, LEAVE
    }

    public LaLuna_e(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/LaLuna/skeleton.atlas", "lobExtendMod/images/monsters/LaLuna/skeleton.json",
                "0_default", 2.0F);
        this.piano = new AnimateEventNpc(Settings.WIDTH * 0.75F - 200.0F * Settings.scale, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/LaLuna/piano.atlas", "lobExtendMod/images/monsters/LaLuna/piano.json",
                "0_closed_default", 2.0F);
        this.chair = ImageMaster.loadImage("lobExtendMod/images/monsters/LaLuna/chair.png");
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

        this.checked = false;
        this.root.stay(1);
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                if (buttonPressed == 0) {
                    this.screen = CurScreen.NEXT1;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[0]);
                    this.roomEventText.updateDialogOption(0, OPTIONS[0] + OPTIONS[1] + (int) (AbstractDungeon.player.maxHealth * 0.1F) + OPTIONS[2]);
                    this.roomEventText.clearRemainingOptions();
                    this.roomEventText.addDialogOption(MainRoom.OPTIONS[5]);
                    this.checked = true;
                    this.root.stay(1);
                    this.root.onCheck();
                }
                else if (buttonPressed == 1){
                    this.screen = CurScreen.NEXT2;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[4]);
                    this.roomEventText.updateDialogOption(0, OPTIONS[0]);
                    this.roomEventText.clearRemainingOptions();
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F - 350.0F * Settings.scale, AbstractDungeon.player.drawY);
                    this.piano.state.setAnimation(0, "1_open", false);
                    this.piano.state.addAnimation(0, "2_opened_default", true, 0.0F);
                    AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, (int) (AbstractDungeon.player.maxHealth * 0.15F)));
                    this.action();
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case NEXT1:
                if (buttonPressed == 0){
                    this.screen = CurScreen.NEXT2;
                    this.roomEventText.updateBodyText(DESCRIPTIONS[1]);
                    AbstractDungeon.player.movePosition(Settings.WIDTH * 0.75F - 350.0F * Settings.scale, AbstractDungeon.player.drawY);
                    this.piano.state.setAnimation(0, "1_open", false);
                    this.piano.state.addAnimation(0, "2_opened_default", true, 0.0F);
                    AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, (int) (AbstractDungeon.player.maxHealth * 0.1F)));
                    this.action();
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case NEXT2:
                if (buttonPressed == 0){
                    this.screen = CurScreen.NEXT3;
                    if (this.checked){
                        this.roomEventText.updateBodyText(DESCRIPTIONS[2]);
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, (int) (AbstractDungeon.player.maxHealth * 0.1F)));
                        this.roomEventText.updateDialogOption(0, OPTIONS[0] + OPTIONS[1] + (int) (AbstractDungeon.player.maxHealth * 0.2F) + OPTIONS[3]);
                    }
                    else {
                        this.roomEventText.updateBodyText(DESCRIPTIONS[5]);
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, (int) (AbstractDungeon.player.maxHealth * 0.15F)));
                    }
                    this.action();
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case NEXT3:
                if (buttonPressed == 0){
                    this.screen = CurScreen.LEAVE;
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[5]);
                    this.roomEventText.clearRemainingOptions();
                    if (this.checked){
                        this.roomEventText.updateBodyText(DESCRIPTIONS[3]);
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, (int) (AbstractDungeon.player.maxHealth * 0.2F)));
                    }
                    else {
                        this.roomEventText.updateBodyText(DESCRIPTIONS[5]);
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, (int) (AbstractDungeon.player.maxHealth * 0.3F)));
                    }
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.75F, AbstractDungeon.floorY + 200.0F * Settings.scale,
                            RelicLibrary.getRelic(Moonlight_r.ID).makeCopy());
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
        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(),
                1, "", true, false, true, false);
    }

    @Override
    public void update() {
        super.update();
        this.root.updateNpc();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            c.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(c);
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    public Texture getImg() {
        if (this.entered) {
            return LobExtendImageMaster.LA_LUNA;
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
        if (this.piano != null){
            this.piano.render(sb);
        }
        sb.setColor(Color.WHITE.cpy());
        sb.draw(this.chair, Settings.WIDTH * 0.75F - 375.0F * Settings.scale, AbstractDungeon.floorY, this.chair.getWidth() / 2.4F, this.chair.getHeight() / 2.4F,
                    0, 0, this.chair.getWidth(), this.chair.getHeight(), true, false);
        this.root.renderNpc(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.npc != null){
            this.npc.dispose();
        }
        if (this.piano != null){
            this.piano.dispose();
        }
    }
}
