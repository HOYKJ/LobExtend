package lobExtendMod.monster.friendlyMonster;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.npc.BodiesMountain_npc_tmp;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.relic.CogitoBucket;

/**
 * @author hoykj
 */
public class WitchMonster_f extends AbstractFriendlyMonster {
    public static final String ID = "WitchMonster_f";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("WitchMonster_f");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean right;

    public WitchMonster_f(float x, float y) {
        super(NAME, "WitchMonster_f", 35, 0.0F, 0.0F, 500.0F, 240.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/Laetitia/monster/WitchMonster.atlas", "lobExtendMod/images/monsters/Laetitia/monster/WitchMonster.json", 1.2F);
        this.state.setAnimation(0, "Born", false);
        this.state.addAnimation(0, "Standing", true, 0.0F);
        this.stateData.setMix("Standing", "Attack_01", 0.1F);
        this.stateData.setMix("Standing", "Attack_02", 0.1F);
        this.stateData.setMix("Standing", "Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 10));
        this.drawX = x;
        this.friend = true;
    }

    protected void getMove(int num) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        this.randomTarget();
    }

    public void takeTurn() {
        if(this.nextMove == 1){
            if (this.right) {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
            }
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
            }, 0.8F));
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
        this.right = !this.flipHorizontal;
        this.flipHorizontal = false;

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
        this.changeState("DIE");
        this.deathTimer += 2.0F;
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "Attack_01", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Standing", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "Attack_02", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Standing", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
