package com.tqmall.search.benz.action;

import com.tqmall.search.benz.action.py.PinyinAction;
import com.tqmall.search.benz.action.py.PinyinRequest;
import com.tqmall.search.benz.action.py.PinyinResponse;
import com.tqmall.search.commons.nlp.NlpConst;
import com.tqmall.search.commons.nlp.PinyinConvert;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.Map;

/**
 * Created by xing on 16/3/26.
 *
 * @author xing
 */
public class TransportPinyinAction extends HandledTransportAction<PinyinRequest, PinyinResponse> {

    @Inject
    public TransportPinyinAction(Settings settings, ThreadPool threadPool, TransportService transportService, ActionFilters actionFilters, IndexNameExpressionResolver indexNameExpressionResolver) {
        super(settings, PinyinAction.NAME, threadPool, transportService, actionFilters, indexNameExpressionResolver, PinyinRequest.class);
    }

    @Override
    protected void doExecute(PinyinRequest request, ActionListener<PinyinResponse> listener) {
        int flags = 0;
        if (request.appendFlags() != null) {
            for (AppendFlag f : request.appendFlags()) {
                switch (f) {
                    case WHITESPACE:
                        flags |= NlpConst.APPEND_CHAR_WHITESPACE;
                        break;
                    case LETTER:
                        flags |= NlpConst.APPEND_CHAR_LETTER;
                        break;
                    case DIGIT:
                        flags |= NlpConst.APPEND_CHAR_DIGIT;
                        break;
                    case OTHER:
                        flags |= NlpConst.APPEND_CHAR_OTHER;
                        break;
                }
            }
        }
        String pinyin = null, firstLetter = null;
        if (request.needFirstLetter()) {
            pinyin = PinyinConvert.instance().convert(request.text(), flags);
        } else {
            Map.Entry<String, String> entry = PinyinConvert.instance().firstLetterConvert(request.text(), flags);
            if (entry != null) {
                pinyin = entry.getKey();
                firstLetter = entry.getValue();
            }
        }
        listener.onResponse(new PinyinResponse(pinyin, firstLetter));
    }
}