package com.tqmall.search.benz;

import com.tqmall.search.commons.nlp.NlpUtils;
import com.tqmall.search.commons.nlp.SegmentConfig;
import org.elasticsearch.common.settings.Settings;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
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
        Settings settings = null;
        try {
            settings = Settings.builder()
                    .loadFromPath(configPath)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Config.AnalysisConfig> list = Config.AnalysisConfig.parse(settings);
        Assert.assertEquals(4, list.size());
        for (SegmentConfig s : list) {
            System.out.println(s);
        }
    }
}
