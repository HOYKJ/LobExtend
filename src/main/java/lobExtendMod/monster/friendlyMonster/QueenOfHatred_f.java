package lobExtendMod.monster.friendlyMonster;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.action.HatredLaserAction;
import lobExtendMod.monster.QueenOfHatred_m;
import lobExtendMod.npc.QueenOfHatred_npc;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.monster.Ordeal.AbstractOrdealMonster;
import lobotomyMod.monster.Ordeal.Machine.MachineNight;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.relic.CogitoBucket;
import lobotomyMod.vfx.ordeal.OrdealTitleBack;

/**
 * @author hoykj
 */
public class QueenOfHatred_f extends AbstractFriendlyMonster {
    public static final String ID = "QueenOfHatred_f";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("QueenOfHatred_f");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private int turns, QC, p_hp;
    private boolean panic;
    private AnimationState.TrackEntry entry;
    private QueenOfHatred_npc npc;

    public QueenOfHatred_f(float x, float y, QueenOfHatred_npc npc) {
        super(NAME, "QueenOfHatred_f", 1, 0.0F, 0.0F, 200.0F, 240.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/QueenOfHatred/normal/Magic_circle.atlas", "lobExtendMod/images/monsters/QueenOfHatred/normal/Magic_circle.json", 2.4F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "0_Default_escape_too", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("0_Default_escape_too", "1_Normal_Attack", 0.1F);
        this.stateData.setMix("0_Default_escape_too", "1_Normal_Attack_2", 0.1F);
        this.stateData.setMix("0_Default_escape_too", "1_Attack", 0.1F);
        this.stateData.setMix("0_Default_escape_too", "1_Casting", 0.1F);
        this.stateData.setMix("2_Casting_attack_loop", "3_Casting_attack_to_Delay", 0.1F);
        this.stateData.setMix("4_Delay_loop", "4_Delay_to_Default", 0.1F);
        this.stateData.setMix("0_Default_escape_too", "0_Dead", 0.1F);
        this.stateData.setMix("0_Default_escape_too", "1_Default_to_Panic_Default", 0.1F);
        this.stateData.setMix("2_Panic_Default", "4_transform", 0.1F);
        this.damage.add(new DamageInfo(this, 8));
        this.damage.add(new DamageInfo(this, 24));
        this.damage.add(new DamageInfo(this, 2));
        this.turns = 1;
        this.drawX = AbstractDungeon.player.drawX - 240.0F * Settings.scale;
        this.drawY = AbstractDungeon.player.drawY;
        this.canBeTarget = false;
        this.panic = false;
        //this.halfDead = true;
        this.hideHealthBar();
        this.npc = npc;
        this.friend = true;
    }

    protected void getMove(int num) {
        if(this.panic){
            setMove((byte) 99, Intent.UNKNOWN);
            return;
        }
        switch (this.turns){
            case 1: case 2: case 4: case 5:
                setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
                break;
            case 3:
                setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base);
                break;
            case 6:
                setMove((byte) 3, Intent.ATTACK_BUFF, this.damage.get(2).base, 12, true);
                break;
            case 7:
                setMove((byte) 4, Intent.UNKNOWN);
                break;
        }
        this.turns ++;
        if(this.turns > 7){
            this.turns = 1;
        }
        createIntent();
        //setMove((byte) 3, Intent.ATTACK_BUFF, this.damage.get(2).base);
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                if(this.turns == 2 || this.turns == 5) {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
                }
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(target, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }, 0.83F));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK3"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(target, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }, 3.77F));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToBottom(new HatredLaserAction(this.skeleton, this.state, this.damage.get(2)));
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST_END"));
                }));
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "NORMAL"));
                break;
            case 99:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "TRANSFORM"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    super.die();
                    AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new QueenOfHatred_m(-240, 20), false));
                }, 7.7F));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        //this.halfDead = true;
        //this.hideHealthBar();
        if(!this.panic) {
            if (this.p_hp == 0) {
                this.p_hp = AbstractDungeon.player.currentHealth;
            }
            if (this.p_hp > AbstractDungeon.player.currentHealth) {
                this.QC += this.p_hp - AbstractDungeon.player.currentHealth;
                this.p_hp = AbstractDungeon.player.currentHealth;
            }
        }

        if (this.isDying || this.state == null || this.state.getTracks().get(0) == null || this.state.getTracks().get(0).getAnimation() == null || this.state.getTracks().get(0).getAnimation().getName() == null){
            return;
        }

        if (this.state.getTracks().get(0).getAnimation().getName().equals("0_Default_escape_too")){
            this.skeleton.findSlot("eyebrow/stregth").setAttachment(null);
            this.skeleton.findSlot("eyebrow/sad").setAttachment(null);
        }

        if(this.state.getTracks().get(0).getAnimation().getName().equals("0_Default_escape_too") || this.state.getTracks().get(0).getAnimation().getName().equals("4_Delay_loop")) {
            if(!this.panic) {
                if (this.QC >= AbstractDungeon.player.maxHealth * 0.4F) {
                    this.panic = true;
                    this.skeleton.findSlot("mouth/smile").setAttachment(null);
                    this.changeState("PANIC");
                    setMove((byte) 99, Intent.UNKNOWN);
                    this.createIntent();
                }
            }
            boolean flag = true;
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m != this && !m.isDeadOrEscaped()) {
                    flag = false;
                }
            }
            if (flag) {
                this.halfDead = false;
                this.die();
            }
            else {
                if(this.target == null || this.target.isDeadOrEscaped() || this.target == this){
                    if(!AbstractDungeon.getMonsters().areMonstersBasicallyDead()){
                        this.randomTarget();
                        //this.target = AbstractDungeon.getMonsters().getRandomMonster(this, true);
                    }
                }
            }
        }

        if (this.intent == Intent.DEBUG){
            this.createIntent();
        }
    }

    @Override
    public void damage(DamageInfo info) {
    }

    public void die() {
        super.die();
        this.skeleton.findSlot("mouth/a").setAttachment(null);
        this.changeState("DIE");
        this.deathTimer += 7.3F;
        this.npc.needRemove = false;
        this.npc.init();
        CogitoBucket.npcs.add(this.npc);
    }

    @Override
    public void showHealthBar() {
        //super.showHealthBar();
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ATTACK1":
                this.state.setAnimation(0, "1_Normal_Attack", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default_escape_too", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "1_Normal_Attack_2", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default_escape_too", true, 0.0F);
                break;
            case "ATTACK3":
                this.state.setAnimation(0, "1_Attack", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default_escape_too", true, 0.0F);
                break;
            case "CAST":
                this.state.setAnimation(0, "1_Casting", false);
                this.state.setTimeScale(1F);
                this.entry = this.state.addAnimation(0, "2_Casting_attack_loop", true, 0.0F);
                break;
            case "CAST_END":
                this.state.addAnimation(0, "3_Casting_attack_to_Delay", false, 0.0F);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "4_Delay_loop", true, 0.0F);
                break;
            case "NORMAL":
                this.state.setAnimation(0, "4_Delay_to_Default", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "0_Default_escape_too", true, 0.0F);
                break;
            case "PANIC":
                this.state.setAnimation(0, "1_Default_to_Panic_Default", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "2_Panic_Default", true, 0.0F);
                break;
            case "TRANSFORM":
                this.state.setAnimation(0, "4_transform", false);
                this.state.setTimeScale(1F);
                break;
            case "DIE":
                this.state.setAnimation(0, "0_Dead", false);
                this.state.setTimeScale(1F);
                break;
            case "RESET":
                this.state.setAnimation(0, "0_Default_4_special", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
