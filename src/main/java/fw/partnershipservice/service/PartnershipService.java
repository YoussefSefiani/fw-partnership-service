package fw.partnershipservice.service;

import fw.partnershipservice.model.Partnership;
import fw.partnershipservice.model.Status;
import fw.partnershipservice.repository.PartnershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

//prix plateforme pays date

@Service
public class PartnershipService {

    private final PartnershipRepository partnershipRepository;

    @Autowired
    public PartnershipService(PartnershipRepository partnershipRepository) {
        this.partnershipRepository = partnershipRepository;
    }

    public List<Partnership> getInfluencerPartnerships(String influencerId) {
        return partnershipRepository.findByInfluencerId(influencerId);
    }

    public void addPartnership(Partnership partnership) {
        partnership.setStatus(Status.REQUESTED);
        partnershipRepository.save(partnership);
    }

    public void deletePartnership(Long partnershipId) {
        boolean exists = partnershipRepository.existsById(partnershipId);
        if (!exists) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("User with id %s does not exist", partnershipId));
        }
        partnershipRepository.deleteById(partnershipId);
    }

    public void updateInfluencer(Long partnershipId, Partnership partnership) {
    }


    public void acceptPartnership(Long partnershipId) {
        Partnership partnership = partnershipRepository.findById(partnershipId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Partnership with id %s does not exist", partnershipId))
                );

        partnership.setStatus(Status.PENDING);
        partnershipRepository.save(partnership);
    }

    public void declinePartnership(Long partnershipId) {
        Partnership partnership = partnershipRepository.findById(partnershipId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("partnership with id %s does not exist", partnershipId))
                );

        partnership.setStatus(Status.DECLINED);
        partnershipRepository.save(partnership);
    }

    public void payPartnership(Long partnershipId) {
        Partnership partnership = partnershipRepository.findById(partnershipId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("partnership with id %s does not exist", partnershipId))
                );
        partnership.setStatus(Status.IN_PROGRESS);
    }

    public void finishPartnership(Long partnershipId) {
        Partnership partnership = partnershipRepository.findById(partnershipId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("partnership with id %s does not exist", partnershipId))
                );
        partnership.setStatus(Status.DONE);
    }

    public List<Partnership> getRequestedPartnerships(Long influencerId) {
        return partnershipRepository.findByStatusAndInfluencerId(Status.REQUESTED, influencerId);
    }

    public List<Partnership> getUpcomingEvents(Long influencerId) {
        List<Status> statusList = new ArrayList<>();
        statusList.add(Status.PENDING);
        statusList.add(Status.IN_PROGRESS);


        return partnershipRepository.findByStatusInAndInfluencerId(statusList, influencerId);
    }

    public List<Partnership> getHistoryEvents(Long influencerId) {
        List<Status> statusList = new ArrayList<>();
        statusList.add(Status.DECLINED);
        statusList.add(Status.DONE);
        return partnershipRepository.findByStatusInAndInfluencerId(statusList, influencerId);
    }

    //prix plateforme pays date
    public void getInfluencerStats(Long influencerId) {
       int totalPartnerships = partnershipRepository.countByInfluencerIdAndStatus(influencerId, Status.DONE);
    }
}
