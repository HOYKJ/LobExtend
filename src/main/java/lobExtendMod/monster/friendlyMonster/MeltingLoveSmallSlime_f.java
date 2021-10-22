package lobExtendMod.monster.friendlyMonster;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;

/**
 * @author hoykj
 */
public class MeltingLoveSmallSlime_f extends AbstractFriendlyMonster {
    public static final String ID = "MeltingLoveSmallSlime_f";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("MeltingLoveSmallSlime");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    public MeltingLoveSmallSlime_f(float x, float y) {
        super(NAME, "MeltingLoveSmallSlime_f", 20, 0.0F, -4.0F, 180.0F, 120.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/MeltingLove/SmallSlime/skeleton.atlas", "lobExtendMod/images/monsters/MeltingLove/SmallSlime/skeleton.json", 2.4F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "0_default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("0_default", "1_attack", 0.1F);
        this.stateData.setMix("0_default", "1_attack_2", 0.1F);
        this.stateData.setMix("0_default", "0_dead", 0.1F);
        this.damage.add(new DamageInfo(this, 6));
        this.damage.add(new DamageInfo(this, 8));
        this.drawX = x;
        this.drawY = y;
        this.friend = true;
        this.identifier = "MeltingLove";
    }

    protected void getMove(int num) {
        if(num < 60F) {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
        else {
            setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base);
        }
        this.randomTarget();
    }

    public void takeTurn() {
        if(this.nextMove == 1){
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }, 1.0F));
        }
        else if(this.nextMove == 2){
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }, 1.0F));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        if(this.target == null || this.target.isDeadOrEscaped() || this.target == this){
            if(!AbstractDungeon.getMonsters().areMonstersBasicallyDead()){
                this.randomTarget();
            }
        }
        super.update();
        boolean flag = true;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m != this && !m.isDeadOrEscaped()) {
                flag = false;
            }
        }
        if (flag) {
            this.hideHealthBar();
            this.escaped = true;
            if (AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.getCurrRoom().isBattleOver && !AbstractDungeon.getCurrRoom().cannotLose) {
                AbstractDungeon.getCurrRoom().endBattle();
            }
        }
    }

    public void die() {
        super.die();
        this.deathTimer += 2.4F;
        this.changeState("DIE");
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "1_attack", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_default", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "1_attack_2", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_default", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "0_dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
