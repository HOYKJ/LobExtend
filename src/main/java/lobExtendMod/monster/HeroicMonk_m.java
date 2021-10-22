package lobExtendMod.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import lobExtendMod.relic.Amita_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.DelayAction;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;

/**
 * @author hoykj
 */
public class HeroicMonk_m extends AbstractFriendlyMonster {
    public static final String ID = "HeroicMonk_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("HeroicMonk_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private int turns;
    private boolean left, run, heal;
    private DelayAction action;

    public HeroicMonk_m(float x, float y) {
        super(NAME, "HeroicMonk_m", 120, 0.0F, 0.0F, 260.0F, 240.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/HeroicMonk/templer.atlas", "lobExtendMod/images/monsters/HeroicMonk/templer.json", 2.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "0_Default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("0_Default", "1_Attack_1", 0.1F);
        this.stateData.setMix("0_Default", "1_Attack_2", 0.1F);
        this.stateData.setMix("0_Default", "1_default_to_casting", 0.1F);
        this.stateData.setMix("2_Casting_loop", "3_howl", 0.1F);
        this.stateData.setMix("4_running", "5_running_to_eating_agent", 0.1F);
        this.stateData.setMix("4_running", "5_running_to_groggy", 0.1F);
        this.stateData.setMix("0_Default", "0_Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 18));
        this.damage.add(new DamageInfo(this, 60));
        this.run = false;
        this.heal = false;
        this.drawX = x;
    }

    protected void getMove(int num) {
        switch (this.turns){
            case 0: case 1:
                setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
                this.randomTarget();
                break;
            case 2:
                setMove((byte) 2, Intent.UNKNOWN);
                this.randomTarget();
                break;
            case 3:
                setMove((byte) 3, Intent.ATTACK, this.damage.get(1).base);
                break;
            case 4:
                setMove((byte) 4, Intent.UNKNOWN);
                break;
        }
        this.turns ++;
        if (this.turns > 4){
            this.turns = 0;
        }
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                if (MathUtils.randomBoolean()){
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
                }
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }, 0.9F));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST"));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "RUN"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    this.run = true;
                }, 3.83F));
                this.action = new DelayAction();
                AbstractDungeon.actionManager.addToBottom(this.action);
                break;
            case 4:
                if (this.heal) {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "EAT_END"));
                    AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, 30));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "MISS_END"));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped() || this.target == this) {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.randomTarget();
            }
        }
        super.update();
        if (!this.isDying && this.state.getTracks().get(0).getAnimation().getName().equals("0_Default")){
            this.flipHorizontal = this.target.drawX > this.drawX;
            this.left = !this.flipHorizontal;
        }
        else {
            this.flipHorizontal = !this.left;
        }
        if (this.run){
            float speed = 600;
            if (this.left){
                speed = -600;
            }
            this.drawX += Gdx.graphics.getDeltaTime() * speed;
            if (this.left){
                if (this.drawX < this.target.drawX - 400){
                    this.run = false;
                    this.action.end();
                    if (this.target.currentBlock >= this.damage.get(1).base){
                        this.heal = false;
                        this.changeState("MISS");
                    }
                    else {
                        this.heal = true;
                        this.changeState("EAT");
                    }
                    this.target.damage(this.damage.get(1));
                }
            }
            else {
                if (this.drawX > this.target.drawX + 400){
                    this.run = false;
                    this.action.end();
                    if (this.target.currentBlock >= this.damage.get(1).base){
                        this.heal = false;
                        this.changeState("MISS");
                    }
                    else {
                        this.heal = true;
                        this.changeState("EAT");
                    }
                    this.target.damage(this.damage.get(1));
                }
            }
        }
    }

    public void die() {
        super.die();
        this.changeState("DIE");
        this.deathTimer += 4.0F;
        if (!AbstractDungeon.player.hasRelic(Amita_r.ID)) {
            AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, Amita_r.ID));
        }
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "1_Attack_1", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "1_Attack_2", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default", true, 0.0F);
                break;
            case "CAST":
                this.state.setAnimation(0, "1_default_to_casting", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "2_Casting_loop", true, 0.0F);
                break;
            case "RUN":
                this.state.setAnimation(0, "3_howl", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "4_running", true, 0.0F);
                break;
            case "EAT":
                this.state.setAnimation(0, "5_running_to_eating_agent", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "6_eating_agent", true, 0.0F);
                break;
            case "MISS":
                this.state.setAnimation(0, "5_running_to_groggy", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "6_groggy", true, 0.0F);
                break;
            case "EAT_END":
                this.state.setAnimation(0, "7_eating_agent_to_default", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default", true, 0.0F);
                break;
            case "MISS_END":
                this.state.setAnimation(0, "7_groggy_to_default", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "0_Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
