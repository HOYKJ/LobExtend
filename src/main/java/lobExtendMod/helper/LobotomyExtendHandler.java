package lobExtendMod.helper;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import lobExtendMod.card.test_ex;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.event.Shelter;
import lobExtendMod.relic.*;
import lobotomyMod.helper.LobotomyUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author hoykj
 */
public class LobotomyExtendHandler {

    public static void addStrings(){
        if(Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
            String cardStrings = Gdx.files.internal("lobExtendMod/localization/zhs/card.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
            String powerStrings = Gdx.files.internal("lobExtendMod/localization/zhs/power.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
            String relicStrings = Gdx.files.internal("lobExtendMod/localization/zhs/relic.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
            String uiStrings = Gdx.files.internal("lobExtendMod/localization/zhs/UI.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
            String monsterStrings = Gdx.files.internal("lobExtendMod/localization/zhs/monster.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
            String eventStrings = Gdx.files.internal("lobExtendMod/localization/zhs/event.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
            String tutorialStrings = Gdx.files.internal("lobExtendMod/localization/zhs/tutorials.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(TutorialStrings.class, tutorialStrings);
        }
        else if(Settings.language == Settings.GameLanguage.JPN) {
            String cardStrings = Gdx.files.internal("lobExtendMod/localization/jpn/card.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
            String powerStrings = Gdx.files.internal("lobExtendMod/localization/jpn/power.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
            String relicStrings = Gdx.files.internal("lobExtendMod/localization/jpn/relic.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
            String uiStrings = Gdx.files.internal("lobExtendMod/localization/jpn/UI.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
            String monsterStrings = Gdx.files.internal("lobExtendMod/localization/jpn/monster.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
            String eventStrings = Gdx.files.internal("lobExtendMod/localization/jpn/event.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
            String tutorialStrings = Gdx.files.internal("lobExtendMod/localization/jpn/tutorials.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(TutorialStrings.class, tutorialStrings);
        }
        else if(Settings.language == Settings.GameLanguage.RUS) {
            String cardStrings = Gdx.files.internal("lobExtendMod/localization/rus/card.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
            String powerStrings = Gdx.files.internal("lobExtendMod/localization/rus/power.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
            String relicStrings = Gdx.files.internal("lobExtendMod/localization/rus/relic.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
            String uiStrings = Gdx.files.internal("lobExtendMod/localization/rus/UI.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
            String monsterStrings = Gdx.files.internal("lobExtendMod/localization/rus/monster.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
            String eventStrings = Gdx.files.internal("lobExtendMod/localization/rus/event.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
            String tutorialStrings = Gdx.files.internal("lobExtendMod/localization/rus/tutorials.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(TutorialStrings.class, tutorialStrings);
        }
        else {
            String cardStrings = Gdx.files.internal("lobExtendMod/localization/eng/card.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
            String powerStrings = Gdx.files.internal("lobExtendMod/localization/eng/power.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
            String relicStrings = Gdx.files.internal("lobExtendMod/localization/eng/relic.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
            String uiStrings = Gdx.files.internal("lobExtendMod/localization/eng/UI.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
            String monsterStrings = Gdx.files.internal("lobExtendMod/localization/eng/monster.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
            String eventStrings = Gdx.files.internal("lobExtendMod/localization/eng/event.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
            String tutorialStrings = Gdx.files.internal("lobExtendMod/localization/eng/tutorials.json").readString(String.valueOf(StandardCharsets.UTF_8));
            BaseMod.loadCustomStrings(TutorialStrings.class, tutorialStrings);
        }
    }

    class Keywords{
        Keyword[] Keyword;
    }

    public static void addKeyWords(){
        Gson gson = new Gson();
        Keywords keywords;
        if(Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
            keywords = gson.fromJson(Gdx.files.internal("lobExtendMod/localization/zhs/keyword.json").readString(String.valueOf(StandardCharsets.UTF_8)), Keywords.class);
        }
        else {
            keywords = gson.fromJson(Gdx.files.internal("lobExtendMod/localization/zhs/keyword.json").readString(String.valueOf(StandardCharsets.UTF_8)), Keywords.class);
        }

        if(keywords != null) {
            for (Keyword key : keywords.Keyword) {
                BaseMod.addKeyword(key.NAMES, key.DESCRIPTION);
            }
        }
    }

    public static void addCards(){
        BaseMod.addCard(new test_ex());
    }

    public static void addRelics(){
        LobotomyUtils.addEgoRelic(new InTheNameOfLoveAndHate_r());
        LobotomyUtils.addEgoRelic(new SolemnVow_r());
        LobotomyUtils.addEgoRelic(new Adoration_r());
        //LobotomyUtils.addAbnormalityRelic(new WantedRequest());
        LobotomyUtils.addEgoRelic(new CrimsonScar_r());
        LobotomyUtils.addEgoRelic(new CENSORED_r());
        LobotomyUtils.addEgoRelic(new TheSmile_r());
        LobotomyUtils.addEgoRelic(new SwordSharpenedByTears_r());
        LobotomyUtils.addEgoRelic(new Justitia_r());
        LobotomyUtils.addEgoRelic(new Lamp_r());
        LobotomyUtils.addEgoRelic(new SoundOfStar_r());
        LobotomyUtils.addEgoRelic(new Logging_r());
        LobotomyUtils.addEgoRelic(new ShedSkin_r());
        LobotomyUtils.addEgoRelic(new Heaven_r());
        LobotomyUtils.addEgoRelic(new Gaze_r());
        LobotomyUtils.addEgoRelic(new Moonlight_r());
        LobotomyUtils.addEgoRelic(new MagicBullet_r());
        LobotomyUtils.addEgoRelic(new Amita_r());
        LobotomyUtils.addEgoRelic(new LaetitiaHeart());

        LobotomyUtils.addEgoRelic(new InspiredRelic());

        RelicLibrary.add(new WantedRequest());
        RelicLibrary.add(new LetterFromLob());
        RelicLibrary.add(new RedMistMask());
    }

    public static void addEvents(){
        //BaseMod.addEvent(RoomOfDemon.ID, RoomOfDemon.class, Exordium.ID);
        BaseMod.addEvent(Shelter.ID, Shelter.class);
        BaseMod.addEvent(LobotomyEvent.ID, LobotomyEvent.class);
    }
}
