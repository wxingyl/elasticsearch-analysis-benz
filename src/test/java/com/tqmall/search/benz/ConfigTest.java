package com.tqmall.search.benz;

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
        List<SegmentConfig> list = Config.Analyzer.parse(settings);
        Assert.assertEquals(2, list.size());
        for (SegmentConfig s : list) {
            System.out.println(s);
        }
    }
}
