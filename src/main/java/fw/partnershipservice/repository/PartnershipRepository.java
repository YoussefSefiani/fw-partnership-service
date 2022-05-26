package fw.partnershipservice.repository;

import fw.partnershipservice.model.Partnership;
import fw.partnershipservice.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnershipRepository extends JpaRepository<Partnership, Long> {
    List<Partnership> findByInfluencerId(String influencerId);
    List<Partnership> findByStatusAndInfluencerId(Status status, Long influencerId);
    List<Partnership> findByStatusAndBrandId(Status status, Long brandId);
    List<Partnership> findByStatusInAndInfluencerId(List<Status> statusList, Long influencerId);
    int countByStatusAndInfluencerId(Status status, Long influencerId);
}
