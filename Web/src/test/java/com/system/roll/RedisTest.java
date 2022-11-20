package com.system.roll;

import com.system.roll.redis.StudentRedis;
import com.system.roll.utils.PinyinUtil;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest {
    @Resource
    private PinyinUtil pinyinUtil;
    @Resource
    private StudentRedis studentRedis;

    @Test
    public void testPinYin() throws BadHanyuPinyinOutputFormatCombination {
        studentRedis.savePinYin("032002601",pinyinUtil.toPinyin("陈宏侨"));
        List<String> pinYin = Arrays.asList(studentRedis.getPinYin("032002601"));
        pinYin.forEach(item->System.out.print(item+" "));
    }
}
