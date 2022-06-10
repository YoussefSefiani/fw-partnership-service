package fw.partnershipservice.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class BrandIdWrapper {
    private List<Long> brandIds;

    public BrandIdWrapper() {
        brandIds = new ArrayList<>();
    }

}
