package com.system.roll;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.context.common.CommonContext;
import com.system.roll.entity.constant.impl.Period;
import com.system.roll.entity.constant.impl.TimeUnit;
import com.system.roll.entity.pojo.Course;
import com.system.roll.entity.pojo.CourseArrangement;
import com.system.roll.entity.pojo.LeaveRelation;
import com.system.roll.entity.properites.CommonProperties;
import com.system.roll.entity.vo.Result;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.excel.annotation.Excel;
import com.system.roll.excel.uitl.ExcelUtil;
import com.system.roll.formBuilder.FormBuilder;
import com.system.roll.mapper.*;
import com.system.roll.oss.OssHandler;
import com.system.roll.oss.OssResource;
import com.system.roll.redis.CourseRedis;
import com.system.roll.redis.StudentRedis;
import com.system.roll.utils.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@org.springframework.boot.test.context.SpringBootTest(webEnvironment = org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTest {

    @Resource(name = "ExcelUtil")
    private ExcelUtil excelUtil;
    @Resource(name = "CommonContext")
    private CommonContext commonContext;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private IdUtil idUtil;
    @Resource
    private MajorMapper majorMapper;
    /**
     * 批量导入院系数据
     * */
    @Test
    public void batchInsertDepartmentInfo() throws IOException {
        String resourceDir = commonContext.getResourceDir();
        FileInputStream inputStream = new FileInputStream(new File(resourceDir + "/福州大学院系信息.xlsx"));
        List<Department> departments = excelUtil.importExcel(Department.class, inputStream, ExcelUtil.ExcelType.XLSX);
        departments.forEach(department -> {
            for (int i = 2019; i <=2022 ; i++) {
                com.system.roll.entity.pojo.Department department1 = new com.system.roll.entity.pojo.Department()
                        .setDepartmentName(department.getDepartmentName())
                        .setGrade(i)
                        .setId(idUtil.getId());
                departmentMapper.insert(department1);
            }
        });
    }
    @Test
    public void test(){
        System.out.println(idUtil.getId());
    }

    @Resource
    private HttpRequestUtil httpRequestUtil;
    @Test
    public void testSpider() throws IOException {
        String url = "https://zsks2.fzu.edu.cn/linianluqu/?zssf-3,p-%d,zxkl-0=";
        Set<String> majorSet = new HashSet<>();
        for (int i = 1; i <=32 ; i++) {
            String url1 = String.format(url, i);
            Result<?> result = httpRequestUtil.httpGet(url1, null);
            String html = String.valueOf(result.getData());
            String substring = html.substring(html.lastIndexOf("<tbody>"), html.lastIndexOf("</tbody>"));
            Matcher matcher = Pattern.compile("<td style=\"text-align: left;\">[\u4E00-\u9FA5]{1,}</td>").matcher(substring);
            while (matcher.find()){
                Matcher matcher1 = Pattern.compile("[\u4E00-\u9FA5]{1,}").matcher(matcher.group());
                if (matcher1.find()) {
                    String major = matcher1.group();
                    if (!majorSet.contains(major)){
                        majorSet.add(major);
//                        Major major1 = new Major()
//                                .setId(idUtil.getId())
//                                .setMajorName(major);
//                        majorMapper.insert(major1);
                    }
                }
            }
        }
        List<Major> majors = majorSet.stream().map(majorName -> new Major().setMajorName(majorName)).collect(Collectors.toList());
        excelUtil.exportExcel(Major.class,majors, ExcelUtil.ExcelType.XLSX);
        System.out.println("共有："+majorSet.size()+"个专业");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Department{
        @Excel(value = "学院名称")
        private String departmentName;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Major{
        @Excel("专业名称")
        private String majorName;
    }

    @Resource
    private OssHandler ossHandler;
    @Test
    public void testOssUpload(){
        File file = new File(commonContext.getResourceDir() + "福州大学专业信息.xlsx");
        String excel = ossHandler.postFile(file, "excel");
        System.out.println(excel);
    }
    @Test
    public void testOssDownload() throws IOException {
        OssResource file = ossHandler.getFile("excel/53610122.xlsx");
        ossHandler.postFile(file.getInputStream(),"excel/111111111.xlsx");
    }
    @Resource
    private CourseRedis courseRedis;
    @Resource
    private CourseMapper courseMapper;
    @Test
    public void insertCourseName(){
        List<Course> courses = courseMapper.selectList(null);
        courses.forEach(course -> courseRedis.saveCourseName(course.getId(),course.getCourseName()));
    }
    @Resource
    private PinyinUtil pinyinUtil;
    @Resource(name = "StudentRedis")
    private StudentRedis studentRedis;
    @Test
    public void testInsertRedisHash(){
        studentRedis.savePinYin("032002601",pinyinUtil.toPinyin("陈宏侨"));
        studentRedis.savePinYin("032002601",pinyinUtil.toPinyin("陈宏侨"));

    }
    @Resource(name = "FormBuilder")
    private FormBuilder formBuilder;
    @Test
    public void testGetForm(){
        StudentRollListVo form = formBuilder.getForm("55521624");
        form.getStudents().forEach(System.out::println);
        System.out.println(form.getTotal());
    }
    @Test
    public void testGetConvertor(){
//        System.out.println(SpringContextUtil.getBean(RollDataConvertorImpl.class).SingleRollStatisticVoToRollStatistics(new SingleRollStatisticVo().setEnrollNum(10)));
//        RollDataConvertor rollDataConvertor = SpringContextUtil.getBean(RollDataConvertorImpl.class);
//        System.out.println(rollDataConvertor.getClass());
        IdUtil idUtil = SpringContextUtil.getBean("IdUtil");
        RollStatisticsMapper rollStatisticsMapper = SpringContextUtil.getBean("RollStatisticsMapper");
    }

    @Resource
    private CommonProperties commonProperties;

    @Test
    public void testClassTime(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + commonProperties.ClassTime(TimeUnit.MINUTE));
        System.out.println(timestamp);
    }

    @Test
    public void testPattern(){
        String regex = "[0-9]{2}";
        for (String s : commonProperties.getArrangement()) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(s);
//            while (matcher.find()){
//                System.out.println(matcher.group());
//            }
            System.out.println(matcher.group());
            System.out.println(matcher.group());
            System.out.println(matcher.group());
            System.out.println(matcher.group());
//            if (matcher.find()) System.out.println(matcher.group());
//            if (matcher.find()) System.out.println(matcher.group());

        }
//        List<String> arrangement = commonProperties.getArrangement();
//        String regex = "\\[[^0-9]{2}:[^0-9]{2}]";
//        Pattern pattern = Pattern.compile(regex);
//        for (String s : arrangement) {
//            Matcher matcher = pattern.matcher(s);
//            matcher.find();
//            System.out.println(matcher.group());
//        }
    }
    @Test
    public void getDayTimeMap(){
//        Map<CommonProperties.ClassTime, Period> dayTimeMap = commonProperties.DayTimeMap();
//        for (CommonProperties.ClassTime classTime : dayTimeMap.keySet()) {
//            System.out.println(classTime);
//            System.out.println(dayTimeMap.get(classTime));
//        }
    }

    @Test
    public void testGetPeriod(){
        int hour;
        int minute;
        for (hour = 8;hour<=21;++hour){
            for (minute=0;minute<60;minute+=10) System.out.println(String.format("%02d:%02d 属于 %s", hour, minute, commonProperties.getPeriod(hour, minute).getMsg()));
        }
    }
    @Resource
    private CourseArrangementMapper courseArrangementMapper;
    @Resource
    private DateUtil dateUtil;
    @Test
    public void testSelectCourseArrangement(){
        LambdaQueryWrapper<CourseArrangement> wrapper = new LambdaQueryWrapper<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        wrapper.eq(CourseArrangement::getCourseId,"55414535")
                .eq(CourseArrangement::getPeriod,commonProperties.getPeriod(calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE)))
                .eq(CourseArrangement::getWeekDay,dateUtil.getWeekDay(new Date(System.currentTimeMillis())));
        System.out.println(courseArrangementMapper.selectOne(wrapper));
    }

    @Resource
    private LeaveRelationMapper leaveRelationMapper;
    @Resource
    private StudentMapper studentMapper;
    @Test
    public void testLeaveMapper(){
        LeaveRelation leaveRelation = new LeaveRelation()
                .setId(idUtil.getId())
                .setTransactorId("id")
                .setTransactorName("name")
                .setCreated(new Timestamp(System.currentTimeMillis()))
                .setStudentId("1")
                .setStudentName("nick")
                .setExcuse("督导队员标记为请假")
                .setStartTime(new Date(System.currentTimeMillis()))
                .setEndTime(new Date(System.currentTimeMillis()+commonProperties.ClassTime(TimeUnit.MINUTE)))
                .setResult(1);
        leaveRelationMapper.insert(leaveRelation);
//        Student student = new Student().setId(idUtil.getId())
//                .setStudentName("nick");
//        studentMapper.insert(student);
//        LeaveRelation leaveRelation = new LeaveRelation().setId(idUtil.getId()).setStudentId("100000001");
//        leaveRelationMapper.insert(leaveRelation);
    }

    @Test
    public void testGetPeriod1(){
        /*生成课程时段信息*/
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Period period = commonProperties.getPeriod(calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE));
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println(calendar.get(Calendar.MINUTE));
        System.out.println(period);
    }

}
