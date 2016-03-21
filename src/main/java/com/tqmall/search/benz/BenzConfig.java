package com.tqmall.search.benz;

import com.tqmall.search.commons.analyzer.CjkLexiconSupplier;
import com.tqmall.search.commons.trie.RootNodeType;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * Created by xing on 16/3/21.
 * benz 配置
 *
 * @author xing
 */
public class BenzConfig {

    private static final Logger log = LoggerFactory.getLogger(BenzConfig.class);

    /**
     * 当前插件benz的config路径
     * 通过{@link Class#getProtectionDomain()}获取
     */
    static final String BENZ_CONFIG_DIR_NAME = "config";
    /**
     * 配置文件[确认能够正确打开, 可读, yml格式文件], 在es主配置文件[elasticsearch.yml]中指定
     * 不配置则默认读取{@link #BENZ_CONFIG_DIR_NAME} + {@link #DEFAULT_CONFIG_FILE_NAME}
     * 配置值为es/elasticsearch/es.yml/elasticsearch.yml 中的任意一个, 则从es主配置文件[elasticsearch.yml]中读取
     */
    static final String CONFIG_FILE_PATH_KEY = "benz.lexicon.file";

    /**
     * 默认的词库文件名, 没有配置{@link #LEXICON_FILE_PATH_KEY} 则从{@link #BENZ_CONFIG_DIR_NAME}文件下获取该文件
     */
    static final String DEFAULT_CONFIG_FILE_NAME = "benz_config.yml";

    /**
     * 词库文件路径配置key, 可以为文件夹
     * 如果配置项值为一个文件夹, 则加载该文件夹下面, 以"words"为前缀, 并且".dic"为后缀的所有词库文件
     */
    static final String LEXICON_FILE_PATH_KEY = "benz.lexicon.file";

    /**
     * 默认的词库文件名, 没有配置{@link #LEXICON_FILE_PATH_KEY} 则从{@link #BENZ_CONFIG_DIR_NAME}文件下获取该文件
     */
    static final String DEFAULT_LEXICON_FILE_NAME = "words_default.dic";

    /**
     * 词库文件是否只包含中文cjk字符, 默认true
     */
    static final String LEXICON_ONLY_CJK_KEY = "benz.lexicon.only_cjk";

    private final CjkLexiconSupplier cjkLexicon;

    @Inject
    public BenzConfig(Settings settings) {
        String value = settings.get(CONFIG_FILE_PATH_KEY);
        final Path benzConfigDirPath = Paths.get(new File(BenzConfig.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath()).getAbsolutePath(), BENZ_CONFIG_DIR_NAME);
        final Settings configSettings;
        if ("es".equalsIgnoreCase(value) || "es.yml".equalsIgnoreCase(value)
                || "elasticsearch".equalsIgnoreCase(value) || "elasticsearch.yml".equalsIgnoreCase(value)) {
            configSettings = settings;
        } else {
            Path configPath = value == null ? benzConfigDirPath.resolve(DEFAULT_CONFIG_FILE_NAME)
                    : Paths.get(value);
            configSettings = Settings.builder()
                    .loadFromPath(configPath)
                    .build();
        }
        value = configSettings.get(LEXICON_FILE_PATH_KEY);
        this.cjkLexicon = loadLexicon(configSettings, value == null ? benzConfigDirPath.resolve(DEFAULT_LEXICON_FILE_NAME)
                : Paths.get(value));
    }

    /**
     * 加载词库文件
     */
    private CjkLexiconSupplier loadLexicon(Settings settings, Path lexiconPath) {
        Path[] lexiconPaths;
        if (Files.isDirectory(lexiconPath, LinkOption.NOFOLLOW_LINKS)) {
            try {
                lexiconPaths = FileSystemUtils.files(lexiconPath, new DirectoryStream.Filter<Path>() {
                    @Override
                    public boolean accept(Path p) throws IOException {
                        return p.startsWith("words") && p.endsWith(".dic");
                    }
                });
            } catch (IOException e) {
                log.error("load lexicon path: " + lexiconPath + " directory files have exception", e);
                throw new SettingsException("load lexicon path: " + lexiconPath + " directory files have exception", e);
            }
        } else {
            lexiconPaths = new Path[]{lexiconPath};
        }
        RootNodeType cjkRootNodeType = settings.getAsBoolean(LEXICON_ONLY_CJK_KEY, true) ? RootNodeType.CJK
                : RootNodeType.CJK;
        return CjkLexiconSupplier.valueOf(cjkRootNodeType, lexiconPaths);
    }

    public CjkLexiconSupplier getCjkLexicon() {
        return cjkLexicon;
    }
}
