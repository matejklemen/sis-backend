package Beans.Logic;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import Beans.CRUD.StudentDataBean;
import Beans.CRUD.UserLoginBean;
import entities.StudentData;
import entities.UserLogin;

@ApplicationScoped
public class StudentImport {

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    private static  int nextStudentId = 0;

    private StudentDataBean sdb = new StudentDataBean();

    private UserLoginBean ulb = new UserLoginBean();


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

    private int GenerateNewStudentId(){
        nextStudentId++;
        return 63150000 + nextStudentId;
    }

    private String GeneratePassword(StudentData sd){
        String prefix = sd.getEmail().split("@")[0];
        return prefix + "_" + sd.getStudentId();
    }
}
