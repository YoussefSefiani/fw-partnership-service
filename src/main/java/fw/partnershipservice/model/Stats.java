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

    private int totalMoneyEarned;
    private int totalPartnerships;
    private HashMap<String, Integer> topCountries;
    private HashMap<String, Integer> topSocialMedia;

    private int moneyEarnedMonth;
    private int totalPartnershipsMonth;


}
