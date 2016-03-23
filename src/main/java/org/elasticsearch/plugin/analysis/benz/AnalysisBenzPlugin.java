package org.elasticsearch.plugin.analysis.benz;

import com.tqmall.search.benz.Config;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.indices.analysis.BenzIndicesAnalysisModule;
import org.elasticsearch.plugins.Plugin;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by xing on 16/3/18.
 * analysis benz plugin main class
 *
 * @author xing
 */
public class AnalysisBenzPlugin extends Plugin {

    private final Config config;

    public AnalysisBenzPlugin(Settings settings) {
        config = new Config(settings);
    }

    @Override
    public String name() {
        return Config.PLUGIN_NAME;
    }

    @Override
    public String description() {
        return "Benz Analyzer for ElasticSearch";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new BenzIndicesAnalysisModule(config));
    }
}
