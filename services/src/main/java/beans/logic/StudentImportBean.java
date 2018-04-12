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
    private CandidateBean cb;

    public List<Candidate> ParseStudentData(String studentData){
        List<Candidate> listOfCandidates = new ArrayList<>();

        GenerateNewStudentId();
        int pos = 0;
        while(pos < studentData.length()){

            // Create new candidate
            Candidate can = new Candidate();
            can.setName(studentData.substring(pos, (pos = pos+30)).replaceAll("\\s+",""));
            can.setSurname(studentData.substring(pos, (pos = pos+30)).replaceAll("\\s+",""));
            can.setStudyProgram(spb.getStudyProgramByEvsCode(Integer.parseInt(studentData.substring(pos, (pos = pos+7)))));
            can.setRegisterNumber(GenerateNewStudentId());
            can.setEmail(studentData.substring(pos, (pos = pos+60)).replaceAll("\\s+",""));

            // Register user for login
            UserLogin ul = new UserLogin();
            ul.setUsername(GenerateUsername(can));
            ul.setPassword(GeneratePassword(ul, can));
            ul.setRole(urb.getRoleByName("Student"));

            ul = ulb.insertUserLoginSingle(ul.getUsername(), ul.getPassword(), ul.getRole());
            can.setLoginData(ul);
            cb.putCandidate(can);

            listOfCandidates.add(can);
        }

        return listOfCandidates;
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

    private String GeneratePassword(UserLogin ul, Candidate can){
        String prefix = ul.getUsername().substring(0,2);
        return prefix + "_" + can.getRegisterNumber();
    }

    private String GenerateUsername(Candidate can){
        String prefix = can.getName().substring(0,1) + can.getSurname().substring(0,1);
        String[] idNumbers = can.getRegisterNumber().split("");
        String uniqueId = idNumbers[3] + idNumbers[5] + idNumbers[6] + idNumbers[7];
        String suffix = "@student.uni-lj.si";
        return prefix.toLowerCase()  + uniqueId + suffix;
    }
}
