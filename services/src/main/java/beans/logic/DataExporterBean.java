package beans.logic;

import beans.crud.CourseOrganizationBean;
import beans.crud.EnrolmentBean;
import beans.crud.ExamSignUpBean;
import beans.crud.StudentCoursesBean;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import entities.Enrolment;
import entities.Student;
import entities.StudyProgram;
import entities.curriculum.Course;
import entities.curriculum.CourseOrganization;
import entities.curriculum.ExamSignUp;
import entities.curriculum.StudentCourses;
import entities.logic.TableData;
import javafx.scene.control.Tab;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DataExporterBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private  final String CHARSET = "UTF8";
    private final String DELIMETER = ";";
    private  final String NEW_LINE = "\r\n";

    private final BaseFont base = BaseFont.createFont("arial.ttf", "Cp1250", BaseFont.NOT_EMBEDDED);
    private final Font font1 = new Font(base, 12);
    private final Font font2 = new Font(base, 10, Font.NORMAL, BaseColor.GRAY);
    private final Font font3 = new Font(base, 10);
    private final Font font4 = new Font(base, 10, Font.BOLD);
    private final Font font5 = new Font(base, 8, Font.BOLDITALIC);

    private final List<String> indexHeader = Arrays.asList("#","Šifra","Predmet","KT","Predavatelj(i)","Datum","Ocena","Polaganja");

    @Inject
    EnrolmentBean eB;

    @Inject
    StudentCoursesBean scB;

    @Inject
    ExamSignUpBean esuB;

    @Inject
    CourseOrganizationBean coB;

    public DataExporterBean() throws IOException, DocumentException {
    }

    public ByteArrayInputStream generateTablePdf(TableData tableData){
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            header.addCell(createHeader(tableData.getHead()));
            PdfPTable legend = createLegend(tableData.getInLegend(), tableData.getColoumnNames());
            PdfPCell cell = new PdfPCell();
            if(legend != null) {
                cell.addElement(legend);
            }
            cell.setBorder(PdfPCell.NO_BORDER);
            header.addCell(cell);
            document.add(header);

            PdfPTable table = new PdfPTable(tableData.getColoumnNames().size());
            float[] columnWidths = getOptimizedTableWidths(tableData.getColoumnNames(), tableData.getRows(), tableData.getInLegend());
            table.setWidths(columnWidths);
            table.setWidthPercentage(100);

            addTableHeader(table, font3, tableData.getColoumnNames(), tableData.getInLegend());
            addRows(table, font3, tableData.getRows());

            document.add(table);

            document.close();

            byte[] pdf = baos.toByteArray();

            PdfReader reader = new PdfReader(pdf);
            int pages = reader.getNumberOfPages();

            ByteArrayOutputStream pagedPdf = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, pagedPdf);
            PdfContentByte pageContent;
            for(int i=1; i<=pages; i++) {
                pageContent = stamper.getOverContent(i);
                ColumnText.showTextAligned(pageContent, Element.ALIGN_RIGHT, new Phrase(String.format("%d / %d", i, pages), font3), pageContent.getPdfDocument().right(), pageContent.getPdfDocument().bottom() - 16, 0);
            }
            stamper.close();
            reader.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(pagedPdf.toByteArray());
            return bais;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ByteArrayInputStream generateTableCsv(TableData tableData){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);

            try {
                Iterator<String> namesIt = tableData.getColoumnNames().iterator();
                while (namesIt.hasNext()) {
                    out.write(namesIt.next().getBytes(CHARSET));
                    if(namesIt.hasNext()) {
                        out.write(DELIMETER.getBytes(CHARSET));
                    } else {
                        out.write(NEW_LINE.getBytes(CHARSET));
                    }
                }
                Iterator<List<String>> rowIt = tableData.getRows().iterator();
                while (rowIt.hasNext()) {
                    Iterator<String> cellIt = rowIt.next().iterator();
                    while (cellIt.hasNext()) {
                        out.write(cellIt.next().getBytes(CHARSET));
                        if(cellIt.hasNext()) {
                            out.write(DELIMETER.getBytes(CHARSET));
                        } else {
                            out.write(NEW_LINE.getBytes(CHARSET));
                        }
                    }
                }
                baos.flush();
                baos.close();
            } catch (Exception e) {
                log.severe(e.getMessage());
                return null;
            }
            byte[] csv = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(csv);
            return bais;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return null;
        }
    }

    public ByteArrayInputStream generateEnrolmentSheet(int studentId){
        Enrolment enrolment = eB.getLastEnrolmentByStudentId(studentId);
        if(enrolment == null) {
            return null;
        }
        Enrolment firstEnrolment = eB.getFirstEnrolmentByStudentIdAndProgram(studentId, enrolment.getStudyProgram().getId());
        List<StudentCourses> studentCourses = scB.getStudentCoursesByEnrolmentId(enrolment.getId());
        try{
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();
            cell.addElement(new Paragraph("VPISNI LIST "+enrolment.getStudyYear().getName(),font1));
            cell.addElement(new Paragraph("za študente",font2));
            cell.addElement(new Paragraph("Fakulteta za računalništvo in informatiko",font2));
            cell.setPadding(0);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
            PdfReader reader = new PdfReader("services/src/main/resources/logo-ul.pdf");
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            cell = new PdfPCell(Image.getInstance(page));
            cell.setPadding(0);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);

            addSpacing("", table, 3, font1);

            table.addCell(getCell("Vpisna številka:", enrolment.getStudent().getRegisterNumber(), PdfPCell.ALIGN_LEFT, font1, font2));
            table.addCell(getCell("Priimek in ime:", enrolment.getStudent().getSurname() +" "+ enrolment.getStudent().getName(), PdfPCell.ALIGN_CENTER, font1, font2));
            table.addCell(getCell("Datum rojstva:", enrolment.getStudent().getDateOfBirth().toString(), PdfPCell.ALIGN_RIGHT, font1, font2));

            table.addCell(getCell("Kraj rojstva:", enrolment.getStudent().getPlaceOfBirth(), PdfPCell.ALIGN_LEFT, font1, font2));
            table.addCell(getCell("Država, občina rojstva:", enrolment.getStudent().getCountryOfBirth().getName() +", "+ enrolment.getStudent().getMunicipalityOfBirth().getName(), PdfPCell.ALIGN_CENTER, font1, font2));
            table.addCell(getCell("Državljanstvo:", enrolment.getStudent().getCitizenship().getName(), PdfPCell.ALIGN_RIGHT, font1, font2));

            table.addCell(getCell("Spol:", enrolment.getStudent().getGender() == 'M' ? "Moški" : "Ženska", PdfPCell.ALIGN_LEFT, font1, font2));
            table.addCell(getCell("Emšo:", enrolment.getStudent().getEmso(), PdfPCell.ALIGN_CENTER, font1, font2));
            table.addCell(getCell("Davčna številka:", enrolment.getStudent().getTaxNumber(), PdfPCell.ALIGN_RIGHT, font1, font2));

            table.addCell(getCell("e-pošta:", enrolment.getStudent().getEmail(), PdfPCell.ALIGN_LEFT, font1, font2));
            table.addCell(getCell("Telefonska številka:", enrolment.getStudent().getPhoneNumber(), PdfPCell.ALIGN_CENTER, font1, font2));
            table.addCell(cell);
            document.add(table);

            table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell(cell);
            table.addCell(getCell("Vročanje:", "", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("Naslov:", "", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("Država, občina:", "", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("Stalno bivališče:", "", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("", enrolment.getStudent().getSendingAddress() != 2 ? "Da" : "", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("", enrolment.getStudent().getAddress1().getLine1(), PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("", enrolment.getStudent().getAddress1().getCountry().getName()+", "+enrolment.getStudent().getAddress1().getPost().getName(), PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("Začasno bivališče:", "", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("", enrolment.getStudent().getAddress2() != null ? (enrolment.getStudent().getSendingAddress() == 2 ? "Da" : "") : "/", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("", enrolment.getStudent().getAddress2() != null ? enrolment.getStudent().getAddress2().getLine1() : "/", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            table.addCell(getCell("", enrolment.getStudent().getAddress2() != null ? enrolment.getStudent().getAddress2().getCountry().getName()+", "+enrolment.getStudent().getAddress2().getPost().getName() : "/", PdfPCell.ALIGN_UNDEFINED, font1, font2));
            document.add(table);

            table = new PdfPTable(2);
            table.setWidthPercentage(100);
            addSpacing("PODATKI O VPISU", table, 2, font1);

            table.addCell(getCell("Študijski program:", enrolment.getStudyProgram().getId()+" - "+enrolment.getStudyProgram().getName(), PdfPCell.ALIGN_LEFT, font1, font2));
            table.addCell(getCell("Kraj izvajanja:", "Ljubljana", PdfPCell.ALIGN_RIGHT, font1, font2));

            table.addCell(getCell("Vrsta študija:", enrolment.getKlasiusSrv().getId()+" - "+enrolment.getKlasiusSrv().getName(), PdfPCell.ALIGN_LEFT, font1, font2));
            table.addCell(getCell("Vrsta vpisa:", enrolment.getType().getName(), PdfPCell.ALIGN_RIGHT, font1, font2));

            table.addCell(getCell("Letnik/dodatno leto:", String.valueOf(enrolment.getYear()), PdfPCell.ALIGN_LEFT, font1, font2));
            table.addCell(getCell("Način študija:", enrolment.getKind().getName(), PdfPCell.ALIGN_RIGHT, font1, font2));

            table.addCell(getCell("Oblika študija:", enrolment.getForm().getName(), PdfPCell.ALIGN_LEFT, font1, font2));
            table.addCell(getCell("Študijsko leto prvega vpisa v ta študijski program:", firstEnrolment.getStudyYear().getName(), PdfPCell.ALIGN_RIGHT, font1, font2));
            document.add(table);
            document.newPage();

            document.add(new Paragraph("Priloga 1: Predmetnik\n\n", font1));

            List<String> header = new ArrayList<>();
            header.add("#");
            header.add("Ime");
            header.add("Semester");
            header.add("KT");

            List<List<String>> rows = new ArrayList<>();
            Iterator<StudentCourses> studentCoursesItr = studentCourses.iterator();
            int index = 1;
            int KD = 0;
            while (studentCoursesItr.hasNext()) {
                List<String> row = new ArrayList<>();
                Course course = studentCoursesItr.next().getCourse();
                row.add(String.valueOf(index));
                row.add(course.getName());
                row.add(course.getSemester());
                KD += course.getCreditPoints();
                row.add(String.valueOf(course.getCreditPoints()));
                rows.add(row);
                index++;
            }

            table = new PdfPTable(4);
            table.setWidthPercentage(100);
            float[] columnWidths = getOptimizedTableWidths(header, rows, new ArrayList<>());
            table.setWidths(columnWidths);
            addTableHeader(table, font3, header, new ArrayList<>());
            addRows(table, font3, rows);
            document.add(table);

            document.add(new Paragraph("Skupaj izbranih kreditnih točk: "+KD+"/60", font1));

            document.close();

            byte[] pdf = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(pdf);
            return bais;
        }catch(Exception e){
            log.severe(e.getMessage());
            return null;
        }
    }

    public ByteArrayInputStream generateEnrolmentConfirmation(int studentId){
        Enrolment enrolment = eB.getLastEnrolmentByStudentId(studentId);
        if(enrolment == null) {
            return null;
        }
        try{
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
            PdfReader reader = new PdfReader("services/src/main/resources/logo-ul-fri.pdf");
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            cell = new PdfPCell(Image.getInstance(page));
            cell.setPadding(0);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
            cell.addElement(new Paragraph("Večna pot 113\n1000 LJUBLJANA, Slovenija\ntelefon:01 47 98 100\nwww.fri.uni-lj.si\ne-mail: dekanat@fri.uni-lj.si",font5));
            cell.setPadding(0);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);

            table.addCell(getCell(enrolment.getStudent().getName().toUpperCase() +" "+ enrolment.getStudent().getSurname().toUpperCase(), enrolment.getStudent().getSendingAddress() != 2 ? enrolment.getStudent().getAddress1().getLine1().toUpperCase() +"\n"+ enrolment.getStudent().getAddress1().getPost().getId() +" "+ enrolment.getStudent().getAddress1().getPost().getName().toUpperCase() : enrolment.getStudent().getAddress2().getLine1().toUpperCase() +"\n"+ enrolment.getStudent().getAddress2().getPost().getId() +" "+ enrolment.getStudent().getAddress2().getPost().getName().toUpperCase(), PdfPCell.ALIGN_LEFT, font3, font4));
            table.addCell(cell);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
            Date date = new Date();
            table.addCell(getCell("Številka: "+enrolment.getId(), "Datum: "+ dateFormat.format(date), PdfPCell.ALIGN_RIGHT, font3, font3));
            document.add(table);

            Paragraph para = new Paragraph("\nPOTRDILO O VPISU\n\n", font4);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            table = new PdfPTable(3);
            table.setWidthPercentage(100);

            table.addCell(getCell("Vpisna številka:", enrolment.getStudent().getRegisterNumber(), PdfPCell.ALIGN_LEFT, font4, font2));
            table.addCell(getCell("Priimek, ime:", enrolment.getStudent().getSurname().toUpperCase() +", "+ enrolment.getStudent().getName().toUpperCase(), PdfPCell.ALIGN_CENTER, font4, font2));
            table.addCell(cell);

            table.addCell(getCell("Datum rojstva:", enrolment.getStudent().getDateOfBirth().toString(), PdfPCell.ALIGN_LEFT, font4, font2));
            table.addCell(getCell("Kraj rojstva:", enrolment.getStudent().getPlaceOfBirth(), PdfPCell.ALIGN_CENTER, font4, font2));
            table.addCell(getCell("Država rojstva:", enrolment.getStudent().getCountryOfBirth().getName(), PdfPCell.ALIGN_RIGHT, font4, font2));

            table.addCell(getCell("Študijsko leto:", enrolment.getStudyYear().getName(), PdfPCell.ALIGN_LEFT, font4, font2));
            table.addCell(getCell("Vrsta vpisa:", enrolment.getType().getName(), PdfPCell.ALIGN_CENTER, font4, font2));
            table.addCell(getCell("Način študija:", enrolment.getKind().getName(), PdfPCell.ALIGN_RIGHT, font4, font2));

            table.addCell(getCell("Oblika študija:", enrolment.getForm().getName(), PdfPCell.ALIGN_LEFT, font4, font2));
            table.addCell(getCell("Letnik/dodatno leto:", String.valueOf(enrolment.getYear()+". letnik"), PdfPCell.ALIGN_CENTER, font4, font2));
            table.addCell(cell);

            table.addCell(getCell("Študijski program:", enrolment.getStudyProgram().getId()+" - "+enrolment.getStudyProgram().getName(), font4, font2, 3));

            table.addCell(getCell("Vrsta študija:", enrolment.getKlasiusSrv().getId()+" - "+enrolment.getKlasiusSrv().getName(), font4, font2, 3));

            table.addCell(cell);
            table.addCell(cell);
            table.addCell(getCell("\n\n\n\nprof. dr. Bojan Orel\ndekan","", PdfPCell.ALIGN_RIGHT, font3, font3));
            document.add(table);

            document.close();

            byte[] pdf = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(pdf);
            return bais;
        }catch(Exception e){
            log.severe(e.getMessage());
            return null;
        }
    }

    public ByteArrayInputStream genarateIndexPdf(int studentId, boolean full){
        List<Enrolment> allEnrolments = eB.getEnrolmentsByStudentId(studentId);
        Student student = allEnrolments.get(0).getStudent();
        try{
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            boolean firstPage = true;

            /* Sections */
            StudyProgram lastStudyProgram = null;
            Paragraph para;
            for(Enrolment enrolment : allEnrolments){

                /* File header */
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);

                // Left
                table.addCell(getCell("UNIVERZA V LJUBLJANI", PdfPCell.ALIGN_LEFT, font4, false));
                table.addCell(getCell("", PdfPCell.ALIGN_LEFT, font4, false));
                table.addCell(getCell("Fakulteta za računalništvo in informatiko", PdfPCell.ALIGN_LEFT, font4, false));

                // Right
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
                Date date = new Date();
                table.addCell(getCell("Datum: " + dateFormat.format(date), PdfPCell.ALIGN_RIGHT, font3, false));

                PdfPCell cell = new PdfPCell(new Phrase(" "));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setMinimumHeight(24);

                table.addCell(cell);
                table.addCell(cell);

                // Left
                table.addCell(getCell("KARTOTEČNI LIST", PdfPCell.ALIGN_LEFT, font4, true));
                table.addCell(getCell("", PdfPCell.ALIGN_RIGHT, font4, true));

                if(!firstPage)
                    document.newPage();

                document.add(table);

                if(firstPage){
                    /* Under line */
                    table = new PdfPTable(1);
                    table.setWidthPercentage(100);
                    table.addCell(getCell("Pregled opravljenih izpitov", Element.ALIGN_CENTER, font4, false));
                    table.addCell(getCell("   ", Element.ALIGN_CENTER, font4, false));
                    table.addCell(getCell(student.getRegisterNumber() + " " + student.getName().toUpperCase() +" "+ student.getSurname().toUpperCase(), Element.ALIGN_CENTER, font3, false));
                    table.setSpacingBefore(10);
                    document.add(table);
                }
                firstPage = false;



                // We group by Study Program
                if(lastStudyProgram  == null || lastStudyProgram.getId() != enrolment.getStudyProgram().getId()){
                    para = new Paragraph("\n" + enrolment.getStudyProgram().getName() + "\n\n", font1);
                    para.setAlignment(Element.ALIGN_CENTER);
                    document.add(para);
                }

                lastStudyProgram = enrolment.getStudyProgram();

                /* Inner header */
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                infoTable.setWidthPercentage(100);

                infoTable.addCell(getHeaderCell("Študijsko leto: ", enrolment.getStudyYear().getName()));
                infoTable.addCell(getHeaderCell("Smer: ", enrolment.getStudyProgram().getName()+", Ni smeri"));
                infoTable.addCell(getHeaderCell("Letnik: ", String.valueOf(enrolment.getYear())));
                infoTable.addCell(getHeaderCell("Vrsta vpisa: ", enrolment.getType().getName()));
                infoTable.addCell(getHeaderCell("Način: ", enrolment.getKind().getName() + " študij"));
                infoTable.addCell(getHeaderCell("Skupina: ", "LJUBLJANA"));

                infoTable.setSpacingAfter(10);
                infoTable.setSpacingBefore(10);
                document.add(infoTable);


                List<List<String>> rows = new ArrayList<>();
                int index = 1;
                int allKT = 0;
                int passedKT = 0;
                int passedCounter = 0;
                int gradeSum = 0;
                Integer examGrade = 0;

                List<StudentCourses> allstudentCourses = scB.getStudentCoursesByEnrolmentId(enrolment.getId());
                for(StudentCourses studentCourse : allstudentCourses){
                    allKT += studentCourse.getCourse().getCreditPoints();

                    List<String> row = new ArrayList<>();
                    Course course = studentCourse.getCourse();

                    // Index
                    row.add(String.valueOf(index));

                    // Course id
                    row.add(String.valueOf(course.getId()));

                    // Course name
                    row.add(course.getName().toUpperCase());

                    // Credit points (KT)
                    row.add(String.valueOf(course.getCreditPoints()));

                    // Professor
                    CourseOrganization co = coB.getCourseOrganizationsByCourseIdAndYear(studentCourse.getCourse().getId(), enrolment.getStudyYear().getId());
                    row.add(co.formatCourseOrganizers().toUpperCase());


                    List<ExamSignUp> allExamSignUps = esuB.getExamSignUpsOnCourseForStudent(studentCourse.getCourse().getId(), student.getId());

                    if(allExamSignUps.isEmpty()){
                        fullEmptyRows(row, 4);
                        rows.add(row);
                        index++;
                        continue;
                    }

                    Integer numb = esuB.getNumberOfExamTakingsBeforeStudyYear(student.getId(), studentCourse.getCourse().getId(), enrolment.getStudyYear().getId());

                    boolean first = true;
                    int signUpsCout = 0;
                    for(ExamSignUp examSignUp : allExamSignUps){

                        if(!first && full){
                            row = new ArrayList<>();
                            fullEmptyRows(row, 5);
                        }
                        first = false;

                        // If no grade -> fill with empty
                        if(examSignUp.getSuggestedGrade() == null){
                            fullEmptyRows(row, 4);
                            rows.add(row);
                            index++;
                            continue;
                        }

                        if(full || signUpsCout == allExamSignUps.size() - 1){
                            // Date
                            dateFormat = new SimpleDateFormat("dd.MM.yy");
                            row.add(dateFormat.format(examSignUp.getCourseExamTerm().getDatetimeObject()));

                            // Grade
                            row.add(String.valueOf(examSignUp.getSuggestedGrade()));
                            examGrade = examSignUp.getSuggestedGrade();

                            // Count this year
                            row.add(String.valueOf(signUpsCout + 1));

                            // Count all
                            row.add( numb == null? String.valueOf(signUpsCout + 1) : String.valueOf(numb + signUpsCout + 1));

                            rows.add(row);
                            index++;
                        }

                        signUpsCout++;
                    }

                    // Get statistics
                    if(examGrade > 5){
                        passedKT += studentCourse.getCourse().getCreditPoints();
                        gradeSum += examGrade;
                        passedCounter++;
                    }
                }

                // Set table
                table = new PdfPTable(9);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.setWidthPercentage(100);
                table.setWidths(new float[] { 2,3,12,2,11,4,3,2,2 });
                addTableHeaderNoBorder(table, font3, indexHeader,true);
                addRows(table, font3, rows);
                document.add(table);

                /* Inner footer */
                infoTable = new PdfPTable(2);
                infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                infoTable.setWidthPercentage(100);

                infoTable.addCell(getHeaderCell("Skupno število kreditnih točk: ", passedKT + " od " + allKT));
                infoTable.addCell(getHeaderCell("",""));
                infoTable.addCell(getHeaderCell("Povprečje izpitov: ", String.valueOf((float)(passedCounter > 0? (float)gradeSum / (float)passedCounter : 0))));
                infoTable.addCell(getHeaderCell("",""));

                infoTable.setSpacingBefore(10);
                document.add(infoTable);
            }

            document.close();

            byte[] pdf = baos.toByteArray();

            PdfReader reader = new PdfReader(pdf);
            int pages = reader.getNumberOfPages();

            ByteArrayOutputStream pagedPdf = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, pagedPdf);
            PdfContentByte pageContent;
            for(int i=1; i<=pages; i++) {
                pageContent = stamper.getOverContent(i);
                ColumnText.showTextAligned(pageContent, Element.ALIGN_RIGHT, new Phrase(String.format("Stran: %d od %d", i, pages), font3), pageContent.getPdfDocument().right(), pageContent.getPdfDocument().top()-31, 0);
            }
            stamper.close();
            reader.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(pagedPdf.toByteArray());
            return bais;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ByteArrayInputStream generateIndexCsv(int studentId, boolean full){
        List<Enrolment> allEnrolments = eB.getEnrolmentsByStudentId(studentId);
        Student student = allEnrolments.get(0).getStudent();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(baos);

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
            Date date = new Date();
            out.write(("Datum: "+dateFormat.format(date)).getBytes());
            out.write(NEW_LINE.getBytes(CHARSET));

            out.write((student.getRegisterNumber() + " " + student.getName().toUpperCase() +" "+ student.getSurname().toUpperCase()).getBytes());
            out.write(NEW_LINE.getBytes(CHARSET));

            /* Sections */
            StudyProgram lastStudyProgram = null;
            Paragraph para;
            for(Enrolment enrolment : allEnrolments){
                // We group by Study Program
                if(lastStudyProgram  == null || lastStudyProgram.getId() != enrolment.getStudyProgram().getId()){
                    para = new Paragraph("\n" + enrolment.getStudyProgram().getName() + "\n\n", font1);
                    para.setAlignment(Element.ALIGN_CENTER);
                }

                lastStudyProgram = enrolment.getStudyProgram();

                /* Inner header */
                out.write(("Študijsko leto: "+enrolment.getStudyYear().getName()).getBytes());
                out.write(NEW_LINE.getBytes(CHARSET));

                out.write(("Smer: "+ enrolment.getStudyProgram().getName()+", Ni smeri").getBytes());
                out.write(NEW_LINE.getBytes(CHARSET));

                out.write(("Letnik: "+ String.valueOf(enrolment.getYear())).getBytes());
                out.write(NEW_LINE.getBytes(CHARSET));

                out.write(("Vrsta vpisa: "+ enrolment.getType().getName()).getBytes());
                out.write(NEW_LINE.getBytes(CHARSET));

                out.write(("Način: "+ enrolment.getKind().getName() + " študij").getBytes());
                out.write(NEW_LINE.getBytes(CHARSET));

                // Set table header
                int i = 0;
                for(String header: indexHeader){
                    if(i>0)
                        out.write(DELIMETER.getBytes(CHARSET));
                    out.write(header.getBytes());
                    i++;
                }
                out.write(NEW_LINE.getBytes(CHARSET));

                List<List<String>> rows = new ArrayList<>();
                int index = 1;
                int allKT = 0;
                int passedKT = 0;
                int passedCounter = 0;
                int gradeSum = 0;
                Integer examGrade = 0;

                List<StudentCourses> allstudentCourses = scB.getStudentCoursesByEnrolmentId(enrolment.getId());
                for(StudentCourses studentCourse : allstudentCourses){
                    allKT += studentCourse.getCourse().getCreditPoints();

                    List<String> row = new ArrayList<>();
                    Course course = studentCourse.getCourse();

                    // Index
                    out.write(String.valueOf(index).getBytes());
                    out.write(DELIMETER.getBytes(CHARSET));

                    // Course id
                    out.write(String.valueOf(course.getId()).getBytes());
                    out.write(DELIMETER.getBytes(CHARSET));

                    // Course name
                    out.write(course.getName().toUpperCase().getBytes());
                    out.write(DELIMETER.getBytes(CHARSET));

                    // Credit points (KT)
                    out.write(String.valueOf(course.getCreditPoints()).getBytes());
                    out.write(DELIMETER.getBytes(CHARSET));

                    // Professor
                    CourseOrganization co = coB.getCourseOrganizationsByCourseIdAndYear(studentCourse.getCourse().getId(), enrolment.getStudyYear().getId());
                    out.write(co.formatCourseOrganizers().toUpperCase().getBytes());
                    out.write(DELIMETER.getBytes(CHARSET));

                    List<ExamSignUp> allExamSignUps = esuB.getExamSignUpsOnCourseForStudent(studentCourse.getCourse().getId(), student.getId());

                    if(allExamSignUps.isEmpty()){
                        out.write(DELIMETER.getBytes(CHARSET));
                        out.write(DELIMETER.getBytes(CHARSET));
                        out.write(DELIMETER.getBytes(CHARSET));
                        out.write(DELIMETER.getBytes(CHARSET));
                        out.write(NEW_LINE.getBytes(CHARSET));
                        index++;
                        continue;
                    }

                    Integer numb = esuB.getNumberOfExamTakingsBeforeStudyYear(student.getId(), studentCourse.getCourse().getId(), enrolment.getStudyYear().getId());

                    boolean first = true;
                    int signUpsCout = 0;
                    for(ExamSignUp examSignUp : allExamSignUps){

                        if(!first && full){
                            out.write(DELIMETER.getBytes(CHARSET));
                            out.write(DELIMETER.getBytes(CHARSET));
                            out.write(DELIMETER.getBytes(CHARSET));
                            out.write(DELIMETER.getBytes(CHARSET));
                            out.write(DELIMETER.getBytes(CHARSET));
                        }
                        first = false;

                        // If no grade -> fill with empty
                        if(examSignUp.getSuggestedGrade() == null){
                            out.write(DELIMETER.getBytes(CHARSET));
                            out.write(DELIMETER.getBytes(CHARSET));
                            out.write(DELIMETER.getBytes(CHARSET));
                            out.write(DELIMETER.getBytes(CHARSET));
                            out.write(NEW_LINE.getBytes(CHARSET));
                            index++;
                            continue;
                        }

                        if(full || signUpsCout == allExamSignUps.size() - 1){
                            // Date
                            dateFormat = new SimpleDateFormat("dd.MM.yy");
                            out.write(dateFormat.format(examSignUp.getCourseExamTerm().getDatetimeObject()).getBytes());
                            out.write(DELIMETER.getBytes(CHARSET));

                            // Grade
                            out.write(String.valueOf(examSignUp.getSuggestedGrade()).getBytes());
                            out.write(DELIMETER.getBytes(CHARSET));
                            examGrade = examSignUp.getSuggestedGrade();

                            // Count this year
                            out.write(String.valueOf(signUpsCout + 1).getBytes());
                            out.write(DELIMETER.getBytes(CHARSET));

                            // Count all
                            out.write((numb == null? String.valueOf(signUpsCout + 1) : String.valueOf(numb + signUpsCout + 1)).getBytes());
                            out.write(NEW_LINE.getBytes(CHARSET));

                            rows.add(row);
                            index++;
                        }

                        signUpsCout++;
                    }

                    // Get statistics
                    if(examGrade > 5){
                        passedKT += studentCourse.getCourse().getCreditPoints();
                        gradeSum += examGrade;
                        passedCounter++;
                    }
                }

                /* Inner footer */
                out.write(("Skupno število kreditnih točk: " + String.valueOf(passedKT) + " od " + String.valueOf(allKT)).getBytes());
                out.write(NEW_LINE.getBytes(CHARSET));
                out.write(("Povprečje izpitov: " + String.valueOf((float)(passedCounter > 0? (float)gradeSum / (float)passedCounter : 0))).getBytes());
                out.write(NEW_LINE.getBytes(CHARSET));
            }

            baos.flush();
            baos.close();

            byte[] csv = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(csv);
            return bais;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return null;
        }
    }

    /* Helpers */

    private PdfPCell createHeader(List<String> head) throws DocumentException {
        PdfPCell cell = new PdfPCell();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
        Date date = new Date();
        cell.addElement(new Paragraph("Datum kreiranja izpisa: "+ dateFormat.format(date)+"\n", font5));

        Iterator<String> headIt = head.iterator();
        int i = 0;
        while (headIt.hasNext()) {
            String st = headIt.next();
            if(i == 0) {
                cell.addElement(new Paragraph(st+"\n", font1));
            } else {
                cell.addElement(new Paragraph(st+"\n", font2));
            }
            i++;
        }
        cell.addElement( Chunk.NEWLINE );
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPTable createLegend(List<Boolean> inLegend, List<String> coloumnNames) throws DocumentException {
        Iterator<Boolean> inLegendIt = inLegend.iterator();
        boolean createLegend = false;
        while (inLegendIt.hasNext()) {
            boolean tmp = inLegendIt.next();
            log.info(String.valueOf(tmp));
            if(tmp) {
                createLegend = true;
                break;
            }
        }
        if(createLegend) {
            PdfPTable legend = new PdfPTable(2);
            legend.setWidthPercentage(100);
            PdfPCell header = new PdfPCell(new Paragraph("Legenda:", font3));
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setColspan(2);
            header.setBorder(PdfPCell.NO_BORDER);
            legend.addCell(header);
            int i = 0, j = 1;
            inLegendIt = inLegend.iterator();
            while (inLegendIt.hasNext()) {
                if(inLegendIt.next()) {
                    PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(j++), font3));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    legend.addCell(cell);
                    cell = new PdfPCell(new Paragraph(coloumnNames.get(i), font3));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    legend.addCell(cell);
                }
                i++;
            }
            return legend;
        }
        return null;
    }

    private void addSpacing(String text, PdfPTable table, int numCols, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph("\n\n"+text+"\n", font));
        cell.setColspan(numCols);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
    }

    private PdfPCell getCell(String text1, String text2, int alignment, Font font1, Font font2) {
        PdfPCell cell = new PdfPCell();
        if(!text1.equals("")) {
            cell.addElement(new Paragraph(text1, font2));
        }
        if(!text2.equals("")) {
            cell.addElement(new Paragraph(text2,font1));
        }
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell getCell(String text1, String text2, Font font1, Font font2, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(colspan);
        if(!text1.equals("")) {
            cell.addElement(new Paragraph(text1, font2));
        }
        if(!text2.equals("")) {
            cell.addElement(new Paragraph(text2,font1));
        }
        cell.setPadding(0);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public PdfPCell getCell(String text, int alignment, Font font, boolean bottomBorder) {
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        if(!bottomBorder)cell.setBorder(PdfPCell.NO_BORDER);
        if(bottomBorder){
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidthBottom(2);
            cell.setPaddingBottom(5);
        }
        return cell;
    }

    private void addTableHeader(PdfPTable table, Font font, List<String> names, List<Boolean> inLegend) {
        Iterator<String> namesIt = names.iterator();
        int i = 0, j=1;
        while (namesIt.hasNext()) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            String name = namesIt.next();
            String phrase = i > 0 && inLegend.get(i-1) ? String.valueOf(j++) : name;
            header.setPhrase(new Phrase(phrase, font));
            table.addCell(header);
            i++;
        }
    }

    private void addTableHeaderNoBorder(PdfPTable table, Font font, List<String> names, boolean lastColSpan2) {
        int i = 0;
        Iterator<String> namesIt = names.iterator();
        while (namesIt.hasNext()) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setPhrase(new Phrase(namesIt.next(), font));
            header.setBorder(Rectangle.NO_BORDER);
            if(i == names.size()-1)
                header.setColspan(2);
            table.addCell(header);
            i++;
        }
    }

    private void addRows(PdfPTable table, Font font, List<List<String>> rows) {
        Iterator<List<String>> rowIt = rows.iterator();
        while (rowIt.hasNext()) {
            Iterator<String> cellIt = rowIt.next().iterator();
            while (cellIt.hasNext()) {
                table.addCell(new Paragraph(cellIt.next(), font));
            }
        }
    }

    private float[] getOptimizedTableWidths(List<String> coloumnNames, List<List<String>> rows, List<Boolean> inLegend) {
        float[] widths = new float[coloumnNames.size()];

        int i = 0;
        Iterator<String> coloumnNamesIt = coloumnNames.iterator();
        while (coloumnNamesIt.hasNext()) {
            String coloumnName = coloumnNamesIt.next();
            if(coloumnName.length() > widths[i]) {
                if(i > 0 && !inLegend.get(i-1)) {
                    widths[i] = coloumnName.length();
                }
            }
            i++;
        }

        Iterator<List<String>> rowIt = rows.iterator();
        while (rowIt.hasNext()) {
            i = 0;
            Iterator<String> cellIt = rowIt.next().iterator();
            while (cellIt.hasNext()) {
                String cell = cellIt.next();
                if(cell.length() > widths[i]) {
                    widths[i] = cell.length();
                }
                i++;
            }
        }

        float sum = 0;
        for (i = 0; i < widths.length; i++) {
            sum += widths[i];
        }
        if(widths[0]/sum < 0.5f) {
            widths[0] = sum*5/100;
        }

        return widths;
    }

    private void fullEmptyRows(List<String> row, int count){
        for(int i = 0; i < count; i++){
            row.add("");
        }
    }

    private PdfPCell getHeaderCell(String placeHolder,String data) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(placeHolder, font4));
        p.add(new Chunk(" " + data, font3));
        PdfPCell cell = new PdfPCell(p);
        cell.setPadding(7);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(new GrayColor(240));
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
