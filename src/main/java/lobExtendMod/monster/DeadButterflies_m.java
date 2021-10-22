package lobExtendMod.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import lobExtendMod.LobExtendMod;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.relic.CENSORED_r;
import lobExtendMod.relic.InTheNameOfLoveAndHate_r;
import lobExtendMod.relic.SolemnVow_r;
import lobExtendMod.vfx.ButterflyAttackEffect;
import lobExtendMod.vfx.ButterflyCastRange;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.Funeral;
import lobotomyMod.card.uncommonCard.QueenOfHatred;
import lobotomyMod.monster.Ordeal.AbstractOrdealMonster;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.vfx.action.LatterEffect;

/**
 * @author hoykj
 */
public class DeadButterflies_m extends AbstractFriendlyMonster {
    public static final String ID = "DeadButterflies_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("DeadButterflies_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private float delayTimer;
    private boolean walk, moveRight;

    public DeadButterflies_m(float x, float y) {
        super(NAME, "DeadButterflies_m", 40, 0.0F, -10.0F, 280.0F, 340.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/DeadButterflies/Butterfly.atlas", "lobExtendMod/images/monsters/DeadButterflies/Butterfly.json", 1.6F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("default", "walk", 0.1F);
        this.stateData.setMix("walk", "default", 0.1F);
        this.stateData.setMix("default", "attack_front", 0.1F);
        this.stateData.setMix("walk", "attack_side_1", 0.1F);
        this.stateData.setMix("walk", "attack_side_2", 0.1F);
        this.stateData.setMix("default", "speical", 0.1F);
        this.stateData.setMix("walk", "speical", 0.1F);
        this.stateData.setMix("speical2", "speical3", 0.1F);
        this.stateData.setMix("default", "dead", 0.1F);
        this.stateData.setMix("walk", "dead", 0.1F);
        this.damage.add(new DamageInfo(this, 10));
        this.damage.add(new DamageInfo(this, 3));
        this.delayTimer = 0;
        this.moveRight = true;
        this.walk = false;
    }

    protected void getMove(int num) {
        if(num < 70F) {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
        else {
            setMove((byte) 2, Intent.ATTACK, 3, 6, true);
        }
        this.randomTarget();
    }

    public void takeTurn() {
        if(this.nextMove == 1){
            if(this.walk){
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    this.walk = false;
                }));
                if(AbstractDungeon.monsterRng.randomBoolean()){
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
                    AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                        CardCrawlGame.sound.play("Butterfly_Attack");
                        AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                        this.attackEffect();
                    }, 3.1F));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK3"));
                    AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                        CardCrawlGame.sound.play("Butterfly_Attack");
                        AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                        this.attackEffect();
                    }, 2.0F));
                }
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    CardCrawlGame.sound.play("Butterfly_Attack");
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                    this.attackEffect();
                }, 1.6F));
            }
        }
        else if(this.nextMove == 2){
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST"));
            CardCrawlGame.sound.play("Butterfly_open");
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                this.walk = false;
                AbstractDungeon.effectsQueue.add(new ButterflyCastRange(this.state, this.skeleton,  this.target.drawX > this.drawX));
            }));
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                AbstractDungeon.actionManager.addToTop(new DamageAllAction(this, 3, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
            }, 2.17F));
            for(int i = 0; i < 5; i ++) {
                AbstractDungeon.actionManager.addToBottom(new LatterAction(() -> {
                    AbstractDungeon.actionManager.addToTop(new DamageAllAction(this, 3, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
                }, 1.34F));
            }
            AbstractDungeon.actionManager.addToBottom(new LatterAction(() -> {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST_END"));
            }, 0.67F));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        if(this.target == null || this.target.isDeadOrEscaped() || this.target == this){
            if(!AbstractDungeon.getMonsters().areMonstersBasicallyDead()){
                this.randomTarget();
                LobExtendMod.logger.info(this.target.id);
            }
        }
        super.update();
        float moveSpeed = 100.0F;
        if(!this.isDying && this.walk){
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
        if (!this.isDying && this.state.getTracks().get(0).getAnimation().getName().equals("default")){
            if(!this.walk){
                if(this.delayTimer > 0){
                    this.delayTimer -= Gdx.graphics.getDeltaTime();
                }
                else {
                    this.walk = true;
                    this.changeState("WALK");
                }
            }
            else{
                this.changeState("WALK");
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if(this.currentHealth > 0) {
            this.changeState("STAND");
            this.walk = false;
            this.delayTimer = 6;
        }
    }

    public void die() {
        super.die();
        this.deathTimer += 6.0F;
        this.changeState("DIE");
        CardCrawlGame.sound.play("Butterfly_Dead");
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, SolemnVow_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new Funeral());
    }

    private void attackEffect(){
        int count = MathUtils.random(7, 10);
        for (int i = 0; i < count; i ++){
            AbstractDungeon.effectsQueue.add(new ButterflyAttackEffect(this.target));
        }
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "attack_front", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "default", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "attack_side_1", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "default", true, 0.0F);
                break;
            case "ATTACK3":
                this.state.setAnimation(0, "attack_side_1", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "default", true, 0.0F);
                break;
            case "CAST":
                this.state.setAnimation(0, "speical", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "speical2", true, 0.0F);
                break;
            case "CAST_END":
                this.state.setAnimation(0, "speical3", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "default", true, 0.0F);
                break;
            case "WALK":
                this.state.setAnimation(0, "walk", true);
                this.state.setTimeScale(1F);
                break;
            case "STAND":
                this.state.setAnimation(0, "default", true);
                this.state.setTimeScale(1F);
                break;
            case "DIE":
                this.state.setAnimation(0, "dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
