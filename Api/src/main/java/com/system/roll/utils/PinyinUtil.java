package com.system.roll.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼音工具组件
 * */
@Component
public class PinyinUtil {

    public String[] toPinyin(String chineseLan) {
        List<String> list = new ArrayList<>();
        /*转换为字符*/
        char[] chars = chineseLan.toCharArray();
        /*输出格式*/
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        /*小写格式输出*/
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        /*需要语调输出*/
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        for (char aChar : chars) {
                try {
                    if (String.valueOf(aChar).matches("[\u4e00-\u9fa5]+")){
                        /*没使用中文则不转换*/
                        list.add(PinyinHelper.toHanyuPinyinStringArray(aChar,format)[0]);
                    }else {
                        /*有使用中文则进行转换*/
                        list.add(PinyinHelper.toHanyuPinyinStringArray(aChar,format)[0]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
        }
        return list.toArray(new String[]{});
    }

}
