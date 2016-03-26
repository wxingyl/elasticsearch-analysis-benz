package com.tqmall.search.benz.action;

import com.tqmall.search.benz.Config;
import com.tqmall.search.commons.nlp.NlpUtils;
import com.tqmall.search.commons.nlp.SegmentConfig;
import org.elasticsearch.common.settings.Settings;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by xing on 16/3/22.
 *
 * @author xing
 */
public class ConfigTest {

    @Test
    public void yamlParseTest() {
        Path configPath = NlpUtils.getPathOfClass(ConfigTest.class, "/config.yml");
        Settings settings = Settings.builder()
                .loadFromPath(configPath)
                .build();
        List<Config.Analysis> list = Config.Analysis.parse(settings);
        Assert.assertEquals(4, list.size());
        for (SegmentConfig s : list) {
            System.out.println(s);
        }
    }
}
