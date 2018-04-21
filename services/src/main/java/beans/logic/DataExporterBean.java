package beans.logic;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entities.logic.TableData;

import javax.enterprise.context.ApplicationScoped;
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

    public ByteArrayInputStream generateEnrolmentSheet(){
        try{
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont base = BaseFont.createFont("arial.ttf", "Cp1250", BaseFont.NOT_EMBEDDED);
            Font font1 = new Font(base, 14);
            Font font2 = new Font(base, 8);

            /* HEADER */
            document.add(new Paragraph("VPISNI LIST 2014/2015",font1)); //TODO: date
            document.add(new Paragraph("za študente", font2));
            document.add(new Paragraph("Fakulteta za računalništvo in informatiko", font2));

            /* LOGO */
            Image img = Image.getInstance("services/src/logo-ulj.png"); //TODO: should prob. handle diff. for deployment
            img.setAbsolutePosition(
                    (PageSize.A4.getWidth() - img.getScaledWidth()) / 2,
                    PageSize.A4.getHeight() - 130);
            document.add(img);


            document.close();

            byte[] pdf = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(pdf);
            return bais;
        }catch(Exception e){
            log.severe(e.getMessage());
            return null;
        }
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
