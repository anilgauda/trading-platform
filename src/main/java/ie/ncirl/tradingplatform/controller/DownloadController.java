package ie.ncirl.tradingplatform.controller;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import ie.ncirl.tradingplatform.service.S3ClientService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class DownloadController {
    @Autowired
    S3ClientService s3ClientService;

    @GetMapping("/app/trade/report/download")
    public ResponseEntity<byte[]> downLoadReport(@RequestParam("fileName") String fileName) throws IOException {
        String absfileName = s3ClientService.getDownloadPath(fileName);

        S3ObjectInputStream result = s3ClientService.downloadReport(absfileName, fileName);

        byte[] bytes = IOUtils.toByteArray(result);


        HttpHeaders respHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/pdf");
        //respHeaders.setContentType(mediaType);
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setContentLength(bytes.length);
        respHeaders.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, respHeaders, HttpStatus.OK);
    }

}
