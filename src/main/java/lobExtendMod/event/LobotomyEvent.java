package lobExtendMod.event;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import lobExtendMod.event.isolate.*;
import lobExtendMod.npc.event.AbstractActionNpc;
import lobotomyMod.vfx.action.LatterEffect;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class LobotomyEvent extends AbstractEvent {
    public static final String ID = "LobotomyEvent";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("LobotomyEvent");
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String INTRO_MSG = DESCRIPTIONS[0];
    private ArrayList<AbstractIsolate> isolateList = new ArrayList<>();
    private ArrayList<AbstractIsolate> randomList = new ArrayList<>();
    private AbstractIsolate isolate;
    public int stayTime;
    private ArrayList<AbstractActionNpc> npcs = new ArrayList<>();

    public LobotomyEvent(){
        this.initRandomIsolate();
        this.stayTime = 0;
    }

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        this.setIsolate(0);
    }

    @Override
    protected void buttonEffect(int i) {

    }

    public void stay(int time){
        this.stayTime += time;
        for (AbstractIsolate room : this.isolateList){
            room.stayTime(time);
            room.totalTime(this.stayTime);
        }
    }

    public void onCheck(){
        for (AbstractActionNpc npc : this.npcs){
            npc.onCheck(this.isolate);
        }
    }

    public void onAction(){
        for (AbstractActionNpc npc : this.npcs){
            npc.onAction(this.isolate);
        }
    }

    public void onLeave(){
        for (AbstractActionNpc npc : this.npcs){
            npc.onLeave(this.isolate);
        }
    }

    public void updateNpc(){
        for (AbstractActionNpc npc : this.npcs){
            if (npc.remove){
                AbstractDungeon.effectList.add(new LatterEffect(()->{
                    this.npcs.remove(npc);
                }));
            }
            else {
                npc.update(this.isolate);
            }
        }
        if (this.isolate.reopen){
            this.isolate.reopen = false;
            this.isolate.onEnterRoom();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD){
                AbstractDungeon.closeCurrentScreen();
            }
        }
    }

    public void renderNpc(SpriteBatch sb) {
        for (AbstractActionNpc npc : this.npcs){
            npc.render(sb, this.isolate);
        }
    }

    public void addNpc(AbstractActionNpc npc){
        this.npcs.add(npc);
    }

    public void unlockInfo(){
        int counter = 4, i = 1;
        while (counter > 0){
            counter --;
            while (this.isolateList.get(i).info){
                i ++;
                if (i >= this.isolateList.size() - 1){
                    break;
                }
            }
            this.isolateList.get(i).info = true;
            i ++;
            if (i >= this.isolateList.size() - 1){
                break;
            }
        }
    }

    public void setIsolate(int code){
        AbstractDungeon.player.movePosition(Settings.WIDTH * 0.25F, AbstractDungeon.player.drawY);
        if (!(this.isolate instanceof MainRoom)){
            this.onLeave();
        }
        this.isolate = this.isolateList.get(code);
        RoomEventDialog.optionList.clear();
        AbstractDungeon.getCurrRoom().event = this.isolate;
        AbstractDungeon.getCurrRoom().event.onEnterRoom();
        if (!(this.isolate instanceof MainRoom)){
            for (AbstractActionNpc npc : this.npcs){
                if (npc.remove){
                    AbstractDungeon.effectList.add(new LatterEffect(()->{
                        this.npcs.remove(npc);
                    }));
                }
                else {
                    npc.onEnterRoom(this.isolate);
                }
            }
        }
    }

    public AbstractIsolate getRandomIsolate(){
        return this.isolateList.get(AbstractDungeon.eventRng.random(1, this.isolateList.size() - 1));
    }

    public ArrayList<AbstractIsolate> getIsolates(){
        return this.isolateList;
    }

    public ArrayList<AbstractIsolate> getRandomIsolates(){
        return this.randomList;
    }

    private void initRandomIsolate(){
        this.isolateList.add(new MainRoom(this));
        ArrayList<Integer> codes = new ArrayList<>();
        for (int i = 1; i <= 17; i ++){
            codes.add(i);
        }
        while (this.isolateList.size() < 6){
            Integer i = codes.get(MathUtils.random(codes.size() - 1));
            codes.remove(i);
            addIsolate(i);
        }

        ArrayList<AbstractIsolate> tmp = new ArrayList<>(this.isolateList);
        while (tmp.size() > 0){
            AbstractIsolate ai = tmp.get(AbstractDungeon.shuffleRng.random(tmp.size() - 1));
            this.randomList.add(ai);
            tmp.remove(ai);
        }
    }

    private void addIsolate(int code){
        switch (code){
            case 1:
                this.isolateList.add(new PromiseAndFaith(this));
                break;
            case 2:
                this.isolateList.add(new ExpressTrainToHell(this));
                break;
            case 3:
                this.isolateList.add(new ArmorCreature(this));
                break;
            case 4:
                this.isolateList.add(new Bunny(this));
                break;
            case 5:
                this.isolateList.add(new YouMusHappy(this));
                break;
            case 6:
                this.isolateList.add(new NakedNestEgg(this));
                break;
            case 7:
                this.isolateList.add(new BurrowingHeaven(this));
                break;
            case 8:
                this.isolateList.add(new Schadenfreude(this));
                break;
            case 9:
                this.isolateList.add(new LaLuna_e(this));
                break;
            case 10:
                this.isolateList.add(new BackwardClock_e(this));
                break;
            case 11:
                this.isolateList.add(new DontTouchMe(this));
                break;
            case 12:
                this.isolateList.add(new FieryBird_e(this));
                break;
            case 13:
                this.isolateList.add(new Freischutz_e(this));
                break;
            case 14:
                this.isolateList.add(new HeroicMonk_e(this));
                break;
            case 15:
                this.isolateList.add(new CherryBlossoms_e(this));
                break;
            case 16:
                this.isolateList.add(new VoidDream_e(this));
                break;
            case 17:
                this.isolateList.add(new Laetitia_e(this));
                break;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (AbstractActionNpc npc : this.npcs){
            npc.dispose();
        }
    }
}
