package fw.offerservice.repository;

import fw.offerservice.model.Offer;
import fw.offerservice.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByInfluencerId(String influencerId);
    List<Offer> findByStatus(Status status);
    List<Offer> findByStatusNot(Status status);
}
