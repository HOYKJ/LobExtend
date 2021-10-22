package lobExtendMod.patches.music;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.Settings;

import java.util.HashMap;

@SpirePatch(cls="com.megacrit.cardcrawl.audio.SoundMaster", method="play", paramtypes={"java.lang.String", "boolean"})
public class SoundMasterplayPatch
{
    public static HashMap<String, Sfx> map = new HashMap<>();

    public static long Postfix(long res, SoundMaster _inst, String key, boolean useBgmVolume)
    {
        if (map.containsKey(key)) {
            if (useBgmVolume)
                return (map.get(key)).play(Settings.MUSIC_VOLUME * Settings.MASTER_VOLUME);

            return (map.get(key)).play(Settings.SOUND_VOLUME * Settings.MASTER_VOLUME);
        }
        return res;
    }

    private static Sfx load(String filename)
    {
        return new Sfx("audio/sound/" + filename, false);
    }

    static
    {
        map.put("Butterfly_Attack", load("Butterfly_Attack.ogg"));
        map.put("Butterfly_open", load("Butterfly_open.ogg"));
        map.put("Butterfly_Skill", load("Butterfly_Skill.ogg"));
        map.put("Butterfly_Close", load("Butterfly_Close.ogg"));
        map.put("Butterfly_Dead", load("Butterfly_Dead.ogg"));
        map.put("Danggo_Lv1_Atk1", load("Danggo_Lv1_Atk1.ogg"));
        map.put("Danggo_Lv2", load("Danggo_Lv2.ogg"));
        map.put("Danggo_Lv3_Atk", load("Danggo_Lv3_Atk.ogg"));
        map.put("Danggo_Lv3_Special", load("Danggo_Lv3_Special.ogg"));
        map.put("LongBird_Stun", load("LongBird_Stun.ogg"));
        map.put("Bigbird_Attract", load("Bigbird_Attract.ogg"));
        map.put("Bigbird_Dead1", load("Bigbird_Dead1.ogg"));
        map.put("Bigbird_Dead2", load("Bigbird_Dead2.ogg"));
        map.put("Bigbird_Walk", load("Bigbird_Walk.ogg"));
        map.put("touch_dead1", load("touch_dead1.ogg"));
        map.put("touch_dead2", load("touch_dead2.ogg"));
        map.put("touch_moodDown", load("touch_moodDown.ogg"));
        map.put("Dreamy_Shout", load("Dreamy_Shout.ogg"));
    }
}
