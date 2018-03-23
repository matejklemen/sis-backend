package beans.logic;

import beans.crud.StudentDataBean;
import beans.crud.UserLoginBean;
import entities.StudentData;
import entities.UserLogin;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentImport {

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    private static  int nextStudentId = 0;

    @Inject
    private StudentDataBean sdb;

    @Inject
    private UserLoginBean ulb;


    public void ParseStudentData(String studentData){
        List<String> lines = Arrays.asList(studentData.split(System.getProperty("line.separator")));
        Iterator<String> iter = lines.iterator();

        while(iter.hasNext()){
            StudentData sd = new StudentData();
            sd.setName(iter.next());
            sd.setSurname(iter.next());
            sd.setCourse(iter.next());
            sd.setEmail(iter.next());
            sd.setStudentId(GenerateNewStudentId());

            UserLogin ul = new UserLogin();
            ul.setUsername(sd.getEmail());
            ul.setPassword(GeneratePassword(sd));
            ul.setRole("student");
            log.info("dve");
            //ul = ulb.putUserLogin(ul);
            log.info("deluje");
            sd.setLoginId(123);
            log.info("dve + "+sdb.toString());
            sdb.putStudent(sd);
        }
    }

    private String GenerateNewStudentId(){
        nextStudentId++;
        return String.valueOf(63150000 + nextStudentId);
    }

    private String GeneratePassword(StudentData sd){
        String prefix = sd.getEmail().split("@")[0];
        return prefix + "_" + sd.getStudentId();
    }
}
