package org.elasticsearch.plugin.analysis.benz;

import org.elasticsearch.plugins.Plugin;

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
}
