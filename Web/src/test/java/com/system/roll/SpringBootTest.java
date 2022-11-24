package com.system.roll;

import com.system.roll.context.common.CommonContext;
import com.system.roll.entity.pojo.Course;
import com.system.roll.entity.vo.Result;
import com.system.roll.excel.annotation.Excel;
import com.system.roll.excel.uitl.ExcelUtil;
import com.system.roll.mapper.CourseMapper;
import com.system.roll.mapper.DepartmentMapper;
import com.system.roll.mapper.MajorMapper;
import com.system.roll.oss.OssHandler;
import com.system.roll.oss.OssResource;
import com.system.roll.redis.CourseRedis;
import com.system.roll.redis.StudentRedis;
import com.system.roll.utils.HttpRequestUtil;
import com.system.roll.utils.IdUtil;
import com.system.roll.utils.PinyinUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
}
