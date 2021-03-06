package fw.partnershipservice.feign;

import fw.partnershipservice.model.BrandIdWrapper;
import fw.partnershipservice.model.InfluencerIdWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;

@FeignClient(name="fw-auth-service", url="${app.services.auth.url}")
public interface UserRestConsumer {

    @PostMapping("/api/user/brandnames")
    HashMap<Long, String> getAllPartnershipBrandNames(BrandIdWrapper brandIds);

    @PostMapping("/api/user/influencernames")
    HashMap<Long, String> getAllPartnershipInfluencerNames(InfluencerIdWrapper influencerIds);


}
