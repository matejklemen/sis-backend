package beans.logic;

import beans.crud.StudentDataBean;
import beans.crud.UserLoginBean;
import entities.StudentData;
import entities.UserLogin;
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
    private StudentDataBean sdb;

    @Inject
    private UserLoginBean ulb;


    public List<StudentProfile> ParseStudentData(String studentData){
        List<String> lines = Arrays.asList(studentData.split(System.getProperty("line.separator")));
        Iterator<String> iter = lines.iterator();

        List<StudentProfile> listOfStudents = new ArrayList<>();

        while(iter.hasNext()){
            StudentData sd = new StudentData();
            sd.setName(iter.next());
            sd.setSurname(iter.next());
            sd.setCourse(iter.next());
            sd.setStudentId(GenerateNewStudentId());
            sd.setEmail(iter.next());


            UserLogin ul = new UserLogin();
            ul.setUsername(GenerateUsername(sd));
            ul.setPassword(GeneratePassword(ul, sd));
            ul.setRole("student");

            ul = ulb.insertUserLoginSingle(ul.getUsername(), ul.getPassword(), ul.getRole());

            sd.setLoginId(ul.getId());
            sdb.putStudent(sd);

            listOfStudents.add(StudentProfile.setStudentProfile(sd, ul, GeneratePassword(ul,sd)));
        }

        return listOfStudents;
    }

    private int GenerateNewStudentId(){
        nextStudentId++;
        return 63150000 + nextStudentId;
    }

    private String GeneratePassword(UserLogin ul, StudentData sd){
        String prefix = ul.getUsername().substring(0,2);
        return prefix + "_" + sd.getStudentId();
    }

    private String GenerateUsername(StudentData sd){
        String prefix = sd.getName().substring(0,1) + sd.getSurname().substring(0,1);
        String[] idNumbers = Integer.toString(sd.getStudentId()).split("");
        String uniqueId = idNumbers[3] + idNumbers[5] + idNumbers[6] + idNumbers[7];
        String suffix = "@student.uni-lj.si";
        return prefix.toLowerCase()  + uniqueId + suffix;
    }
}
