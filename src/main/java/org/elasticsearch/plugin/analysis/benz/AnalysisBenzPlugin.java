package org.elasticsearch.plugin.analysis.benz;

import org.elasticsearch.common.inject.Module;
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

    @Override
    public String name() {
        return "analysis-benz";
    }

    @Override
    public String description() {
        return "benz analysis";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new BenzIndicesAnalysisModule());
    }
}
