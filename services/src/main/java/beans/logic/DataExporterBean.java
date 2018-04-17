package beans.logic;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import entities.logic.Table;

import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Logger;

@ApplicationScoped
public class DataExporterBean {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public ByteArrayInputStream generateTablePdf(Table table){
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont base = BaseFont.createFont("arial.ttf", "Cp1250", BaseFont.NOT_EMBEDDED);
            Font font = new Font(base, 12);
            //Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            document.add(new Paragraph("Test č ć š ć ž đ", font));
            //Chunk chunk = new Chunk("Hello Worldčžšđć", font);
            //document.add(chunk);
            document.close();

            byte[] pdf = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(pdf);
            return bais;
        } catch (Exception e) {
            log.severe(e.getMessage());
            return null;
        }

    }

}
