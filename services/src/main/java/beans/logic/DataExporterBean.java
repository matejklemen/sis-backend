package beans.logic;

import beans.crud.EnrolmentBean;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Enrolment;
import entities.logic.TableData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DataExporterBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    private  final String CHARSET = "UTF8";
    private final String DELIMETER = ";";
    private  final String NEW_LINE = "\r\n";

    @Inject
    EnrolmentBean eB;

    public ByteArrayInputStream generateTablePdf(TableData tableData){
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont base = BaseFont.createFont("arial.ttf", "Cp1250", BaseFont.NOT_EMBEDDED);
            Font font1 = new Font(base, 14);
            Font font2 = new Font(base, 8);

            PdfPTable table = new PdfPTable(tableData.getColoumnNames().size());

            PdfPCell mainHeader = new PdfPCell();
            mainHeader.setColspan(tableData.getColoumnNames().size());
            mainHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            mainHeader.setBorderWidth(2);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
            Date date = new Date();
            mainHeader.setPhrase(new Phrase(tableData.getTable_name()+", datum: "+ dateFormat.format(date), font1));
            table.addCell(mainHeader);

            addTableHeader(table, font2, tableData.getColoumnNames());
            addRows(table, font2, tableData.getRows());

            document.add(table);

            document.close();

            byte[] pdf = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(pdf);
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
        try{
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont base = BaseFont.createFont("arial.ttf", "Cp1250", BaseFont.NOT_EMBEDDED);
            Font font1 = new Font(base, 12);
            Font font2 = new Font(base, 10, Font.NORMAL, BaseColor.GRAY);

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
            cell = new PdfPCell(Image.getInstance("services/src/main/resources/logo-ul.jpg"));
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

            /*TODO*/
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

    private void addSpacing(String text, PdfPTable table, int numCols, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph("\n\n"+text+"\n", font));
        cell.setColspan(numCols);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
    }

    public PdfPCell getCell(String text1, String text2, int alignment, Font font1, Font font2) {
        PdfPCell cell = new PdfPCell();
        if(!text1.equals("")) {
            cell.addElement(new Paragraph(text1, font2));
        }
        if(!text2.equals("")) {
            cell.addElement(new Paragraph(text2,font1));
        }
        cell.setPadding(0);
        //cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private void addTableHeader(PdfPTable table, Font font, List<String> names) {
        Iterator<String> namesIt = names.iterator();
        while (namesIt.hasNext()) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(namesIt.next(), font));
            table.addCell(header);
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

}
