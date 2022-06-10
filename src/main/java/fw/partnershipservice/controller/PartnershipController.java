package fw.partnershipservice.controller;

import fw.partnershipservice.model.CheckoutPayment;
import fw.partnershipservice.model.Partnership;
import fw.partnershipservice.model.Stats;
import fw.partnershipservice.service.PartnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/partnership")
public class PartnershipController {

    private final PartnershipService partnershipService;

    @Autowired
    public PartnershipController(PartnershipService partnershipService) {
        this.partnershipService = partnershipService;
    }

    @GetMapping(path="influencer/{influencerId}")
    public List<Partnership> getInfluencerPartnerships(@PathVariable("influencerId") Long influencerId) {
        return partnershipService.getInfluencerPartnerships(influencerId);
    }

    @GetMapping(path="brand/{brandId}")
    public List<Partnership> getBrandPartnerships(@PathVariable("brandId") Long brandId) {
        return partnershipService.getBrandPartnerships(brandId);
    }

    @PostMapping
    public Long addPartnership(@RequestBody Partnership partnership) {
        return partnershipService.addPartnership(partnership);
    }

    @DeleteMapping(path = "{partnershipId}")
    public void deletePartnership(@PathVariable("partnershipId") Long partnershipId) {
        partnershipService.deletePartnership(partnershipId);
    }

    @PutMapping(path = "{partnershipId}")
    public void updatePartnership(@PathVariable("partnershipId") Long partnershipId, @RequestBody Partnership partnership) {
        partnershipService.updateInfluencer(partnershipId, partnership);
    }

    @GetMapping(path = "requested/{influencerId}")
    public List<Partnership> getRequestedPartnerships(@PathVariable("influencerId") Long influencerId) {
        return partnershipService.getRequestedPartnerships(influencerId);
    }

    @GetMapping(path = "upcoming/{influencerId}")
    public List<Partnership> getUpcomingEvents(@PathVariable("influencerId") Long influencerId) {
        return partnershipService.getUpcomingEvents(influencerId);
    }

    @GetMapping(path = "pending/{brandId}")
    public List<Partnership> getPendingPartnerships(@PathVariable("brandId") Long brandId) {
        return partnershipService.getPendingPartnerships(brandId);
    }

    @GetMapping(path = "history/{influencerId}")
    public List<Partnership> getHistoryEvents(@PathVariable("influencerId") Long influencerId) {
        return partnershipService.getHistoryEvents(influencerId);
    }

    @GetMapping(path = "accept/{partnershipId}")
    public void acceptPartnership(@PathVariable("partnershipId") Long partnershipId) {
        partnershipService.acceptPartnership(partnershipId);
    }

    @GetMapping(path = "decline/{partnershipId}")
    public void declinePartnership(@PathVariable("partnershipId") Long partnershipId) {
        partnershipService.declinePartnership(partnershipId);
    }

    @PostMapping(path = "validate/{partnershipId}")
    public void validatePartnership(@PathVariable("partnershipId") Long partnershipId) {
        partnershipService.validatePartnership(partnershipId);
    }

    @GetMapping(path = "finish/{partnershipId}")
    public void finishPartnership(@PathVariable("partnershipId") Long partnershipId) {
        partnershipService.finishPartnership(partnershipId);
    }

    @GetMapping(path = "stats/influencer/{influencerId}")
    public Stats getInfluencerStats(@PathVariable("influencerId") Long influencerId) {
       return partnershipService.getInfluencerStats(influencerId);
    }


}
