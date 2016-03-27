package com.tqmall.search.benz.action;

import com.tqmall.search.commons.nlp.TraditionToSimple;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 * Created by xing on 16/3/27.
 *
 * @author xing
 */
public class TransportTraditionToSimpleAction extends HandledTransportAction<TraditionToSimpleRequest, TraditionToSimpleResponse> {

    @Inject
    public TransportTraditionToSimpleAction(Settings settings, ThreadPool threadPool, TransportService transportService,
                                            ActionFilters actionFilters, IndexNameExpressionResolver indexNameExpressionResolver) {
        super(settings, TraditionToSimpleAction.NAME, threadPool, transportService, actionFilters,
                indexNameExpressionResolver, TraditionToSimpleRequest.class);
    }

    @Override
    protected void doExecute(TraditionToSimpleRequest request, ActionListener<TraditionToSimpleResponse> listener) {
        listener.onResponse(new TraditionToSimpleResponse(TraditionToSimple.instance().convert(request.word())));
    }
}
