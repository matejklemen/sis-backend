package beans.logic;

import beans.crud.StudentBean;
import beans.crud.UserLoginBean;
import beans.crud.UserRoleBean;
import entities.StudyProgram;
import entities.UserLogin;
import entities.Student;
import entities.UserRole;
import pojo.StudentProfile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@ApplicationScoped
public class StudentImport {

    private static  int nextStudentId = 0;

    @Inject
    private StudentBean sdb;

    @Inject
    private UserLoginBean ulb;

    @Inject
    private UserRoleBean urb;


    public List<StudentProfile> ParseStudentData(String studentData){
        List<String> lines = Arrays.asList(studentData.split(System.getProperty("line.separator")));
        Iterator<String> iter = lines.iterator();

        List<StudentProfile> listOfStudents = new ArrayList<>();

        while(iter.hasNext()){
            Student sd = new Student();
            sd.setName(iter.next());
            sd.setSurname(iter.next());
            sd.setStudyProgram(new StudyProgram()); iter.next();
            sd.setRegisterNumber(GenerateNewStudentId());
            sd.setEmail(iter.next());

            UserLogin ul = new UserLogin();
            ul.setUsername(GenerateUsername(sd));
            ul.setPassword(GeneratePassword(ul, sd));
            ul.setRole(urb.getRoleByName("Student"));

            ul = ulb.insertUserLoginSingle(ul.getUsername(), ul.getPassword(), ul.getRole());

            sd.setLoginData(ul); //setLoginId(ul.getId());
            sdb.putStudent(sd);

            listOfStudents.add(StudentProfile.setStudentProfile(sd, ul, GeneratePassword(ul,sd)));
        }

        return listOfStudents;
    }

    private String GenerateNewStudentId(){
        nextStudentId++;
        return String.valueOf(63150000 + nextStudentId);
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
