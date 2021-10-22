package lobExtendMod.helper;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;

/**
 * @author hoykj
 */
public class LobExtendImageMaster {
    //public static TextureAtlas.AtlasRegion KNIGHT_SWORD;
    public static Texture[] BUTTERFLY_ATTACK = new Texture[3];
    public static Texture[] BUTTERFLY_CAST = new Texture[3];
    public static Texture MERCENARY_AXE;
    public static Texture MERCENARY_EYE;
    public static Texture WANTED_CURSOR;
    public static Texture SHELTER_BG;
    public static Texture BIGBIRD_MARK;
    public static Texture QUESTION_BUTTON;
    public static Texture INFO_BG;

    public static Texture HIDDEN;
    public static Texture PROMISE_AND_FAITH;
    public static Texture EXPRESS_TRAIN_TO_HELL;
    public static Texture ARMOR_CREATURE;
    public static Texture BUNNY;
    public static Texture YOU_MUS_HAPPY;
    public static Texture NAKED_NEST;
    public static Texture BURROWING_HEAVEN;
    public static Texture SCHADENFREUDE;
    public static Texture LA_LUNA;
    public static Texture BACKWARD_CLOCK;
    public static Texture DONT_TOUCH_ME;
    public static Texture FIERY_BIRD;
    public static Texture FREISCHUTZ;
    public static Texture HEROIC_MONK;
    public static Texture CHERRY_BLOSSOMS;
    public static Texture VOID_DREAM;
    public static Texture LAETITIA;

    public static Texture[] DONT_TOUCH_DEAD = new Texture[85];
    public static Texture[] DONT_TOUCH_DOWN = new Texture[101];

    public static void initialize(){
//        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("lobExtendMod/images/monsters/DespairKnight/magicalGirl3.atlas"));
//        KNIGHT_SWORD = atlas.findRegion("sword");

        BUTTERFLY_ATTACK[0] = ImageMaster.loadImage("lobExtendMod/images/monsters/DeadButterflies/ButterflyAttackEffect/0.png");
        BUTTERFLY_ATTACK[1] = ImageMaster.loadImage("lobExtendMod/images/monsters/DeadButterflies/ButterflyAttackEffect/1.png");
        BUTTERFLY_ATTACK[2] = ImageMaster.loadImage("lobExtendMod/images/monsters/DeadButterflies/ButterflyAttackEffect/2.png");
        BUTTERFLY_CAST[0] = ImageMaster.loadImage("lobExtendMod/images/monsters/DeadButterflies/ButterflyEffect/0.png");
        BUTTERFLY_CAST[1] = ImageMaster.loadImage("lobExtendMod/images/monsters/DeadButterflies/ButterflyEffect/1.png");
        BUTTERFLY_CAST[2] = ImageMaster.loadImage("lobExtendMod/images/monsters/DeadButterflies/ButterflyEffect/2.png");
        MERCENARY_AXE = ImageMaster.loadImage("lobExtendMod/images/monsters/Mercenary/axe.png");
        MERCENARY_EYE = ImageMaster.loadImage("lobExtendMod/images/monsters/Mercenary/eye_hunt_noeye.png");
        WANTED_CURSOR = ImageMaster.loadImage("lobExtendMod/images/monsters/Mercenary/Cursor_Wanted.png");
        SHELTER_BG = ImageMaster.loadImage("lobExtendMod/images/events/Shelter.png");
        BIGBIRD_MARK = ImageMaster.loadImage("lobExtendMod/images/monsters/BigBird/light.png");
        QUESTION_BUTTON = ImageMaster.loadImage("lobExtendMod/images/ui/topPanel/question_button.png");
        INFO_BG = ImageMaster.loadImage("lobExtendMod/images/events/InfoBg.png");

        HIDDEN = ImageMaster.loadImage("lobExtendMod/images/events/isolates/NoData.png");
        PROMISE_AND_FAITH = ImageMaster.loadImage("lobExtendMod/images/events/isolates/PromiseAndFaith.png");
        EXPRESS_TRAIN_TO_HELL = ImageMaster.loadImage("lobExtendMod/images/events/isolates/ExpressTraintoHellPortrait.png");
        ARMOR_CREATURE = ImageMaster.loadImage("lobExtendMod/images/events/isolates/ArmorCreature.png");
        BUNNY = ImageMaster.loadImage("lobExtendMod/images/events/isolates/Bunny.png");
        YOU_MUS_HAPPY = ImageMaster.loadImage("lobExtendMod/images/events/isolates/YouMusHappy.png");
        NAKED_NEST = ImageMaster.loadImage("lobExtendMod/images/events/isolates/NakedNest.png");
        BURROWING_HEAVEN = ImageMaster.loadImage("lobExtendMod/images/events/isolates/BurrowingHeaven.png");
        SCHADENFREUDE = ImageMaster.loadImage("lobExtendMod/images/events/isolates/Schadenfreude.png");
        LA_LUNA = ImageMaster.loadImage("lobExtendMod/images/events/isolates/LaLuna.png");
        BACKWARD_CLOCK = ImageMaster.loadImage("lobExtendMod/images/events/isolates/BackwardClock.png");
        DONT_TOUCH_ME = ImageMaster.loadImage("lobExtendMod/images/events/isolates/DontTouchMe.png");
        FIERY_BIRD = ImageMaster.loadImage("lobExtendMod/images/events/isolates/FieryBird.png");
        FREISCHUTZ = ImageMaster.loadImage("lobExtendMod/images/events/isolates/Freischutz.png");
        HEROIC_MONK = ImageMaster.loadImage("lobExtendMod/images/events/isolates/HeroicMonk.png");
        CHERRY_BLOSSOMS = ImageMaster.loadImage("lobExtendMod/images/events/isolates/CherryBlossoms.png");
        VOID_DREAM = ImageMaster.loadImage("lobExtendMod/images/events/isolates/VoidDream.png");
        LAETITIA = ImageMaster.loadImage("lobExtendMod/images/events/isolates/Laetitia.png");

        for (int i = 0; i < 85; i ++){
            DONT_TOUCH_DEAD[i] = ImageMaster.loadImage("lobExtendMod/images/texture/DontTouch/Dead_" + i + ".png");
        }
        for (int i = 1; i < 102; i ++){
            DONT_TOUCH_DOWN[i - 1] = ImageMaster.loadImage("lobExtendMod/images/texture/DontTouch/Down_" + i + ".png");
        }
    }
}
