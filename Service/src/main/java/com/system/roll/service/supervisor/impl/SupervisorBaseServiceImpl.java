package com.system.roll.service.supervisor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.constant.impl.*;
import com.system.roll.entity.dto.student.CourseDto;
import com.system.roll.entity.dto.student.InfoDto;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.course.CourseVo;
import com.system.roll.entity.vo.student.InfoVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import com.system.roll.excel.annotation.Excel;
import com.system.roll.excel.uitl.ExcelUtil;
import com.system.roll.handler.mapstruct.StudentConvertor;
import com.system.roll.handler.mapstruct.SupervisorConvertor;
import com.system.roll.mapper.*;
import com.system.roll.oss.OssHandler;
import com.system.roll.redis.CourseRedis;
import com.system.roll.redis.StudentRedis;
import com.system.roll.service.auth.WxApiService;
import com.system.roll.service.supervisor.SupervisorBaseService;
import com.system.roll.utils.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component(value = "SupervisorBaseService")
public class SupervisorBaseServiceImpl implements SupervisorBaseService {

    @Resource
    private DateUtil dateUtil;

    @Resource
    private IdUtil idUtil;

    @Resource
    private RollRelationMapper rollRelationMapper;

    @Resource
    private CourseArrangementMapper courseArrangementMapper;

    @Resource
    private DeliveryMapper deliveryMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private ProfessorMapper professorMapper;

    @Resource
    private CourseRelationMapper courseRelationMapper;

    @Resource
    private EnumUtil enumUtil;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private ExcelUtil excelUtil;

    @Resource
    private SupervisorConvertor supervisorConvertor;

    @Resource(name = "StudentRedis")
    private StudentRedis studentRedis;


    @Resource(name = "WxApiService")
    private WxApiService wxApiService;

    @Resource
    private OssHandler ossHandler;

    @Resource
    private StudentConvertor studentConvertor;

    @Resource
    private FileUtil fileUtil;

    @Resource(name = "CourseRedis")
    private CourseRedis courseRedis;

    @Resource
    private PinyinUtil pinyinUtil;

    @Override
    public SupervisorVo getSupervisorInfo(String openId) {
        /*先到学生表中查询*/
        Student student = studentMapper.selectByOpenId(openId);
        /*不在学生表中*/
        SupervisorVo supervisorVo;
        if (student==null){
            /*到教师表中查询*/
            Professor professor = professorMapper.selectByOpenId(openId);
            if (professor==null) return null;
            supervisorVo = supervisorConvertor.professorToSupervisorVo(professor);
        }else {
            /*更新角色*/
            if (student.getRole().equals(Role.STUDENT)) studentMapper.updateRole(student.getId(),Role.SUPERVISOR);
            supervisorVo = supervisorConvertor.studentToSupervisorVo(student);
        }
        supervisorVo
                .setCurrentWeek(dateUtil.getWeek(new Date(System.currentTimeMillis())))
                .setDepartmentName(departmentMapper.selectNameById(supervisorVo.getDepartmentId()));
        return supervisorVo;
    }

    @Override
    public CourseListVo getAllCourse() {
        String studentId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        Integer currentWeek = dateUtil.getWeek(new Date(System.currentTimeMillis()));
        Integer currentDay = dateUtil.getWeekDay(new Date(System.currentTimeMillis()));

        List<CourseVo> courses = new ArrayList<>();

        boolean isOdd = currentWeek % 2 == 1;
        // 1.查出该督导员督导哪些课程
        LambdaQueryWrapper<RollRelation> crqw = new LambdaQueryWrapper<>();
        crqw.eq(RollRelation::getSupervisorId,studentId);
        List<RollRelation> rollRelations = rollRelationMapper.selectList(crqw);
        List<String> courseIds = rollRelations.stream().map(RollRelation::getCourseId).collect(Collectors.toList());

        // 2.查出该督导员当天需要点名的课程信息
        LambdaQueryWrapper<CourseArrangement> caqw = new LambdaQueryWrapper<>();
        caqw.in(CourseArrangement::getCourseId,courseIds)
                .eq(CourseArrangement::getWeekDay,currentDay)
                .in(CourseArrangement::getMode,isOdd ? TeachingMode.ODD_SINGLE_WEEK : TeachingMode.EVEN_SINGLE_WEEK,TeachingMode.EVERY_WEEK);

        List<CourseArrangement> courseArrangements = courseArrangementMapper.selectList(caqw);

        // 3.将数据封装成vo对象
        LambdaQueryWrapper<Delivery> dqw = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Course> cqw = new LambdaQueryWrapper<>();
        for (CourseArrangement item : courseArrangements) {
            dqw.clear();
            cqw.clear();

            cqw.eq(Course::getId,item.getCourseId())
                    .le(Course::getStartWeek,currentWeek)
                    .ge(Course::getEndWeek,currentWeek);

            Course course = courseMapper.selectOne(cqw);
            if (course==null) continue;
            dqw.eq(Delivery::getCourseId,item.getCourseId());
            List<Delivery> deliveries = deliveryMapper.selectList(dqw);
            List<String> professorNames = deliveries.stream().map(Delivery::getProfessorName).collect(Collectors.toList());
            CourseVo courseVo = new CourseVo();
            courseVo.setEndWeek(course.getEndWeek())
                    .setStartWeek(course.getStartWeek())
                    .setPeriod(item.getPeriod().getMsg())
                    .setId(item.getCourseId())
                    .setName(course.getCourseName())
                    .setProfessorName(StringUtils.join(professorNames,','));

            courses.add(courseVo);
        }

        return new CourseListVo(courses,courses.size());
    }

    @Override
    @Transactional
    @Operation(type = OperationType.DELETE_COURSE)
    public void deleteCourse(String courseId) {
        if (courseMapper.selectById(courseId)==null) throw new ServiceException(ResultCode.RESOURCE_NOT_FOUND);
        courseRedis.deleteCourseName(courseId);
        Map<String, Object> map = new HashMap<>();
        map.put("course_id",courseId);
        courseArrangementMapper.deleteByMap(map);
        deliveryMapper.deleteByMap(map);
        Course course = courseMapper.selectById(courseId);
        ossHandler.deleteFile(course.getAttachment());
        courseRelationMapper.deleteByMap(map);
        rollRelationMapper.deleteByMap(map);
        courseMapper.deleteById(courseId);
    }

    @Override
    @Transactional
    @Operation(type = OperationType.UPLOAD_COURSE)
    public CourseVo uploadCourse(CourseDto courseDto) {
        /*获取督导人员的id*/
        String supervisorId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        //String supervisorId = "1";
        /*参数检查*/
        List<String> courseArrangements = courseDto.getCourseArrangements();
        if(courseArrangements.isEmpty()){
            throw new ServiceException(ResultCode.PARAM_NOT_MATCH);
        }
        /*导入学生信息*/
        List<StudentInfo> studentInfos;
        try {
            MultipartFile studentList = courseDto.getStudentList();
            String excelName = studentList.getOriginalFilename();
            String suffix = Objects.requireNonNull(excelName).substring(excelName.lastIndexOf("."));
            ExcelUtil.ExcelType excelType = enumUtil.getEnumByDescription(ExcelUtil.ExcelType.class, suffix);
            studentInfos = excelUtil.importExcel(StudentInfo.class, studentList.getInputStream(), excelType);
        } catch (IOException e) {
            throw new ServiceException(ResultCode.FAILED_TO_IMPORT_EXCEL);
        }

        /*分配课程id*/
        String courseId = idUtil.getId();
//        String courseId = org.springframework.util.StringUtils.hasText(courseDto.getId())?courseDto.getId():idUtil.getId();
        Course course = new Course().setId(courseId);

        /*保存点名表单*/
        if (courseDto.getStudentList()!=null){
            String attachment = ossHandler.postFile(courseDto.getStudentList(), "excel", courseId);
            course.setAttachment(attachment);
        }

        /*组装pojo对象*/
        course.setCourseName(courseDto.getCourseName())
                .setStartWeek(courseDto.getStartWeek())
                .setEndWeek(courseDto.getEndWeek())
                .setEnrollNum(studentInfos.size());
        // 插入课程
        courseMapper.insert(course);
        /*记录courseId和courseName的映射*/
        courseRedis.saveCourseName(course.getId(),course.getCourseName());

        /*遍历学生信息，插入学生关系表*/
        for (StudentInfo studentInfo : studentInfos){
            /*根据学生姓名生成拼音，并进行保存*/
            if (studentInfo.getId()==null||studentInfo.getId().equals("")||studentInfo.getName()==null||studentInfo.getName().equals("")){
                log.warn("有异常的学生关系项：{}",studentInfo.toString());
                continue;
            }
            /*将学号补上前导0*/
            studentInfo.setId(String.format("%9s",studentInfo.getId()).replace(" ","0"));
            System.out.println(studentInfo);
            studentRedis.savePinYin(studentInfo.getId(), pinyinUtil.toPinyin(studentInfo.getName()));
            /*插入学生关系表*/
            courseRelationMapper.insert(new CourseRelation().setId(idUtil.getId()).setStudentName(studentInfo.getName()).setCourseId(courseId).setStudentId(studentInfo.getId()));
        }

        // 插入点名关系表
        rollRelationMapper.insert(new RollRelation()
                .setId(idUtil.getId())
                .setSupervisorId(supervisorId)
                .setCourseId(course.getId()));

        /*导入授课关系*/
        LambdaQueryWrapper<Professor> pqw = new LambdaQueryWrapper<>();
        pqw.clear();
        String[] professorNames = courseDto.getProfessorName().split(",");
        for(String professorName: professorNames){
            pqw.eq(Professor::getProfessorName,professorName);
            Professor professor = professorMapper.selectOne(pqw);
            /*若暂无教授的记录，则系统临时分配一条记录*/
            if(ObjectUtils.isEmpty(professor)){
                professor = new Professor();
                professor.setId(idUtil.getId())
                        .setProfessorName(professorName)
                        .setRole(Role.PROFESSOR);
                professorMapper.insert(professor);
            }
            // 插入授课情况
            Delivery delivery = new Delivery();
            delivery.setId(idUtil.getId())
                    .setCourseId(courseId)
                    .setProfessorName(professorName)
                    .setProfessorId(professor.getId());
            deliveryMapper.insert(delivery);
        }

        /*导入课程安排*/
        for (String courseArrangement:courseArrangements){
            // 拆分字符串
            String[] split = courseArrangement.split(" ");
            // 插入课程安排表
            CourseArrangement arrangement = new CourseArrangement();
            arrangement.setId(idUtil.getId())
                    .setCourseId(course.getId())
                    .setClassroomNo(courseDto.getClassroomNo())
                    .setWeekDay(Integer.parseInt(split[0]))
                    .setPeriod(enumUtil.getEnumByCode(Period.class,Integer.parseInt(split[1])))
                    .setMode(enumUtil.getEnumByCode(TeachingMode.class,Integer.parseInt(split[2])));
            courseArrangementMapper.insert(arrangement);
        }
        /*封装返回结果*/
        CourseVo courseVo = new CourseVo();
        courseVo.setId(course.getId()).setName(course.getCourseName());
        return courseVo;
    }

    @Override
    @Operation(type = OperationType.UPDATE_COURSE)
    public CourseVo updateCourse(CourseDto courseDto) {
        String courseId = courseDto.getId();
        if (courseMapper.selectById(courseId)==null) throw new ServiceException(ResultCode.RESOURCE_NOT_FOUND);
        if (courseDto.getStudentList()==null){
            try {
                Course course = courseMapper.selectById(courseId);
                InputStream inputStream = ossHandler.getFile(course.getAttachment()).getInputStream();
                MultipartFile multipartFile = fileUtil.getMultipartFile(inputStream, course.getAttachment());
                courseDto.setStudentList(multipartFile);
            } catch (IOException e) {
                throw new ServiceException(ResultCode.RESOURCE_NOT_FOUND);
            }
        }
        deleteCourse(courseId);
        return uploadCourse(courseDto);
    }

    @Override
    @Transactional
    public InfoVo register(InfoDto infoDto) {
        /*获取用户的openId*/
        String openId = wxApiService.jsCode2session(infoDto.getCode()).getOpenId();

        /*检查用户是否为空*/
        if (studentMapper.selectByOpenId(openId)!=null) throw new ServiceException(ResultCode.USER_ALREADY_EXISTS);
        /*构造pojo对象*/
        Student student = studentConvertor.infoDtoToStudent(infoDto);
        /*查询院系id*/
        String departmentId = departmentMapper.selectIdByNameAndGrade(infoDto.getDepartmentName(),infoDto.getGrade());
        /*查询专业id*/
        String majorId = majorMapper.selectIdByName(infoDto.getMajorName());

        student.setOpenId(openId)
                .setRole(Role.SUPERVISOR)
                .setDepartmentId(departmentId)
                .setMajorId(majorId);

        studentRedis.saveName(student.getId(),student.getStudentName());
        studentMapper.insert(student);
        /*组装视图对象*/
        InfoVo infoVo = studentConvertor.studentToInfoVo(student);
        infoVo.setDepartmentName(infoDto.getDepartmentName())
                .setMajorName(infoDto.getMajorName())
                .setCurrentWeek(dateUtil.getWeek(new Date(System.currentTimeMillis())));

        return infoVo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class StudentInfo{
        @Excel(value = "学号")
        private String id;

        @Excel(value = "姓名")
        private String name;
    }

}
