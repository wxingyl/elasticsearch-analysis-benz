package com.tqmall.search.benz;

import com.tqmall.search.commons.analyzer.CjkLexicon;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Created by xing on 16/3/19.
 * cjk词库管理
 *
 * @author xing
 * @see CjkLexicon
 */
public class Lexicon {

    public final static String LEXICON_FILE_KEY = "benz.lexicon.file";

    public final static String DEFAULT_LEXICON_FILE = "default_lexicon.txt";

    private final CjkLexicon cjkLexicon;

    @Inject
    public Lexicon(Environment env, Settings settings) {
        Path lexiconPath;
        if (settings.get(LEXICON_FILE_KEY) != null) {
            lexiconPath = FileSystems.getDefault().getPath(settings.get(LEXICON_FILE_KEY));
        } else {
            lexiconPath = env.pluginsFile().resolve(DEFAULT_LEXICON_FILE);
        }
        try {
            cjkLexicon = new CjkLexicon(Files.newInputStream(lexiconPath, StandardOpenOption.READ));
        } catch (IOException e) {
            throw new ElasticsearchException("read lexicon file: " + lexiconPath + " have IOException", e);
        }
    }

    public CjkLexicon getCjkLexicon() {
        return cjkLexicon;
    }
}
