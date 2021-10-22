package lobExtendMod.event.isolate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.npc.event.AnimateEventNpc;
import lobExtendMod.relic.ShedSkin_r;

/**
 * @author hoykj
 */
public class NakedNestEgg extends AbstractIsolate {
    public static final String ID = "NakedNestEgg";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("NakedNestEgg");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = MainRoom.DESCRIPTIONS[6];
    private CurScreen screen = CurScreen.INTRO;
    private LobotomyEvent root;
    private AnimateEventNpc npc;
    private float chance;

    private static enum CurScreen {
        INTRO, CHOOSE, LEAVE
    }

    public NakedNestEgg(LobotomyEvent root){
        this.root = root;
        this.title = NAME;
        this.npc = new AnimateEventNpc(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,
                "lobExtendMod/images/monsters/NakedNest/egg.atlas", "lobExtendMod/images/monsters/NakedNest/egg.json",
                "Level0_Default", 1.4F);
        this.chance = 12;
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
                    this.roomEventText.updateDialogOption(0, MainRoom.OPTIONS[7]);
                    this.roomEventText.clearRemainingOptions();
                    this.roomEventText.addDialogOption(MainRoom.OPTIONS[5]);
                    this.action();
                    this.npc.state.setAnimation(0, "Level1_Default", true);
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
                    this.action();
                    this.npc.state.setAnimation(0, "Level1_Default", true);
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
            case LEAVE:
                if (buttonPressed == 0){
                    this.action();
                    this.root.onAction();
                }
                else {
                    this.root.setIsolate(0);
                }
                break;
        }
    }

    private void action(){
        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, AbstractDungeon.aiRng.random(5, 7)));
        if (AbstractDungeon.aiRng.random(100) < this.chance){
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.75F, AbstractDungeon.floorY,  RelicLibrary.getRelic(ShedSkin_r.ID).makeCopy());
            this.chance -= 100;
        }
        else {
            this.chance += 12;
            if (AbstractDungeon.aiRng.random(AbstractDungeon.player.maxHealth) * 0.75F > AbstractDungeon.player.currentHealth){
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Parasite(), Settings.WIDTH * 0.75F, AbstractDungeon.floorY));
            }
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
            return LobExtendImageMaster.NAKED_NEST;
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
