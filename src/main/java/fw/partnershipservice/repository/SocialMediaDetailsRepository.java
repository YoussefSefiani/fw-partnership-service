package fw.partnershipservice.repository;

import fw.partnershipservice.model.SocialMediaDetails;
import fw.partnershipservice.model.SocialMediaList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SocialMediaDetailsRepository extends JpaRepository<SocialMediaDetails, Long> {

    @Query(value = "SELECT * FROM social_media_details s where s.partnership_id = ?1", nativeQuery = true)
    List<SocialMediaDetails> findByPartnershipId(Long partnershipId);
}
