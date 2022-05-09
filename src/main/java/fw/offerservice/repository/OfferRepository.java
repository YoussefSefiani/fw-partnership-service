package fw.offerservice.repository;

import fw.offerservice.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByInfluencerId(String influencerId);
}
