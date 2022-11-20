package com.system.roll.excel.uitl;

import com.system.roll.constant.CommonEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ExcelUtil {

    /**
     * 导入excel工具的方法
     * @param tClass 导出的列表的泛型
     * @param inputStream 文件输入流
     * @param type 要导入的excel文件的类型
     * */
    <T> List<T> importExcel(Class<T>tClass, InputStream inputStream,ExcelType type) throws IOException;

    /**
     * 导出为excel的方法
     * @param data 要导出的数据
     * @param type 要导出的excel文件的类型
     * */
    <T> OutputStream exportExcel(List<T> data,ExcelType type);


    @SuppressWarnings("all")
    enum ExcelType implements CommonEnum {
        XLS("xls类型",0),
        XLSX("xlsx类型",1)
        ;
        private String msg;
        private Integer code;

        private ExcelType(String msg,Integer code){
            this.msg = msg;
            this.code = code;
        }
        @Override
        public String getMsg() {
            return this.msg;
        }
        @Override
        public Integer getCode() {
            return this.code;
        }
    }
}
