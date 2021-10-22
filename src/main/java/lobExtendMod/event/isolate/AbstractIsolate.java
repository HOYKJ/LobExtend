package lobExtendMod.event.isolate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.scene.EventBgParticle;
import lobExtendMod.LobExtendMod;
import lobotomyMod.relic.CogitoBucket;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public abstract class AbstractIsolate extends AbstractEvent {
    protected String title;
    private EventType recordType;
    public boolean entered = false, info = false;
    public boolean needEnd, reopen;

    public AbstractIsolate(){
        this.recordType = EventType.ROOM;
    }

    public AbstractIsolate(String title, String body, String imgUrl){
//        this.imageEventText.clear();
//        this.roomEventText.clear();
        this.title = title;
        this.body = body;
//        this.imageEventText.loadImage(imgUrl);
        type = EventType.IMAGE;
        this.recordType = EventType.IMAGE;
        //this.noCardsInRewards = false;
    }

    public void stayTime(int time){

    }

    public void totalTime(int time){

    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        type = this.recordType;
        if (type == EventType.ROOM){
            AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
        }
        else {
            AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
            AbstractDungeon.effectsQueue.add(new LatterEffect(()->{
                AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
            }));
        }

        this.entered = true;
        this.info = true;

        this.imageEventText.clear();
        this.roomEventText.clear();
    }

    @Override
    public void update() {
        if (type == EventType.ROOM) {
            super.update();
        }
        else {
            if (!this.combatTime) {
                this.hasFocus = true;
                if (MathUtils.randomBoolean(0.1F)) {
                    AbstractDungeon.effectList.add(new EventBgParticle());
                }

                if (this.waitTimer > 0.0F) {
                    this.waitTimer -= Gdx.graphics.getDeltaTime();
                    if (this.waitTimer < 0.0F) {
                        this.imageEventText.show(this.title, this.body);
                        this.waitTimer = 0.0F;
                    }
                }

                if (!GenericEventDialog.waitForInput) {
                    this.buttonEffect(GenericEventDialog.getSelectedOption());
                }
            }
        }
    }

    @Override
    public void showProceedScreen(String bodyText) {
        if (type == EventType.ROOM) {
            super.showProceedScreen(bodyText);
        }
        else {
            this.imageEventText.updateBodyText(bodyText);
            this.imageEventText.updateDialogOption(0, DESCRIPTIONS[0]);
            this.imageEventText.clearRemainingOptions();
            this.screenNum = 99;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (type == EventType.ROOM) {
            super.render(sb);
        }
    }

    @Override
    public void enterCombat() {
        if (AbstractDungeon.player.hasRelic(CogitoBucket.ID)) {
            CogitoBucket.level[0] -= 1;
        }
        super.enterCombat();
    }

    public void backCombat() {
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        AbstractDungeon.getCurrRoom().isBattleOver = false;
        AbstractDungeon.getCurrRoom().monsters.monsters.clear();
        AbstractDungeon.getCurrRoom().rewards.clear();
        AbstractDungeon.getCurrRoom().rewardTime = false;
        AbstractDungeon.combatRewardScreen.clear();
        this.hasFocus = true;
        this.combatTime = false;
        CardCrawlGame.fadeIn(1.5F);
    }

    public void enterCombatFromImage() {
        if (AbstractDungeon.player.hasRelic(CogitoBucket.ID)) {
            CogitoBucket.level[0] -= 1;
        }
        AbstractDungeon.getCurrRoom().smoked = false;
        AbstractDungeon.player.isEscaping = false;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
        AbstractDungeon.getCurrRoom().monsters.init();
        AbstractRoom.waitTimer = 0.1F;
        AbstractDungeon.player.preBattlePrep();
        this.hasFocus = false;
        GenericEventDialog.hide();
        CardCrawlGame.fadeIn(1.5F);
        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
        this.combatTime = true;
    }

    public void enterImageFromCombat() {
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
        AbstractDungeon.getCurrRoom().isBattleOver = false;
        AbstractDungeon.getCurrRoom().monsters.monsters.clear();
        AbstractDungeon.getCurrRoom().rewards.clear();
        this.hasDialog = true;
        this.hasFocus = true;
        this.combatTime = false;
        GenericEventDialog.show();
        CardCrawlGame.fadeIn(1.5F);
        AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
    }

    @Override
    public void reopen() {
        super.reopen();
        if (this.needEnd){
            return;
        }
        this.reopen = true;
        AbstractDungeon.resetPlayer();
        AbstractDungeon.player.drawX = Settings.WIDTH * 0.25F;
        AbstractDungeon.player.preBattlePrep();
        if (type == EventType.ROOM){
            this.backCombat();
        }
        else {
            this.enterImageFromCombat();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public abstract Texture getImg();
}
