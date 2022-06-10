package fw.partnershipservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class InfluencerIdWrapper {

    private List<Long> influencerIds;

    public InfluencerIdWrapper() {
        influencerIds = new ArrayList<>();
    }

}
