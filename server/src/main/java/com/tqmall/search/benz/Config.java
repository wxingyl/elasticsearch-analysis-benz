package com.tqmall.search.benz;

import com.tqmall.search.commons.analyzer.CjkAnalyzer;
import com.tqmall.search.commons.analyzer.CjkLexicon;
import com.tqmall.search.commons.analyzer.MaxAsciiAnalyzer;
import com.tqmall.search.commons.analyzer.StopWords;
import com.tqmall.search.commons.lang.AsyncInit;
import com.tqmall.search.commons.lang.Supplier;
import com.tqmall.search.commons.nlp.SegmentConfig;
import com.tqmall.search.commons.trie.RootNodeType;
import com.tqmall.search.commons.utils.SearchStringUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;

import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsException;
import org.elasticsearch.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.elasticsearch.common.Strings.cleanPath;

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
    private static final String PLUGIN_NAME = "analysis-benz";

    /**
     * 配置文件路径[确认能够正确打开, 可读, yml格式文件], 在es主配置文件[elasticsearch.yml]中设置该文件的路径
     * 未配置该值, 则读取默认文件{@link #DEFAULT_CONFIG_FILE_NAME}
     * <p>
     * 该属性的值如果为[es, elasticsearch] 中的任意一个, 则认为不需要单独的配置文件, 直接从es主配置文件[elasticsearch.yml]中读取
     * 推荐单独文件
     */
    private static final String CONFIG_FILE_PATH_KEY = "benz.path.conf";

    /**
     * 词库文件路径配置key, 可以为文件夹
     * 如果配置项值为一个文件夹, 则加载该文件夹下面, 以"words"为前缀, 并且".dic"为后缀的所有词库文件
     */
    private static final String LEXICON_FILE_PATH_KEY = "benz.path.lexicon";

    /**
     * 词库文件是否只包含中文cjk字符, 默认true
     */
    private static final String LEXICON_ONLY_CJK_KEY = "benz.lexicon.only_cjk";

    /**
     * 默认的配置文件名, 没有配置{@link #CONFIG_FILE_PATH_KEY}, 则读取这个配置文件
     */
    private static final String DEFAULT_CONFIG_FILE_NAME = "config.yml";

    /**
     * 默认的词库文件名, 没有配置{@link #LEXICON_FILE_PATH_KEY} 则读取Es提供的默认配置路径
     */
    private static final String DEFAULT_LEXICON_FILE_NAME = "words_default.dic";

    /**
     * 分词器配置文件解析
     * 默认值遵循{@link SegmentConfig}中的默认值, 具体查看其文档
     *
     * @see SegmentConfig
     */
    public static class AnalysisConfig extends SegmentConfig {
        /**
         * 分词器配置 key
         * 必须配置
         */
        static final String KEY = "benz.analyzer";
        /**
         * benz配置分词器名称前缀 key
         * 默认 benz_
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
        /**
         * 对于英文单词,是否需要执行stem操作, 可以配置"k"和"porter", 分别使用{@link KStemFilter}和{@link PorterStemFilter}作为stem算法
         * 不做配置则不对keyword做stem操作
         */
        static final String EN_STEM = "en_stem";

        static final String STOP_WORD = "stop_word";

        static AnalysisConfig parse(String namePrefix, String keyPrefix, Settings settings) {
            AnalysisConfig config = new AnalysisConfig((namePrefix + getConfigValue(settings, keyPrefix + NAME)).toLowerCase());
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
            String value = getConfigValue(settings, keyPrefix + CJK_ANALYZER);
            if (value != null) {
                config.setCjkAnalyzerType(CjkAnalyzer.Type.valueOf(value.toUpperCase()));
            }
            if (settings.getAsBoolean(keyPrefix + MERGE_NUM_QUANTIFIER, false)) {
                config.setMergeNumQuantifier(true);
                config.setAppendNumQuantifier(settings.getAsBoolean(keyPrefix + APPEND_NUM_QUANTIFIER, false));
            }
            value = getConfigValue(settings, keyPrefix + EN_STEM);
            if (value != null) {
                try {
                    config.enStem = EnStemType.valueOf(value.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("you config analyzer: " + config.getName() + ", the " + EN_STEM
                            + " filed value: " + value + " must is \"k\" or \"porter\"");
                }
            }
            if (settings.getAsBoolean(keyPrefix + STOP_WORD, true)) {
                config.needStopWord = true;
            }
            return config;
        }

        static List<AnalysisConfig> parse(Settings settings) {
            String namePrefix = getConfigValue(settings, PREFIX_KEY);
            if (namePrefix == null) namePrefix = "benz_";
            else namePrefix = namePrefix.toLowerCase();
            List<AnalysisConfig> segmentConfigList = new ArrayList<>();
            int count = 0;
            for (; ; count++) {
                String keyPrefix = KEY + '.' + count + '.';
                String value = getConfigValue(settings, keyPrefix + NAME);
                if (value == null) break;
                segmentConfigList.add(parse(namePrefix, keyPrefix, settings));
            }
            if (count == 0 && settings.get(KEY + '.' + NAME) != null) {
                segmentConfigList.add(parse(namePrefix, KEY + '.', settings));
            }
            return segmentConfigList;
        }

        EnStemType enStem;

        boolean needStopWord;

        AnalysisConfig(String name) {
            super(name);
        }

        public EnStemType getEnStem() {
            return enStem;
        }

        public boolean isNeedStopWord() {
            return needStopWord;
        }
    }

    public enum EnStemType {
        K {
            @Override
            public TokenFilter wrapper(TokenStream input) {
                return new KStemFilter(input);
            }
        },
        PORTER {
            @Override
            public TokenFilter wrapper(TokenStream input) {
                return new PorterStemFilter(input);
            }
        };

        public abstract TokenFilter wrapper(TokenStream input);
    }

    private final Supplier<CjkLexicon> cjkLexicon;

    private final Map<String, AnalysisConfig> segmentConfigMap;

    private final CharArraySet stopWords;

    Config(Settings settings) {
        final Path esConfigDir;
        if (Environment.PATH_CONF_SETTING.exists(settings)) {
            esConfigDir = PathUtils.get(cleanPath(Environment.PATH_CONF_SETTING.get(settings)));
        } else {
            esConfigDir = PathUtils.get(cleanPath(Environment.PATH_HOME_SETTING.get(settings))).resolve("config");
        }
        String value = getConfigValue(settings, CONFIG_FILE_PATH_KEY);
        final Path benzConfigDirPath = esConfigDir.resolve(PLUGIN_NAME);
        if (!"es".equalsIgnoreCase(value) && !"elasticsearch".equalsIgnoreCase(value)) {
            Path configPath = SearchStringUtils.isEmpty(value) ? benzConfigDirPath.resolve(DEFAULT_CONFIG_FILE_NAME)
                    : Paths.get(value);
            try {
                settings = Settings.builder()
                        .loadFromPath(configPath)
                        .build();
            } catch (IOException e) {
                log.error("load config path: " + configPath + " exception", e);
                throw new RuntimeException("load config path: " + configPath + " exception", e);
            }
        }
        value = getConfigValue(settings, LEXICON_FILE_PATH_KEY);
        List<Path> lexiconPaths = getLexiconPaths(value == null ? benzConfigDirPath.resolve(DEFAULT_LEXICON_FILE_NAME) : Paths.get(value));
        final RootNodeType rootNodeType = settings.getAsBoolean(LEXICON_ONLY_CJK_KEY, true) ? RootNodeType.CJK : RootNodeType.ALL;
        this.segmentConfigMap = Collections.unmodifiableMap(AnalysisConfig.parse(settings).stream().collect(HashMap::new, (map, a) -> map.put(a.getName(), a), HashMap::putAll));
        this.cjkLexicon = new AsyncInit<>(() -> new CjkLexicon(rootNodeType, lexiconPaths), AsyncInit.DEFAULT_WAIT_TIMEOUT);
        this.stopWords = new CharArraySet(StopWords.instance().allStopwords(), false);
    }

    /**
     * 配置文件是yml的, 所有的字符串在yml解析的时候做了term
     */
    private static String getConfigValue(Settings settings, String key) {
        return SearchStringUtils.filterString(settings.get(key));
    }

    private List<Path> getLexiconPaths(Path lexiconPath) {
        if (Files.isDirectory(lexiconPath, LinkOption.NOFOLLOW_LINKS)) {
            try {
                return Arrays.asList(FileSystemUtils.files(lexiconPath, p -> p.startsWith("words") && p.endsWith(".dic")));
            } catch (IOException e) {
                log.error("load lexicon path: " + lexiconPath + " directory files have exception", e);
                throw new SettingsException("load lexicon path: " + lexiconPath + " directory files have exception", e);
            }
        } else {
            return Collections.singletonList(lexiconPath);
        }
    }

    public Supplier<CjkLexicon> getCjkLexicon() {
        return cjkLexicon;
    }

    public Collection<AnalysisConfig> getSegmentConfig() {
        return segmentConfigMap.values();
    }

    /**
     * 获取指定分词器名称的配置
     */
    public AnalysisConfig getSegmentConfig(String analyzerName) {
        return Objects.requireNonNull(segmentConfigMap.get(analyzerName), "can not find analyzer named " + analyzerName);
    }

    public CharArraySet getStopWords() {
        return stopWords;
    }

}
