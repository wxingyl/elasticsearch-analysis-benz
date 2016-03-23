package com.tqmall.search.benz;

import com.tqmall.search.commons.analyzer.CjkAnalyzer;
import com.tqmall.search.commons.analyzer.CjkLexiconFactory;
import com.tqmall.search.commons.analyzer.MaxAsciiAnalyzer;
import com.tqmall.search.commons.analyzer.StopWords;
import com.tqmall.search.commons.nlp.SegmentConfig;
import com.tqmall.search.commons.trie.RootNodeType;
import com.tqmall.search.commons.utils.SearchStringUtils;
import org.apache.lucene.analysis.util.CharArraySet;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsException;
import org.elasticsearch.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xing on 16/3/21.
 * benz 配置
 *
 * @author xing
 */
public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    /**
     * 当前插件的名称
     */
    public static final String PLUGIN_NAME = "analysis-benz";

    /**
     * 分词器停止词TokenFilter name
     */
    public static final String STOP_FILTER_NAME = "benz_stop";

    /**
     * 配置文件[确认能够正确打开, 可读, yml格式文件], 在es主配置文件[elasticsearch.yml]中指定
     * 不配置则默认读取es config {@link #DEFAULT_CONFIG_FILE_NAME}
     * 配置值为es/elasticsearch/es.yml/elasticsearch.yml 中的任意一个, 则从es主配置文件[elasticsearch.yml]中读取
     * 推荐单独文件
     */
    static final String CONFIG_FILE_PATH_KEY = "benz.conf";

    /**
     * 词库文件路径配置key, 可以为文件夹
     * 如果配置项值为一个文件夹, 则加载该文件夹下面, 以"words"为前缀, 并且".dic"为后缀的所有词库文件
     */
    static final String LEXICON_FILE_PATH_KEY = "benz.lexicon";

    /**
     * 词库文件是否只包含中文cjk字符, 默认true
     */
    static final String LEXICON_ONLY_CJK_KEY = "benz.lexicon.only_cjk";

    /**
     * 默认的配置文件名, 没有配置{@link #CONFIG_FILE_PATH_KEY}, 则读取这个配置文件
     */
    static final String DEFAULT_CONFIG_FILE_NAME = "config.yml";

    /**
     * 默认的词库文件名, 没有配置{@link #LEXICON_FILE_PATH_KEY} 则读取Es提供的默认配置路径
     */
    static final String DEFAULT_LEXICON_FILE_NAME = "words_default.dic";

    /**
     * 分词器配置文件解析
     * 默认值遵循{@link SegmentConfig}中的默认值, 具体查看其文档
     *
     * @see SegmentConfig
     */
    static class Analyzer {
        /**
         * 分词器配置 key
         * 必须配置
         */
        static final String KEY = "benz.analyzer";
        /**
         * benz配置分词器名称前缀 key
         * 无默认值, 可以为空
         */
        static final String PREFIX_KEY = "benz.analyzer.name.prefix";

        /**
         * benz配置分词器名称 key, 唯一标识分词器
         *
         * @see SegmentConfig#name
         */
        static final String NAME = "name";
        /**
         * 处理ascii字符(阿拉伯数字和英文字母)是否使用{@link MaxAsciiAnalyzer}
         *
         * @see SegmentConfig#maxAsciiAnalyzer
         * @see MaxAsciiAnalyzer
         */
        static final String ASCII_MAX = "ascii_max";
        /**
         * @see SegmentConfig#asciiAnalyzerParseDecimal
         */
        static final String PARSE_DECIMAL = "parse_decimal";
        /**
         * @see SegmentConfig#asciiAnalyzerParseEnMix
         */
        static final String PARSE_EN_MIX = "parse_en_mix";
        /**
         * @see SegmentConfig#asciiAnalyzerAppendEnMix
         */
        static final String APPEND_EN_MIX = "append_en_mix";
        /**
         * @see SegmentConfig#cjkAnalyzerType
         */
        static final String CJK_ANALYZER = "cjk_analyzer";
        /**
         * @see SegmentConfig#mergeNumQuantifier
         */
        static final String MERGE_NUM_QUANTIFIER = "merge_num_quantifier";
        /**
         * @see SegmentConfig#appendNumQuantifier
         */
        static final String APPEND_NUM_QUANTIFIER = "append_num_quantifier";

        static SegmentConfig parse(String namePrefix, String keyPrefix, Settings settings) {
            SegmentConfig config = new SegmentConfig(namePrefix + settings.get(keyPrefix + NAME));
            if (settings.getAsBoolean(keyPrefix + ASCII_MAX, false)) {
                config.setMaxAsciiAnalyzer(true);
            } else {
                if (!settings.getAsBoolean(keyPrefix + PARSE_DECIMAL, true)) {
                    config.setAsciiAnalyzerParseDecimal(false);
                }
                if (settings.getAsBoolean(keyPrefix + PARSE_EN_MIX, false)) {
                    config.setAsciiAnalyzerParseEnMix(true);
                    config.setAsciiAnalyzerAppendEnMix(settings.getAsBoolean(keyPrefix + APPEND_EN_MIX, false));
                }
            }
            String value = settings.get(keyPrefix + CJK_ANALYZER);
            if (!SearchStringUtils.isEmpty(value)) {
                config.setCjkAnalyzerType(CjkAnalyzer.Type.valueOf(value.toUpperCase()));
            }
            if (settings.getAsBoolean(keyPrefix + MERGE_NUM_QUANTIFIER, false)) {
                config.setMergeNumQuantifier(true);
                config.setAppendNumQuantifier(settings.getAsBoolean(keyPrefix + APPEND_NUM_QUANTIFIER, false));
            }
            return config;
        }

        static List<SegmentConfig> parse(Settings settings) {
            final String namePrefix = settings.get(PREFIX_KEY, "");
            List<SegmentConfig> segmentConfigList = new ArrayList<>();
            int count = 0;
            for (; ; count++) {
                String keyPrefix = KEY + '.' + count + '.';
                String value = settings.get(keyPrefix + NAME);
                if (value == null) break;
                segmentConfigList.add(parse(namePrefix, keyPrefix, settings));
            }
            if (count == 0 && settings.get(KEY + '.' + NAME) != null) {
                segmentConfigList.add(parse(namePrefix, KEY + '.', settings));
            }
            return segmentConfigList;
        }
    }

    private final CjkLexiconFactory cjkLexicon;

    private final List<SegmentConfig> segmentConfigList;

    private final CharArraySet stopWords;

    public Config(Settings settings) {
        Environment env = new Environment(settings);
        String value = settings.get(CONFIG_FILE_PATH_KEY);
        final Path benzConfigDirPath = env.configFile().resolve(PLUGIN_NAME);
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
        this.segmentConfigList = Collections.unmodifiableList(Analyzer.parse(configSettings));
        stopWords = new CharArraySet(StopWords.instance().allStopwords(), false);
    }

    /**
     * 加载词库文件
     */
    private CjkLexiconFactory loadLexicon(Settings settings, Path lexiconPath) {
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
                : RootNodeType.ALL;
        return CjkLexiconFactory.valueOf(cjkRootNodeType, lexiconPaths);
    }

    public CjkLexiconFactory getCjkLexicon() {
        return cjkLexicon;
    }

    public List<SegmentConfig> getSegmentConfigList() {
        return segmentConfigList;
    }

    public CharArraySet getStopWords() {
        return stopWords;
    }
}
