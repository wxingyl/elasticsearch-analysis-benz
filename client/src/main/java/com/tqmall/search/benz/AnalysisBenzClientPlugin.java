package com.tqmall.search.benz;

import com.tqmall.search.benz.action.LexicalizeAction;
import com.tqmall.search.benz.action.PinyinAction;
import com.tqmall.search.benz.action.TraditionToSimpleAction;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xing on 16/3/24.
 * 客户端调用ElasticSearch索引服务器,实现benz分词器词库增删改等操作
 *
 * @author xing
 */
public class AnalysisBenzClientPlugin extends Plugin implements ActionPlugin {

    /**
     * es 优先调用带{@link Settings}的构造方法, 虽然这个参数没用, 先放着吧
     */
    public AnalysisBenzClientPlugin(Settings settings) {
    }

    @Override
    public List<ActionHandler<? extends ActionRequest<?>, ? extends ActionResponse>> getActions() {
        List<ActionHandler<? extends ActionRequest<?>, ? extends ActionResponse>> actionHandlers = new ArrayList<>();
        actionHandlers.add(new ActionHandler<>(LexicalizeAction.INSTANCE, null));
        actionHandlers.add(new ActionHandler<>(PinyinAction.INSTANCE, null));
        actionHandlers.add(new ActionHandler<>(TraditionToSimpleAction.INSTANCE, null));
        return actionHandlers;
    }

}
