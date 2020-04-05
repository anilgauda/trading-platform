package ie.ncirl.tradingplatform.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class S3ReportDetailsVo {
    private String name;
    private Date date;
    private String link;

}
