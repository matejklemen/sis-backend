package beans.logic;

import beans.crud.*;
import entities.Enrolment;
import entities.Student;
import entities.UserLogin;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

@ApplicationScoped
public class StudentImport {

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

    public List<Student> ParseStudentData(String studentData){
        List<String> lines = Arrays.asList(studentData.split(System.getProperty("line.separator")));
        Iterator<String> iter = lines.iterator();

        List<Student> listOfStudents = new ArrayList<>();
        GenerateNewStudentId();
        while(iter.hasNext()){
            Student stu = new Student();
            stu.setName(iter.next());
            stu.setSurname(iter.next());

            Enrolment enr = new Enrolment();
            enr.setStudent(stu);
            enr.setYear(1);
            enr.setStudyProgram(spb.getStudyProgram(iter.next()));
            enr.setStudyYear(syb.getOrCreateStudyYear("2017/2018"));
            enr.setConfirmed(false);
            enr.setKind("redni");
            enr.setType("prvi vpis");

            stu.setRegisterNumber(GenerateNewStudentId());
            stu.setEmail(iter.next());

            UserLogin ul = new UserLogin();
            ul.setUsername(GenerateUsername(stu));
            ul.setPassword(GeneratePassword(ul, stu));
            ul.setRole(urb.getRoleByName("Student"));

            ul = ulb.insertUserLoginSingle(ul.getUsername(), ul.getPassword(), ul.getRole());

            stu.setLoginData(ul);

            stu = sdb.putStudent(stu);

            // enrolment has to be persisted after student ?
            enr = enb.putEnrolment(enr);

            // update enrolment list...
            stu.getEnrolments().add(enr);
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
