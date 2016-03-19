package org.elasticsearch.indices.analysis;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

/**
 * Created by xing on 16/3/19.
 *
 * @author xing
 */
public class BenzIndicesAnalysis extends AbstractComponent {

    @Inject
    public BenzIndicesAnalysis(Settings settings, IndicesAnalysisService indicesAnalysisService) {
        super(settings);

    }
}