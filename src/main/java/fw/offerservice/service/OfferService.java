package fw.offerservice.service;

import fw.offerservice.model.Offer;
import fw.offerservice.model.Status;
import fw.offerservice.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> getInfluencerOffers(String influencerId) {
        return offerRepository.findByInfluencerId(influencerId);
    }

    public void addOffer(Offer offer) {
        offer.setStatus(Status.REQUESTED);
        offerRepository.save(offer);
    }

    public void deleteOffer(Long offerId) {
        boolean exists = offerRepository.existsById(offerId);
        if (!exists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with id %s does not exist", offerId));
        }
        offerRepository.deleteById(offerId);
    }

    public void updateInfluencer(Long offerId, Offer offer) {
    }


    public void acceptOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Offer with id %s does not exist", offerId))
                );

        offer.setStatus(Status.PENDING);
        offerRepository.save(offer);
    }

    public void declineOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Offer with id %s does not exist", offerId))
                );

        offer.setStatus(Status.DECLINED);
        offerRepository.save(offer);
    }

    public void payOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Offer with id %s does not exist", offerId))
                );
        offer.setStatus(Status.IN_PROGRESS);
    }

    public void finishOffer(Long offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Offer with id %s does not exist", offerId))
                );
        offer.setStatus(Status.DONE);
    }

    public List<Offer> getRequestedOffers(Long influencerId) {
        return offerRepository.findByStatusAndInfluencerId(Status.REQUESTED, influencerId);
    }

    public List<Offer> getUpcomingEvents(Long influencerId) {
        List<Status> statusList = new ArrayList<>();
        statusList.add(Status.PENDING);
        statusList.add(Status.IN_PROGRESS);
        return offerRepository.findByStatusInAndInfluencerId(statusList, influencerId);
    }

    public List<Offer> getHistoryEvents(Long influencerId) {
        List<Status> statusList = new ArrayList<>();
        statusList.add(Status.DECLINED);
        statusList.add(Status.DONE);
        return offerRepository.findByStatusInAndInfluencerId(statusList, influencerId);
    }
}
