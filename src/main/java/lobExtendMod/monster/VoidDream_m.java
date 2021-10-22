package lobExtendMod.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.power.DeepSleepMonster;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.power.DeepSleep;

/**
 * @author hoykj
 */
public class VoidDream_m extends AbstractFriendlyMonster {
    public static final String ID = "VoidDream_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("VoidDream_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean action, changed, right;
    private int run;

    public VoidDream_m(float x, float y) {
        super(NAME, "VoidDream_m", 25, 0.0F, 0.0F, 140.0F, 240.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/VoidDream/skeleton.atlas", "lobExtendMod/images/monsters/VoidDream/skeleton.json", 2.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "1_walk", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.damage.add(new DamageInfo(this, 4));
        this.drawX = x;
        this.action = false;
        this.changed = false;
        this.right = MathUtils.randomBoolean();
        this.run = 6;
    }

    protected void getMove(int num) {
        if (this.changed){
            setMove((byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        }
        else {
            this.run --;
            if (this.run > 0) {
                setMove((byte) 1, Intent.UNKNOWN);
            }
            else {
                setMove((byte) 3, Intent.ESCAPE);
            }
        }
        this.randomTarget();
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                if (this.action) {
                    if (this.target == AbstractDungeon.player) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DeepSleep(AbstractDungeon.player, 1), 1));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, this, new DeepSleepMonster(this.target)));
                    }
                    this.action = false;
                }
                else {
                    this.action = true;
                }
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                        CardCrawlGame.sound.play("Dreamy_Shout");
                    }));
                    AbstractDungeon.actionManager.addToBottom(new DamageAllAction(this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                    if (AbstractDungeon.player.hasPower(DeepSleep.POWER_ID)){
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, this, DeepSleep.POWER_ID));
                        AbstractDungeon.player.currentHealth /= 2;
                    }
                    this.weak(AbstractDungeon.player);
                    for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters){
                        if (!monster.isDeadOrEscaped()){
                            if (monster.hasPower(DeepSleep.POWER_ID)) {
                                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(monster, this, DeepSleep.POWER_ID));
                                monster.currentHealth /= 2;
                            }
                            this.weak(monster);
                        }
                    }
                }, 1.4F));
                break;
            case 3:
                this.isEscaping = true;
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void weak(AbstractCreature creature){
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(creature, this, new FrailPower(creature, 1, true), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(creature, this, new WeakPower(creature, 1, true), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(creature, this, new VulnerablePower(creature, 1, true), 1));
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped() || this.target == this) {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.randomTarget();
            }
        }
        super.update();
        if (!this.changed){
            if (this.isEscaping){
                if(this.right){
                    this.drawX += Gdx.graphics.getDeltaTime() * 200.0F * Settings.scale;
                    if (this.drawX > Settings.WIDTH){
                        this.escaped = true;
                        if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
                            AbstractDungeon.getCurrRoom().endBattle();
                        }
                    }
                }
                else {
                    this.drawX -= Gdx.graphics.getDeltaTime() * 200.0F * Settings.scale;
                    if (this.drawX < 0){
                        this.escaped = true;
                        if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
                            AbstractDungeon.getCurrRoom().endBattle();
                        }
                    }
                }
            }
            else {
                float moveSpeed = 100.0F;
                if(this.right){
                    this.drawX += Gdx.graphics.getDeltaTime() * moveSpeed;
                    if(this.drawX + this.hb.width / 2 > Settings.WIDTH - 100){
                        this.right = false;
                    }
                }
                else {
                    this.drawX -= Gdx.graphics.getDeltaTime() * moveSpeed;
                    if(this.drawX - this.hb.width / 2 < 100){
                        this.right = true;
                    }
                }
                this.flipHorizontal = !this.right;
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0){
            this.transform();
            this.currentHealth = this.maxHealth;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePlayerPower(this, 99), 99));
            this.rollMove();
            this.createIntent();
        }
    }

    public void die() {
        if (this.changed) {
            super.die();
            this.deathTimer += 3.3F;
            this.changeState("DIE");
        }
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK":
                this.state.setAnimation(0, "2_default_to-crying", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "3_crying_loop", false, 0.0F);
                this.state.addAnimation(0, "4_crying_to_default", false, 0.0F);
                this.state.addAnimation(0, "1_default", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "2_dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }

    private void transform(){
        this.changed = true;
        loadAnimation("lobExtendMod/images/monsters/VoidDream/escape/skeleton.atlas", "lobExtendMod/images/monsters/VoidDream/escape/skeleton.json", 2.0F);
        this.state.setAnimation(0, "0_Transform", false);
        this.state.addAnimation(0, "1_default", true, 0.0F);
        this.stateData.setMix("1_default", "2_default_to-crying", 0.1F);
        this.stateData.setMix("1_default", "2_dead", 0.1F);
    }
}
