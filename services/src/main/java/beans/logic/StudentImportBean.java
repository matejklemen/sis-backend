package beans.logic;

import beans.crud.StudentBean;
import beans.crud.StudyProgramBean;
import beans.crud.UserLoginBean;
import beans.crud.UserRoleBean;
import entities.Student;
import entities.UserLogin;
import utils.Pair;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

@ApplicationScoped
public class StudentImportBean {

    private static Integer nextStudentId = null;

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Inject private StudentBean sdb;
    @Inject private UserLoginBean ulb;
    @Inject private UserRoleBean urb;
    @Inject private StudyProgramBean spb;

    public Pair<List<Student>,List<Student>> ParseStudentData(String studentData){
        List<Student> listOfCandidates = new ArrayList<>();
        List<Student> listOfRejected = new ArrayList<>();

        GenerateNewStudentId();
        int pos = 0;
        while(pos < studentData.length()){

            // Create new student
            Student stu = new Student();
            stu.setName(studentData.substring(pos, (pos = pos+30)).replaceAll("\\s+",""));
            stu.setSurname(studentData.substring(pos, (pos = pos+30)).replaceAll("\\s+",""));
            stu.setStudyProgram(spb.getStudyProgram(studentData.substring(pos, (pos = pos+7))));
            stu.setRegisterNumber(GenerateNewStudentId());
            stu.setEmail(studentData.substring(pos, (pos = pos+60)).replaceAll("\\s+",""));

            // Check if Student with this data already exists
            if(sdb.hasStudent(stu)){
                listOfRejected.add(stu);
                continue;
            }

            // Register user for login
            UserLogin ul = new UserLogin();
            ul.setUsername(GenerateUsername(stu));
            ul.setPassword(GeneratePassword(ul, stu));
            ul.setRole(urb.getRoleById(2));

            ul = ulb.insertUserLoginSingle(ul.getUsername(), ul.getPassword(), ul.getRole());
            stu.setLoginData(ul);
            sdb.putStudent(stu);

            listOfCandidates.add(stu);
        }

        return new Pair<>(listOfCandidates,listOfRejected);
    }

    /* Helpers */
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

    private String GeneratePassword(UserLogin ul, Student can){
        String prefix = ul.getUsername().substring(0,2);
        return prefix + "_" + can.getRegisterNumber();
    }

    private String GenerateUsername(Student can){
        String prefix = can.getName().substring(0,1) + can.getSurname().substring(0,1);
        String[] idNumbers = can.getRegisterNumber().split("");
        String uniqueId = idNumbers[3] + idNumbers[5] + idNumbers[6] + idNumbers[7];
        String suffix = "@student.uni-lj.si";
        return prefix.toLowerCase()  + uniqueId + suffix;
    }
}
