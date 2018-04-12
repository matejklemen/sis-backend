package beans.logic;

import beans.crud.*;
import entities.*;
import entities.curriculum.Curriculum;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

@ApplicationScoped
public class StudentImportBean {

    private static Integer nextStudentId = null;

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Inject
    private StudentBean sdb;
    @Inject
    private UserLoginBean ulb;
    @Inject
    private UserRoleBean urb;
    @Inject
    private StudyProgramBean spb;
    @Inject
    private StudyYearBean syb;
    @Inject
    private EnrolmentBean enb;
    @Inject
    private CurriculumBean curB;

    public List<Student> ParseStudentData(String studentData){
        List<Student> listOfStudents = new ArrayList<>();

        GenerateNewStudentId();
        int pos = 0;
        while(pos < studentData.length()){
            Student stu = new Student();
            stu.setName(studentData.substring(pos, (pos = pos+30)).replaceAll("\\s+",""));
            stu.setSurname(studentData.substring(pos, (pos = pos+30)).replaceAll("\\s+",""));
            stu.setGender('-');

            EnrolmentToken enr = new EnrolmentToken();
            enr.setStudent(stu);
            enr.setYear(1);
            StudyProgram studyProgram = spb.getStudyProgramByEvsCode(Integer.parseInt(studentData.substring(pos, (pos = pos+7))));
            enr.setStudyProgram(studyProgram);
            enr.setStudyYear(syb.getOrCreateStudyYear("2017/2018"));
            enr.setUsed(true);
            enr.setKind(enr.getKind());
            enr.setType(enr.getType());

            stu.setRegisterNumber(GenerateNewStudentId());
            stu.setEmail(studentData.substring(pos, (pos = pos+60)).replaceAll("\\s+",""));

            UserLogin ul = new UserLogin();
            ul.setUsername(GenerateUsername(stu));
            ul.setPassword(GeneratePassword(ul, stu));
            ul.setRole(urb.getRoleByName("Student"));

            ul = ulb.insertUserLoginSingle(ul.getUsername(), ul.getPassword(), ul.getRole());

            stu.setLoginData(ul);

            stu = sdb.putStudent(stu);

            // enrolment has to be persisted after student ?
            List<Curriculum> lCurriculum = curB.getFirstYearCourses("2017/2018", studyProgram.getId());
            List<Integer> lCourses = new ArrayList<>();
            Iterator<Curriculum> lCurriculumIterator = lCurriculum.iterator();
            while (lCurriculumIterator.hasNext()) {
                lCourses.add(lCurriculumIterator.next().getIdCourse().getId());
            }

            Enrolment en = enb.putEnrolment(enr, lCourses);

            stu = sdb.updateStudent(stu);

            listOfStudents.add(stu);
        }

        return listOfStudents;
    }

    private String GenerateNewStudentId(){
        if(nextStudentId == null){
            Student lastStudent = sdb.getLastStudent();
            if(lastStudent != null){
                nextStudentId = parseInt(lastStudent.getRegisterNumber());
            }
            else{
                int yearLastTwo = Calendar.getInstance().get(Calendar.YEAR) % 100;
                nextStudentId = 63000000 + (yearLastTwo * 10000);
            }
            log.info("Got last student register number: "+nextStudentId);
        }
        nextStudentId++;
        return String.valueOf(nextStudentId);
    }

    private String GeneratePassword(UserLogin ul, Student sd){
        String prefix = ul.getUsername().substring(0,2);
        return prefix + "_" + sd.getRegisterNumber();
    }

    private String GenerateUsername(Student sd){
        String prefix = sd.getName().substring(0,1) + sd.getSurname().substring(0,1);
        String[] idNumbers = sd.getRegisterNumber().split("");
        String uniqueId = idNumbers[3] + idNumbers[5] + idNumbers[6] + idNumbers[7];
        String suffix = "@student.uni-lj.si";
        return prefix.toLowerCase()  + uniqueId + suffix;
    }
}
