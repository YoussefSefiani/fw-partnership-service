package fw.partnershipservice.model;

import lombok.*;

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
    private HashMap<Date, Integer> totalEarningsGraphData;

    private int totalMoneyEarnedMonth;
    private int totalPartnershipsMonth;


}
