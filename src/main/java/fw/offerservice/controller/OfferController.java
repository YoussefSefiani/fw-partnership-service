package fw.offerservice.controller;

import fw.offerservice.model.Offer;
import fw.offerservice.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @GetMapping(path = "/requested")
    public List<Offer> getRequestedOffers() {
        return offerService.getRequestedOffers();
    }

    @GetMapping(path = "/upcoming")
    public List<Offer> getUpcomingEvents() {
        return offerService.getUpcomingEvents();
    }

    @GetMapping(path = "/accept/{offerId}")
    public void acceptOffer(@PathVariable("offerId") Long offerId) {
        offerService.acceptOffer(offerId);
    }

    @GetMapping(path = "/decline/{offerId}")
    public void declineOffer(@PathVariable("offerId") Long offerId) {
        offerService.declineOffer(offerId);
    }

}
