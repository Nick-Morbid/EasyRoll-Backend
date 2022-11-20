package com.system.roll.excel.uitl.impl;

import com.system.roll.common.context.CommonContext;
import com.system.roll.constant.impl.ResultCode;
import com.system.roll.excel.annotation.Excel;
import com.system.roll.excel.uitl.ExcelUtil;
import com.system.roll.exception.impl.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component(value = "ExcelUtil")
public class ExcelUtilImpl implements ExcelUtil {
    @Resource
    private CommonContext commonContext;
    @Override
    public <T> List<T> importExcel(Class<T> tClass, InputStream inputStream, ExcelType type) throws IOException {
        Workbook workbook;
        if (type.equals(ExcelType.XLS)){
            workbook = new HSSFWorkbook(inputStream);
        }else {
            workbook = new XSSFWorkbook(inputStream);
        }
        try {
            /*得到表*/
            Sheet sheet = workbook.getSheetAt(0);
            /*创建数据项的构造器工厂*/
            ItemBuilderFactory<T> factory = new ItemBuilderFactory<T>(tClass);
            /*获取所需列名称的集合*/
            Set<String> rowNameSet = factory.getRowNameSet();
            /*按行按列进行查找，找到包含所需列名称的行号以及列号*/
            int rowIndex=0,colIndex=0;
            Iterator<Row> rowIterator = sheet.rowIterator();
            boolean flag = false;
            /*计算得到所序列名称与其索引之间的映射*/
            Map<Integer,String> indexNameMap = new HashMap<>();
            while (rowIterator.hasNext()&&!flag){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    cellIterator.remove();
                    rowIndex = cell.getRowIndex();
                    colIndex = cell.getColumnIndex();
                    /*找到了包含所需列名称的那一行*/
                    if (rowNameSet.contains(cell.getStringCellValue())){
                        flag = true;
                        indexNameMap.put(colIndex,cell.getStringCellValue());
                    }
                }
                rowIterator.remove();
            }
            /*如果查找到了最后一行，说明所需列没有对应的数据，认为此时的数据是无效的*/
            if (!rowIterator.hasNext()) throw new ServiceException(ResultCode.FAILED_TO_IMPORT_EXCEL);

            /*开始生成导出的数据*/
            List<T> res = new ArrayList<>();
            /*开始遍历excel（从包含所需列的那一行的下一行开始*/
            for (rowIndex = rowIndex+1,flag = false;rowIndex<=sheet.getLastRowNum()&&!flag;++rowIndex){
                Row row = sheet.getRow(rowIndex);
                /*获取构造器*/
                ItemBuilderFactory.ItemBuilder<T> builder = factory.getItemBuilder();
                for (Integer index : indexNameMap.keySet()) {
                    /*进行构造*/
                    try {
                        Cell cell = row.getCell(index);
                        try {
                            builder.add(indexNameMap.get(index),cell.getStringCellValue());
                        }catch (IllegalStateException e){
                            builder.add(indexNameMap.get(index),cell.getNumericCellValue());
                        }
                    }catch (NullPointerException e){
                        log.info("出现空行，构造结束");
                        flag = true;
                        break;
                    }
                }
                if (!flag){
                    /*获取构造结果*/
                    T build = builder.build();
                    /*保存构造结果*/
                    res.add(build);
                }
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultCode.FAILED_TO_IMPORT_EXCEL);
        }
    }

    @Override
    public <T> String exportExcel(Class<T>tClass,List<T> data,ExcelType type) {
        /*创建工作簿*/
        Workbook workbook;
        if (type.equals(ExcelType.XLS)){
            workbook = new HSSFWorkbook();
        }else {
            workbook = new XSSFWorkbook();
        }
        /*创建工作区*/
        Sheet sheet = workbook.createSheet();
        try {
            /*获取Row的构造器工厂*/
            RowBuilderFactory<T> factory = new RowBuilderFactory<>(tClass);
            /*获取索引和名称的映射*/
            Map<Integer, String> indexNameMap = factory.getIndexNameMap();
            /*创建表头*/
            Row header = sheet.createRow(0);
            for (Integer colIndex : indexNameMap.keySet()) header.createCell(colIndex).setCellValue(indexNameMap.get(colIndex));
            /*获取构造器*/
            RowBuilderFactory.RowBuilder<T> builder = factory.getRowBuilder();
            /*开始进行构造*/
            for (int i = 0; i < data.size(); i++) builder.build(sheet.createRow(i+1),data.get(i));
            /*构造完成，准备输出*/
            File tempFile = File.createTempFile( "excel", type.getMsg(), new File(commonContext.getTempDir()));
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            workbook.write(outputStream);
            outputStream.close();
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultCode.FAILED_TO_EXPORT_EXCEL);
        }
    }











    /*================================以下是导入excel所需要的构造器==================================*/

    /**
     * 数据项构造器的工厂类
     * */
    public static class ItemBuilderFactory<T>{
        /**
         * excel列名与对应的set方法的集合
         * */
        private final Map<String,TypeAndSetMethod<T>> setMethodMap = new ConcurrentHashMap<>();
        /**
         * 要构造的对象的类型
         * */
        private final Class<T> tClass;

        public ItemBuilderFactory(Class<T> tClass) throws NoSuchMethodException {
            this.tClass = tClass;
            /*根据tClass中@Excel注解的内容，生成我们的set方法集合*/

            /*遍历所有的属性，寻找有被@Excel方法注解的属性，并获取对应的value*/
            for (Field field : tClass.getDeclaredFields()) {
                Excel annotation = field.getDeclaredAnnotation(Excel.class);
                if (annotation!=null){
                    /*value对应的是该属性对应的excel列值*/
                    String value = annotation.value();
                    /*获取该属性的名称*/
                    String name = field.getName();
                    /*获取该属性的类型*/
                    Class<?> type = field.getType();
                    /*提取该属性的构造方法*/
                    Method setMethod = tClass.getDeclaredMethod("set" + StringUtil.toUpperCase(name.charAt(0)) + name.substring(1),type);
                    /*构造属性类型和set方法的封装对象，并存入集合中*/
                    setMethodMap.put(value, new TypeAndSetMethod<>(setMethod, type));
                }
            }
        }

        /**
         * 获取一个构造器
         * */
        public ItemBuilder<T> getItemBuilder() throws Exception {
            return new ItemBuilder<>(setMethodMap, tClass);
        }


        /**
         * 数据项构造器
         * */
        public static class ItemBuilder<T>{
            private final Map<String,TypeAndSetMethod<T>> setMethodMap;
            private final T instance;

            /**
             * 数据项构造器的构造函数
             * @param setMethodMap 由工厂注入的集合
             * @param tClass 用于创建要构造的实例
             * */
            public ItemBuilder(Map<String,TypeAndSetMethod<T>> setMethodMap,Class<T> tClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
                this.setMethodMap = setMethodMap;
                this.instance = tClass.getDeclaredConstructor().newInstance();
            }

            /**
             * 构造方法，添加新的字段
             * @param rowName 进行构造的列名
             * @param value 该列对应的值
             * */
            public ItemBuilder<?> add(String rowName,Object value) throws InvocationTargetException, IllegalAccessException {
                if (setMethodMap.containsKey(rowName)){
                    setMethodMap.get(rowName).invoke(instance,value);
                }
                return this;
            }

            /**
             * 构造方法，获取实例化的值
             * */
            public T build(){
                return instance;
            }
        }

        /**
         * 获取列表名称的集合
         * */
        public Set<String> getRowNameSet(){
            return setMethodMap.keySet();
        }
    }

    /**
     * 封装了属性的类型和属性的set方法的对象
     * */
    public static class TypeAndSetMethod<T>{
        private final Method setMethod;
        private final Class<?> type;

        public TypeAndSetMethod(Method setMethod, Class<?> type) {
            this.setMethod = setMethod;
            this.type = type;
        }

        /**
         * @param instance 要构造注入属性的实例
         * @param param 用于构造的属性的值
         * */
        public void invoke(T instance,Object param) throws InvocationTargetException, IllegalAccessException {
            setMethod.invoke(instance,cast(param));
        }

        private Object cast(Object param){
            if (param instanceof Double||param instanceof Long || param instanceof Integer){
                if (type.equals(String.class)){
                    if (param instanceof Double) return String.format("%.0f",param);
                    else return String.valueOf(param);
                }else if (type.equals(Double.class)){
                    return Double.valueOf(String.valueOf(param));
                }else if (type.equals(Long.class)){
                    return Long.valueOf(String.valueOf(param));
                }else if (type.equals(Integer.class)){
                    return Integer.valueOf(String.valueOf(param));
                }
            }else {
                return type.cast(param);
            }
            return null;
        }
    }

    /*================================以下是导出excel所需要的构造器==================================*/


    /**
     * 构造Row的构造器工厂
     * */
    public static class RowBuilderFactory<T>{
        /*列名和get方法的集合*/
        private final Map<String,TypeAndGetMethod<T>> getMethodMap = new LinkedHashMap<>();
        /*列名和对应索引的集合*/
        private final Map<String,Integer> nameIndexMap = new LinkedHashMap<>();

        public RowBuilderFactory(Class<T> tClass) throws NoSuchMethodException {
            /*根据tClass中@Excel注解的内容，生成我们的get方法集合*/
            int count = 0;
            /*遍历所有的属性，寻找有被@Excel方法注解的属性，并获取对应的value*/
            for (Field field : tClass.getDeclaredFields()) {
                Excel annotation = field.getDeclaredAnnotation(Excel.class);
                if (annotation!=null){
                    /*value对应的是该属性对应的excel列值*/
                    String value = annotation.value();
                    /*获取该属性的名称*/
                    String name = field.getName();
                    /*获取该属性的类型*/
                    Class<?> type = field.getType();
                    /*提取该属性的构造方法*/
                    Method getMethod = tClass.getDeclaredMethod("get" + StringUtil.toUpperCase(name.charAt(0)) + name.substring(1));
                    /*构造属性类型和get方法的封装对象，并存入集合中*/
                    getMethodMap.put(value,new TypeAndGetMethod<>(getMethod,type));
                    /*向列名和索引的集合添加新记录*/
                    nameIndexMap.put(value,count++);
                }
            }
        }

        /**
         * 获取一个构造器
         * */
        public RowBuilder<T> getRowBuilder() {
            return new RowBuilder<>(getMethodMap,nameIndexMap);
        }

        /**
         * Row构造器
         * */
        public static class RowBuilder<T>{
            private final Map<String,TypeAndGetMethod<T>> getMethodMap;
            private final Map<String,Integer> nameIndexMap;

            /**
             * Row构造器的构造函数
             * @param getMethodMap 由工厂注入的集合
             * */
            public RowBuilder(Map<String,TypeAndGetMethod<T>> getMethodMap,Map<String,Integer> nameIndexMap) {
                this.getMethodMap = getMethodMap;
                this.nameIndexMap = nameIndexMap;
            }

            /**
             * 构造方法，获取根据数据构造得到的Row对象
             * */
            public Row build(Row row,T data) throws InvocationTargetException, IllegalAccessException {
                for (String rowName : getMethodMap.keySet()) {
                    Integer index = this.nameIndexMap.get(rowName);
                    Cell cell = row.createCell(index);
                    TypeAndGetMethod<T> getMethod = getMethodMap.get(rowName);
                    cell.setCellValue((String) getMethod.invoke(data));
                }
                return row;
            }
        }

        /**
         * 获取索引与列名的集合
         * */
        public Map<Integer,String> getIndexNameMap(){
            Map<Integer,String> indexNameMap = new LinkedHashMap<>();
            for (Integer index : this.nameIndexMap.values()) {
                for (String name : nameIndexMap.keySet()) {
                    if (nameIndexMap.get(name).equals(index)){
                        indexNameMap.put(index,name);
                    }
                }
            }
            return indexNameMap;
        }
    }





    /**
     * 封装了属性的类型和对应的get方法
     * */
    public static class TypeAndGetMethod<T>{
        private final Method getMethod;
        private final Class<?> tClass;

        public TypeAndGetMethod(Method getMethod,Class<?> tClass){
            this.getMethod = getMethod;
            this.tClass = tClass;
        }

        /**
         * 调用get方法获取对应的值
         * */
        public Object invoke(T instance) throws InvocationTargetException, IllegalAccessException {
            return tClass.cast(getMethod.invoke(instance));
        }

    }





    public static void main(String[] args) throws Exception {
//        String field = "name";
//        System.out.println(StringUtil.toUpperCase(field.charAt(0))+field.substring(1));
//        ExcelUtilImpl.ItemBuilderFactory<Test> factory = new ItemBuilderFactory<>(Test.class);
//        ExcelUtilImpl.ItemBuilderFactory.ItemBuilder<Test> builder = factory.getItemBuilder();
//        Test test = (Test) builder.add("学号", "123").add("姓名", "nick").build();
//        System.out.println(test);
//        factory.getRowNameSet().forEach(System.out::println);


//        RowBuilderFactory<Test> factory = new RowBuilderFactory<>(Test.class);
//        RowBuilderFactory.RowBuilder<Test> builder = factory.getRowBuilder();
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet();
//        Row row = builder.build(sheet.createRow(0), new Test().setId("123").setName("nick"));
//        Iterator<Cell> cellIterator = row.cellIterator();
//        while (cellIterator.hasNext()) {
//            Cell cell = cellIterator.next();
//            System.out.println(cell.getColumnIndex());
//            System.out.println(cell.getStringCellValue());
//        }

//        Map<Integer, String> indexNameMap = builder.getIndexNameMap();
//        for (Integer integer : indexNameMap.keySet()) {
//            System.out.println(integer);
//            System.out.println(indexNameMap.get(integer));
//        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Test{
        @Excel(value = "学号")
        private String id;
        @Excel(value = "姓名")
        private String name;
    }
}
