
package ie.ncirl.tradingplatform.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ie.ncirl.tradingplatform.dto.ResponseDTO;
import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.service.GoalsService;
import ie.ncirl.tradingplatform.service.S3ClientService;
import ie.ncirl.tradingplatform.service.ShareService;
import ie.ncirl.tradingplatform.service.UserService;
import ie.ncirl.tradingplatform.vo.S3ReportDetailsVo;
import ie.ncirl.tradingplatform.vo.TransactionHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    GoalsService goalsService;

    @Autowired
    UserService userService;

    @Autowired
    ShareService shareService;

    @Autowired
    S3ClientService s3ClientService;


    @GetMapping("/app/getReports")
    public List<S3ReportDetailsVo> getMyReports() throws IOException {
        return s3ClientService.getAllReports();
    }


    @GetMapping("/app/trade/report")
    public ResponseDTO<String> createReport() {
        Account account = userService.getActiveAccount();
        List<TransactionHistoryVo> txnHistory = shareService.getTransactionsForAccount(account);

        String status = null;
        try {

            DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
            Date dateobj = new Date();
            String fileName = account.getUser().getDisplayName() + "-" + df.format(dateobj) + ".pdf";
            String absfileName = s3ClientService.getDownloadPath(fileName);
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(absfileName));

            document.open();

            PdfPTable table = new PdfPTable(4);

            s3ClientService.addTableHeader(table);
            s3ClientService.addRows(table, account);
            //s3ClientService.addCustomRows(table);

            document.add(table);
            document.close();

            File convFile = new File(absfileName);
            //File file = convertMultiPartToFile(convFile);
            status = s3ClientService.uploadFileTos3bucket(fileName, convFile);
            convFile.delete();

        } catch (Exception e) {
            return new ResponseDTO<String>().withError("UploadController().uploadFile().Exception : " + e.getMessage());
        }
        return new ResponseDTO<String>().withData("Report generated successfully");
    }

}
