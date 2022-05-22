package fw.partnershipservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashMap;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stats {

    private Long influencerId;

    private int totalPartnerships;
    private int totalMoneyEarned;

    private HashMap<String, Integer> topCountries;
    private HashMap<String, Integer> topSocialMedia;

    private HashMap<String, Integer> totalEarningsGraphData;

    private int totalMoneyEarnedMonth;
    private int totalPartnershipsMonth;


}
