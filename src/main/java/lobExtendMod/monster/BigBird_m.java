package lobExtendMod.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.action.BigBirdEatAction;
import lobExtendMod.power.BigBirdEnchantPower;
import lobExtendMod.relic.Lamp_r;
import lobExtendMod.relic.SoundOfStar_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobExtendMod.vfx.RenderMonsterEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.BigBird;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.vfx.LoseSightEffect;
import lobotomyMod.vfx.action.LatterEffect;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class BigBird_m extends AbstractFriendlyMonster {
    public static final String ID = "BigBird_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("BigBird_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean walk, moveRight, castSound, noChange;
    private float shortTimer;
    private LoseSightEffect effect;

    public BigBird_m(float x, float y) {
        super(NAME, "BigBird_m", 160, 0.0F, 0.0F, 300.0F, 300.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/BigBird/skeleton.atlas", "lobExtendMod/images/monsters/BigBird/skeleton.json", 1.6F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "1_Default_1_long", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("1_Default_1_long", "1_Default_2_short", 0.1F);
        this.stateData.setMix("1_Default_1_long", "1_Walk", 0.1F);
        this.stateData.setMix("1_Default_1_long", "1_Dead", 0.1F);
        this.stateData.setMix("1_Default_1_long", "2_Default_to_Casting", 0.1F);
        this.stateData.setMix("1_Default_1_long", "4_Default_to_Deadscene", 0.1F);
        this.stateData.setMix("3_Casting_loop", "4_Casting_to_Deadscene", 0.1F);
        this.stateData.setMix("1_Walk", "2_Default_to_Casting", 0.1F);
        this.stateData.setMix("1_Walk", "4_Walk_to_Deadscene", 0.1F);
        this.damage.add(new DamageInfo(this, 40));
        this.moveRight = true;
        this.walk = false;
        this.castSound = false;
        this.shortTimer = MathUtils.random(8, 24);
        this.effect = new LoseSightEffect();
        AbstractDungeon.effectsQueue.add(this.effect);
        AbstractDungeon.effectsQueue.add(new RenderMonsterEffect(this));
        this.noChange = false;
    }

    protected void getMove(int num) {
        if (!this.noChange) {
            setMove((byte) 1, Intent.UNKNOWN);
        }
    }

    public void takeTurn() {
        this.noChange = false;
        if(this.nextMove == 1){
            ArrayList<AbstractCreature> list = new ArrayList<>();
            if (!AbstractDungeon.player.hasPower(BigBirdEnchantPower.POWER_ID)){
                list.add(AbstractDungeon.player);
            }
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
                if (!monster.isDeadOrEscaped() && !monster.hasPower(BigBirdEnchantPower.POWER_ID) && monster != this){
                    list.add(monster);
                }
            }
            if (list.size() > 0){
                this.target = list.get(AbstractDungeon.aiRng.random(list.size() - 1));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST2"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this, new BigBirdEnchantPower(this.target, this)));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST1"));
            }
            this.castSound = true;
        }
        else if(this.nextMove == 2){
            float time = 3.8F;
            if (this.state.getTracks().get(0).getAnimation().getName().equals("3_Casting_loop")){
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST_ATTACK"));
                time += 0.3F;
            }
            else if (this.walk){
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
            }
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                AbstractDungeon.actionManager.addToBottom(new BigBirdEatAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.target, this, this.target.getPower(BigBirdEnchantPower.POWER_ID)));
            }, time));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        float moveSpeed = 100.0F;
        if(this.walk){
            if(this.moveRight){
                this.drawX += Gdx.graphics.getDeltaTime() * moveSpeed;
                if(this.drawX + this.hb.width / 2 > Settings.WIDTH - 100){
                    this.moveRight = false;
                }
            }
            else {
                this.drawX -= Gdx.graphics.getDeltaTime() * moveSpeed;
                if(this.drawX - this.hb.width / 2 < 100){
                    this.moveRight = true;
                }
            }
            this.flipHorizontal = !this.moveRight;
        }
        if (!this.isDying && this.state.getTracks().get(0).getAnimation().getName().equals("1_Default_1_long")){
            boolean flag = false;
            if (AbstractDungeon.player.hasPower(BigBirdEnchantPower.POWER_ID)){
                flag = true;
            }
            if (!flag) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped() && monster.hasPower(BigBirdEnchantPower.POWER_ID)) {
                        flag = true;
                        break;
                    }
                }
            }
            if(flag){
                this.walk = true;
                this.changeState("WALK");
            }
            else{
                if (this.shortTimer > 0){
                    this.shortTimer -= Gdx.graphics.getDeltaTime();
                }
                else {
                    this.shortTimer = MathUtils.random(8, 24);
                    this.changeState("SHORT");
                }
            }
        }
        if (!this.isDying && this.state.getTracks().get(0).getAnimation().getName().equals("1_Walk")){
            this.walk = true;
        }

        if (this.castSound){
            if (!this.isDying && this.state.getTracks().get(0).getAnimation().getName().equals("3_Casting_loop")){
                this.castSound = false;
                CardCrawlGame.sound.play("Bigbird_Attract");
            }
        }
    }

    public void eat(AbstractCreature target){
        this.target = target;
        this.noChange = true;
        setMove((byte)2, Intent.ATTACK, this.damage.get(0).base);
        createIntent();
        AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte)2, Intent.ATTACK, this.damage.get(0).base));
    }

    public void die() {
        super.die();
        this.deathTimer += 2.5F;
        this.changeState("DIE");
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, Lamp_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new BigBird());
        this.effect.end();
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "CAST1":
                this.state.setAnimation(0, "2_Default_to_Casting", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "3_Casting_loop", false, 0.0F);
                this.state.addAnimation(0, "4_Casting_to_Default", false, 0.0F);
                this.state.addAnimation(0, "1_Default_1_long", true, 0.0F);
                break;
            case "CAST2":
                this.state.setAnimation(0, "2_Default_to_Casting", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "3_Casting_loop", false, 0.0F);
                this.state.addAnimation(0, "4_Casting_to_Walk", false, 0.0F);
                AnimationState.TrackEntry entry = this.state.addAnimation(0, "1_Walk", true, 0.0F);
                entry.setListener(new AnimationState.AnimationStateAdapter() {
                    public void event(int trackIndex, Event event) {
                        if (event.getData().getName().equals("Sound")) {
                            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
                            CardCrawlGame.sound.play("Bigbird_Walk");
                        }
                    }
                });
                break;
            case "CAST_ATTACK":
                this.state.setAnimation(0, "4_Casting_to_Deadscene", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "5_Deadscene", false, 0.0F);
                this.state.addAnimation(0, "1_Default_1_long", true, 0.0F);
                break;
            case "ATTACK1":
                this.state.setAnimation(0, "4_Default_to_Deadscene", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "5_Deadscene", false, 0.0F);
                this.state.addAnimation(0, "1_Default_1_long", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "4_Walk_to_Deadscene", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "5_Deadscene", false, 0.0F);
                this.state.addAnimation(0, "1_Default_1_long", true, 0.0F);
                break;
            case "WALK":
                entry = this.state.setAnimation(0, "1_Walk", true);
                this.state.setTimeScale(1F);
                entry.setListener(new AnimationState.AnimationStateAdapter() {
                    public void event(int trackIndex, Event event) {
                        if (event.getData().getName().equals("Sound")) {
                            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
                            CardCrawlGame.sound.play("Bigbird_Walk");
                        }
                    }
                });
                break;
            case "STAND":
                this.state.setAnimation(0, "1_Default_1_long", true);
                this.state.setTimeScale(1F);
                break;
            case "SHORT":
                this.state.setAnimation(0, "1_Default_2_short", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "1_Default_1_long", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "1_Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
        this.walk = false;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }
}
