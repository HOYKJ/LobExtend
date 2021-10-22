package lobExtendMod;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.RestartForChangesEffect;
import lobExtendMod.helper.LobExtendFontHelper;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.helper.LobExtendRecallHelper;
import lobExtendMod.helper.LobotomyExtendHandler;
import lobExtendMod.screen.ChooseRoomScreen;
import lobotomyMod.LobotomyMod;
import lobotomyMod.character.LobotomyHandler;
import lobotomyMod.helper.LobotomyFontHelper;
import lobotomyMod.helper.LobotomyImageMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author hoykj
 */
@SpireInitializer
public class LobExtendMod implements PostInitializeSubscriber, EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber {
    private static final String MODNAME = "Lobotomy Mod";
    private static final String AUTHOR = "hoykj";
    private static final String DESCRIPTION = "nothing";
    public static final Logger logger = LogManager.getLogger(LobExtendMod.class.getName());
    public static ChooseRoomScreen chooseRoomScreen;

    public static void initialize()  {
        logger.info("========================= 初始化Lobotomy Extend Mod所有数据 =========================");

        LobExtendMod lobExtendMod = new LobExtendMod();

        LobExtendRecallHelper.addRecallAbnormality();

        logger.info("=========================== 初始化Lobotomy Extend Mod成功 ===========================");
    }

    public LobExtendMod(){
        BaseMod.subscribe(this);
    }

    public void receiveEditCards() {
        logger.info("=========================正在加载新的卡牌内容=========================");

        LobotomyExtendHandler.addCards();

        logger.info("=========================加载新的卡牌内容成功=========================");
    }

    public void receiveEditRelics() {
        logger.info("=========================正在加载新的遗物内容=========================");

        LobotomyExtendHandler.addRelics();

        logger.info("=========================加载新的遗物内容成功=========================");
    }

    public void receiveEditStrings() {
        logger.info("========================= 正在加载文本信息 =========================");

        LobotomyExtendHandler.addStrings();

        logger.info("========================= 加载文本信息成功 =========================");
    }

    public void receiveEditKeywords() {
        logger.info("========================= 正在加载特性文本信息 =========================");

        LobotomyExtendHandler.addKeyWords();

        logger.info("========================= 加载特性文本信息成功 =========================");
    }

    public void receivePostInitialize() {
        logger.info("========================= receivePostInitialize =========================");

        try {
            this.CreatePanel();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            LobExtendFontHelper.initialize();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        LobExtendImageMaster.initialize();
        LobotomyExtendHandler.addEvents();

        logger.info("========================= receivePostInitializeDone =========================");
    }

    private void CreatePanel() throws IOException {
        final SpireConfig spireConfig = new SpireConfig("LobExtendMod", "Common");
        final ModPanel settingsPanel = new ModPanel();

        Texture badgeTexture = new Texture(Gdx.files.internal("lobExtendMod/images/Lobotomy.png"));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
    }
}
