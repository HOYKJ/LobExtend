package lobExtendMod.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.event.isolate.FieryBird_e;
import lobExtendMod.relic.Heaven_r;
import lobExtendMod.relic.Justitia_r;
import lobExtendMod.vfx.FieryFlameEffect;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.LongBird;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.relic.HonorFeather;

/**
 * @author hoykj
 */
public class FieryBird_m extends AbstractMonster {
    public static final String ID = "FieryBird_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("FieryBird_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private FieryBird_e event;
    private int remain;
    private boolean attack, fly, stop;
    private AbstractGameEffect effect;

    public FieryBird_m(float x, float y, FieryBird_e event) {
        super(NAME, "FieryBird_m", 150, 0.0F, 0.0F, 300.0F, 600.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/FieryBird/escape/skeleton.atlas", "lobExtendMod/images/monsters/FieryBird/escape/skeleton.json", 1.2F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "0_Default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("0_Default", "1_attack_ready", 0.1F);
        this.stateData.setMix("0_Default", "1_move", 0.1F);
        this.stateData.setMix("0_Default", "4_dead", 0.1F);
        this.damage.add(new DamageInfo(this, 5));
        this.damage.add(new DamageInfo(this, 55));
        this.flipHorizontal = true;

        this.event = event;
        this.remain = AbstractDungeon.aiRng.random(5, 8);
        this.attack = false;
        this.fly = false;
        this.stop = false;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (this.isDeadOrEscaped()){
            return;
        }
        this.effect = new FieryFlameEffect();
        AbstractDungeon.effectList.add(this.effect);
    }

    protected void getMove(int num) {
        if(this.attack) {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(1).base);
        }
        else {
            setMove((byte) 0, Intent.ATTACK, this.damage.get(0).base);
        }
        if (this.remain <= 0){
            setMove((byte) 2, Intent.ESCAPE);
        }
    }

    public void takeTurn() {
        if(this.nextMove == 0){
            AbstractDungeon.actionManager.addToBottom(new DamageAllAction(this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
        }
        else if(this.nextMove == 1){
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                this.fly = true;
                this.stop = false;
            }, 2.7F));
        }
        else if(this.nextMove == 2){
            this.isEscaping = true;
        }
        this.remain --;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        if (this.fly){
            float speed = 3000;
            this.drawX -= Gdx.graphics.getDeltaTime() * speed;
            if (this.drawX <= 0){
                this.stop = true;
                this.drawX = Settings.WIDTH;
            }
            if (this.stop && this.drawX <= Settings.WIDTH * 0.75F){
                this.fly = false;
                this.changeState("STOP");
                AbstractDungeon.actionManager.addToBottom(new DamageAllAction(this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
            }
        }

        if (this.isEscaping){
            this.flipHorizontal = false;
            this.changeState("ESCAPE");
            this.drawX += Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
            if (this.drawX > Settings.WIDTH){
                this.escaped = true;
                if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
                    AbstractDungeon.getCurrRoom().endBattle();
                }
                this.effect.isDone = true;
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        this.attack = true;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        AbstractDungeon.actionManager.addToBottom(new LatterAction(this::createIntent));
    }

    public void die() {
        super.die();
        this.changeState("DIE");
        this.deathTimer += 1.5F;
        if (!AbstractDungeon.player.hasRelic(HonorFeather.ID)) {
            AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, HonorFeather.ID));
        }
        this.event.changeState("BACK");
        this.effect.isDone = true;
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK":
                this.state.setAnimation(0, "1_attack_ready", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "2_attack_loop", true, 0.0F);
                break;
            case "STOP":
                this.state.setAnimation(0, "3_attack_end", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default", true, 0.0F);
                break;
            case "ESCAPE":
                this.state.setAnimation(0, "1_move", true);
                this.state.setTimeScale(1F);
                break;
            case "DIE":
                this.state.setAnimation(0, "4_dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
