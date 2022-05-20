package fw.offerservice.controller;

import fw.offerservice.model.Offer;
import fw.offerservice.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/offer")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }


    @GetMapping(path="{influencerId}")
    public List<Offer> getInfluencerOffer(@PathVariable("influencerId") String influencerId) {
        return offerService.getInfluencerOffers(influencerId);
    }

    @PostMapping
    public void addOffer(@RequestBody Offer offer) {
        offerService.addOffer(offer);
    }

    @DeleteMapping(path = "{offerId}")
    public void deleteOffer(@PathVariable("offerId") Long offerId) {
        offerService.deleteOffer(offerId);
    }

    @PutMapping(path = "{offerId}")
    public void updateOffer(@PathVariable("offerId") Long offerId, @RequestBody Offer offer) {
        offerService.updateInfluencer(offerId, offer);
    }

    @GetMapping(path = "/requested/{influencerId}")
    public List<Offer> getRequestedOffers(@PathVariable("influencerId") Long influencerId) {
        return offerService.getRequestedOffers(influencerId);
    }

    @GetMapping(path = "/upcoming/{influencerId}")
    public List<Offer> getUpcomingEvents(@PathVariable("influencerId") Long influencerId) {
        return offerService.getUpcomingEvents(influencerId);
    }

    @GetMapping(path = "/history/{influencerId}")
    public List<Offer> getHistoryEvents(@PathVariable("influencerId") Long influencerId) {
        return offerService.getHistoryEvents(influencerId);
    }

    @GetMapping(path = "/accept/{offerId}")
    public void acceptOffer(@PathVariable("offerId") Long offerId) {
        offerService.acceptOffer(offerId);
    }

    @GetMapping(path = "/decline/{offerId}")
    public void declineOffer(@PathVariable("offerId") Long offerId) {
        offerService.declineOffer(offerId);
    }

    @GetMapping("/pay/{offerId}")
    public void payOffer(@PathVariable("offerId") Long offerId) {
        offerService.payOffer(offerId);
    }

    @GetMapping("/finish/{offerId}")
    public void finishOffer(@PathVariable("offerId") Long offerId) {
        offerService.finishOffer(offerId);
    }

}
