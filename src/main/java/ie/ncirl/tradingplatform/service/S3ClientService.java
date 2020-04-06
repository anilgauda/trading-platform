package ie.ncirl.tradingplatform.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.StockTransaction;
import ie.ncirl.tradingplatform.repo.StockRepo;
import ie.ncirl.tradingplatform.util.UserUtil;
import ie.ncirl.tradingplatform.vo.S3ReportDetailsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
public class S3ClientService {

    private AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();

    @Value("${s3.bucket.name}")
    private String bucketName;
    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private ShareService shareService;
    
    @Autowired
    private UserService userService;

    public String uploadFileTos3bucket(String fileName, File file) {
        try {
            s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
        } catch (AmazonServiceException e) {
            return "uploadFileTos3bucket().Uploading failed :" + e.getMessage();
        }
        return "Uploading Successfull -> ";
    }

    public List<S3ReportDetailsVo> getAllReports() throws IOException {
        List<S3ReportDetailsVo> reportList = new ArrayList<>();
        ObjectListing objectListing = s3client.listObjects(bucketName);
        Account account = userService.getActiveAccount();
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            if (os.getKey().toLowerCase().startsWith(account.getUser().getDisplayName().toLowerCase())) {
                S3ReportDetailsVo S3ReportDetailsVo = new S3ReportDetailsVo(os.getKey(), os.getLastModified(), os.getKey());
                reportList.add(S3ReportDetailsVo);
            }
        }
        return reportList;
    }

    public S3ObjectInputStream downloadReport(String absFilePath, String fileName) throws IOException {
        S3Object s3object = s3client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        return inputStream;
    }

    public String getDownloadPath(String filename) {
        String absoluteFilePath = "";
        try {
            String workingDir = System.getProperty("user.dir");
            String your_os = System.getProperty("os.name").toLowerCase();
            if (your_os.indexOf("win") >= 0) {
                absoluteFilePath = workingDir + "\\" + filename;
            } else if (your_os.indexOf("nix") >= 0 ||
                    your_os.indexOf("nux") >= 0 ||
                    your_os.indexOf("mac") >= 0) {
                //if unix or mac
                absoluteFilePath = workingDir + "/" + filename;
            } else
                absoluteFilePath = workingDir + "/" + filename;
            //File file = new File(absoluteFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return absoluteFilePath;
    }

    public Document createDoc(String fileName) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        return document;
    }

    public void addTableHeader(PdfPTable table) {
        Stream.of("Share Name", "Buy Quantity", "Sell Quantity", "Profit/Loss")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    public void addRows(PdfPTable table, Account account) {
        Map<String, String> symbolNameMap = new HashMap<>();
        for (ie.ncirl.tradingplatform.model.Stock stock : stockRepo.findAllByAccount(account)) {
            double profit = 0d;
            int buyQuantity = 0;
            int sellQuantity = 0;
            String profitOrLoss = "";
            for (StockTransaction stockTransaction : stock.getStockTransactions()) {
                if (stockTransaction.getBuyPrice() != null) {
                    buyQuantity++;
                    profit -= stockTransaction.getBuyPrice();
                } else {
                    sellQuantity++;
                    profit += stockTransaction.getSellPrice();
                }
            }
            profit = Math.round(100.0 * profit) / 100.0;
            profitOrLoss = profit >= 0 ? "Profit of " + profit : "Loss of " + (profit * -1);
            table.addCell(shareService.getNameFromSymbol(stock.getSymbol(), symbolNameMap));
            table.addCell(String.valueOf(buyQuantity));
            table.addCell(String.valueOf(sellQuantity));
            table.addCell(profitOrLoss);
        }
    }


}


