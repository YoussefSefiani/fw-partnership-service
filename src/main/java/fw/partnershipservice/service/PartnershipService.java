package fw.partnershipservice.service;

import fw.partnershipservice.feign.PaymentRestConsumer;
import fw.partnershipservice.model.*;
import fw.partnershipservice.repository.PartnershipRepository;
import fw.partnershipservice.repository.SocialMediaDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PartnershipService {

    private final PartnershipRepository partnershipRepository;
    private final SocialMediaDetailsRepository socialMediaDetailsRepository;
    private final PaymentRestConsumer consumer;

    @Autowired
    public PartnershipService(PartnershipRepository partnershipRepository, SocialMediaDetailsRepository socialMediaDetailsRepository, PaymentRestConsumer consumer) {
        this.partnershipRepository = partnershipRepository;
        this.socialMediaDetailsRepository = socialMediaDetailsRepository;
        this.consumer = consumer;
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

    public void payPartnership(Long partnershipId, CheckoutPayment payment) {
        consumer.payPartnership(partnershipId, payment);
    }


    public void validatePartnership(Long partnershipId) {
        Partnership partnership = partnershipRepository.findById(partnershipId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("partnership with id %s does not exist", partnershipId))
                );

        partnership.setStatus(Status.IN_PROGRESS);
        partnershipRepository.save(partnership);
    }

    public void finishPartnership(Long partnershipId) {
        Partnership partnership = partnershipRepository.findById(partnershipId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("partnership with id %s does not exist", partnershipId))
                );

        partnership.setStatus(Status.DONE);

        Date date = new Date();
        partnership.setFinishDate(date);

        partnershipRepository.save(partnership);
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

    public Stats getInfluencerStats(Long influencerId) {

        Stats influencerStats = new Stats();

        List<Partnership> partnerships = partnershipRepository.findByStatusAndInfluencerId(Status.DONE, influencerId);
        List<Integer> totalMoneyEarnedList = new ArrayList<>();
        List<Integer> totalPartnershipsMonthList = new ArrayList<>();
        List<Integer> totalMoneyEarnedMonthList = new ArrayList<>();
        HashMap<String, Integer> topSocialMediaMap = new HashMap<>();
        HashMap<String, Integer> totalEarningsGraphDataMap = new HashMap<>();

        partnerships.forEach(partnership -> {

            if(currentMonthChecker(partnership.getFinishDate())) {
                totalPartnershipsMonthList.add(1);
            }

            Long partnershipId = partnership.getId();
            List<SocialMediaDetails> socialMediaDetails = socialMediaDetailsRepository.findByPartnershipId(partnershipId);
            System.out.println(partnership);
            socialMediaDetails.forEach(socialMediaDetail -> {

                System.out.println(socialMediaDetail);
                totalMoneyEarnedList.add(socialMediaDetailsTotalEarned(socialMediaDetail));
                if(currentMonthChecker(partnership.getFinishDate())) {
                    totalMoneyEarnedMonthList.add(socialMediaDetailsTotalEarned(socialMediaDetail));
                }
                totalSocialMedia(topSocialMediaMap, socialMediaDetail);
                totalEarningsGraphData(totalEarningsGraphDataMap, partnership.getFinishDate(), socialMediaDetail);
            });
        });

        influencerStats.setInfluencerId(influencerId);
        influencerStats.setTotalPartnerships(getTotalPartnerships(influencerId));
        influencerStats.setTotalMoneyEarned(totalMoneyEarnedList.stream().mapToInt(Integer::intValue).sum());
        influencerStats.setTopSocialMedia(topSocialMediaMap);
        influencerStats.setTotalPartnershipsMonth(totalPartnershipsMonthList.stream().mapToInt(Integer::intValue).sum());
        influencerStats.setTotalMoneyEarnedMonth(totalMoneyEarnedMonthList.stream().mapToInt(Integer::intValue).sum());
        influencerStats.setTotalEarningsGraphData(totalEarningsGraphDataMap);

        return influencerStats;
    }

    public int getTotalPartnerships(Long influencerId) {
        return partnershipRepository.countByStatusAndInfluencerId(Status.DONE, influencerId);
    }

    private Integer socialMediaDetailsTotalEarned(SocialMediaDetails socialMediaDetail) {
        Integer highlightsMoneyEarned = socialMediaDetail.getHighlights() * socialMediaDetail.getHighlightPrice();
        Integer postsMoneyEarned = socialMediaDetail.getPosts() * socialMediaDetail.getPostPrice();
        Integer storiesMoneyEarned = socialMediaDetail.getStories() * socialMediaDetail.getStoryPrice();
        return highlightsMoneyEarned + postsMoneyEarned + storiesMoneyEarned;
    }

    private void totalSocialMedia(HashMap<String, Integer> socialMediaMap, SocialMediaDetails socialMediaDetail) {
        socialMediaMap.putIfAbsent(socialMediaDetail.getName().name(), 0);
        socialMediaMap.merge(socialMediaDetail.getName().name(), 1, Integer::sum);

    }

    private void totalEarningsGraphData(HashMap<String, Integer> totalEarningsGraphDataMap, Date finishDate, SocialMediaDetails socialMediaDetail) {
        System.out.println("here graph map " + totalEarningsGraphDataMap);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        totalEarningsGraphDataMap.putIfAbsent(dateFormat.format(finishDate), 0);
        totalEarningsGraphDataMap.merge(dateFormat.format(finishDate), socialMediaDetailsTotalEarned(socialMediaDetail), Integer::sum);

    }

    private boolean currentMonthChecker(Date finishDatePartnership) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(finishDatePartnership);
        cal2.setTime(new Date());

        if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            return cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        }
        return false;
    }
}
