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
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.npc.event.ArmorSoul;
import lobExtendMod.relic.InspiredRelic;

/**
 * @author hoykj
 */
public class ArmorCreature extends AbstractIsolate {
    public static final String ID = "ArmorCreature";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("ArmorCreature");
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

    public ArmorCreature(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY - 20.0F * Settings.scale,
                "lobExtendMod/images/monsters/ArmorCreature/ArmorCreature.atlas", "lobExtendMod/images/monsters/ArmorCreature/ArmorCreature.json",
                "Standing", 3.2F);
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
                    AbstractRelic relic = new InspiredRelic();
                    relic.onEquip();
                    AbstractDungeon.player.relics.add(relic);
                    AbstractDungeon.player.reorganizeRelics();
                    this.root.addNpc(new ArmorSoul());
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case CHOOSE:
                if (buttonPressed == 0){
                    AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, AbstractDungeon.player.maxHealth));
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
            return LobExtendImageMaster.ARMOR_CREATURE;
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
