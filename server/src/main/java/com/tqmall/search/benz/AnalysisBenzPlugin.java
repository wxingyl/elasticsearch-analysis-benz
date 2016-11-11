package com.tqmall.search.benz;

import com.tqmall.search.benz.action.LexicalizeAction;
import com.tqmall.search.benz.action.PinyinAction;
import com.tqmall.search.benz.action.TraditionToSimpleAction;
import com.tqmall.search.benz.action.TransportLexicalizeAction;
import com.tqmall.search.benz.action.TransportPinyinAction;
import com.tqmall.search.benz.action.TransportTraditionToSimpleAction;
import com.tqmall.search.benz.analysis.BenzAnalyzer;
import com.tqmall.search.benz.analysis.BenzCjkCharFilter;
import com.tqmall.search.benz.analysis.BenzTokenizer;
import com.tqmall.search.commons.nlp.Segment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.AbstractCharFilterFactory;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.CharFilterFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xing on 16/3/18.
 * analysis benz plugin main class
 *
 * @author xing
 */
public class AnalysisBenzPlugin extends Plugin implements ActionPlugin, AnalysisPlugin {

    private final Config config;

    /**
     * es 优先调用带{@link Settings}的构造方法, 虽然这个参数没用, 先放着吧
     */
    public AnalysisBenzPlugin(Settings settings) {
        this.config = new Config(settings);
    }

    @Override
    public Collection<Module> createGuiceModules() {
        return Collections.singletonList(b -> b.bind(Config.class).toInstance(config));
    }

    @Override
    public List<ActionHandler<? extends ActionRequest<?>, ? extends ActionResponse>> getActions() {
        List<ActionHandler<? extends ActionRequest<?>, ? extends ActionResponse>> actionHandlers = new ArrayList<>();
        actionHandlers.add(new ActionHandler<>(LexicalizeAction.INSTANCE, TransportLexicalizeAction.class));
        actionHandlers.add(new ActionHandler<>(PinyinAction.INSTANCE, TransportPinyinAction.class));
        actionHandlers.add(new ActionHandler<>(TraditionToSimpleAction.INSTANCE, TransportTraditionToSimpleAction.class));
        return actionHandlers;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> retMap = new HashMap<>();
        for (Config.AnalysisConfig ca : config.getSegmentConfig()) {
            final Segment segment = ca.createSegment(config.getCjkLexicon());
            retMap.put(segment.getName(), (indexSettings, environment, name, settings) -> new AbstractIndexAnalyzerProvider(indexSettings, name, settings) {

                @Override
                public Analyzer get() {
                    return new BenzAnalyzer(config, segment);
                }
            });
        }
        return retMap;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> retMap = new HashMap<>();
        for (Config.AnalysisConfig ca : config.getSegmentConfig()) {
            final Segment segment = ca.createSegment(config.getCjkLexicon());
            retMap.put(segment.getName(), (indexSettings, environment, name, settings) -> new AbstractTokenizerFactory(indexSettings, name, settings) {
                @Override
                public Tokenizer create() {
                    return new BenzTokenizer(segment);
                }
            });
        }
        return retMap;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<CharFilterFactory>> getCharFilters() {
        return Collections.singletonMap("benz_stop", (indexSettings, environment, name, settings) -> new AbstractCharFilterFactory(indexSettings, name) {

            @Override
            public Reader create(Reader tokenStream) {
                return new BenzCjkCharFilter(tokenStream);
            }
        });
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        return Collections.singletonMap("benz_cjk", (indexSettings, environment, name, settings) -> new AbstractTokenFilterFactory(indexSettings, name, settings) {

            @Override
            public TokenStream create(TokenStream tokenStream) {
                return new StopFilter(tokenStream, config.getStopWords());
            }
        });
    }
}
