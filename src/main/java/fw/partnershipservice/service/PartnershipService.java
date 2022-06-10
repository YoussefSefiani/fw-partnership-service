package fw.partnershipservice.service;

import fw.partnershipservice.feign.FileRestConsumer;
import fw.partnershipservice.feign.UserRestConsumer;
import fw.partnershipservice.model.*;
import fw.partnershipservice.repository.PartnershipRepository;
import fw.partnershipservice.repository.SocialMediaDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PartnershipService {

    private final PartnershipRepository partnershipRepository;
    private final SocialMediaDetailsRepository socialMediaDetailsRepository;
    private final UserRestConsumer userRestConsumer;
    private final FileRestConsumer fileRestConsumer;

    @Autowired
    public PartnershipService(PartnershipRepository partnershipRepository, SocialMediaDetailsRepository socialMediaDetailsRepository, UserRestConsumer userRestConsumer, FileRestConsumer fileRestConsumer) {
        this.partnershipRepository = partnershipRepository;
        this.socialMediaDetailsRepository = socialMediaDetailsRepository;
        this.userRestConsumer = userRestConsumer;
        this.fileRestConsumer = fileRestConsumer;
    }

    public List<Partnership> getInfluencerPartnerships(Long influencerId) {
        List<Partnership> partnershipsList = partnershipRepository.findByInfluencerId(influencerId);
        BrandIdWrapper brandIds = new BrandIdWrapper();
        partnershipsList.forEach(partnership -> {
            brandIds.getBrandIds().add(partnership.getBrandId());
        });
        System.out.println(brandIds);
        userRestConsumer.getAllPartnershipBrandNames(brandIds).forEach((key, value) -> {
            partnershipsList.forEach(partnership -> {
                if(Objects.equals(partnership.getBrandId(), key)) {
                    partnership.setBrandName(value);
                }
            });
        });
        return partnershipsList;
    }

    public List<Partnership> getBrandPartnerships(Long brandId) {

        List<Partnership> partnershipsList = partnershipRepository.findByBrandId(brandId);
        InfluencerIdWrapper influencerIds = new InfluencerIdWrapper();
        partnershipsList.forEach(partnership -> {
            influencerIds.getInfluencerIds().add(partnership.getInfluencerId());
        });
        System.out.println(influencerIds);
        userRestConsumer.getAllPartnershipInfluencerNames(influencerIds).forEach((key, value) -> {
            partnershipsList.forEach(partnership -> {
                if(Objects.equals(partnership.getInfluencerId(), key)) {
                    partnership.setInfluencerName(value);
                }
            });
        });
        return partnershipsList;

    }

    public Long addPartnership(Partnership partnership, String token) {

        partnership.setStatus(Status.REQUESTED);
        Long partnershipId = partnershipRepository.save(partnership).getId();
       // fileRestConsumer.uploadFile(partnership.getFile(), partnershipId, token);
        return partnershipId;
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

    public List<Partnership> getPendingPartnerships(Long brandId) {
        return partnershipRepository.findByStatusAndBrandId(Status.PENDING, brandId);
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
        List<Integer> totalMoneyEarnedMonthList = new ArrayList<>();
        List<Integer> totalMoneyEarnedWeekList = new ArrayList<>();

        List<Integer> totalPartnershipsMonthList = new ArrayList<>();
        List<Integer> totalPartnershipsWeekList = new ArrayList<>();

        HashMap<String, Integer> topSocialMediaMap = new HashMap<>();
        HashMap<Integer, Integer> topMonths = new HashMap<>();

        HashMap<String, Integer> totalEarningsGraphDataMap = new HashMap<>();
        HashMap<String, Integer> totalEarningsMonthGraphDataMap = new HashMap<>();
        HashMap<String, Integer> totalEarningsWeekGraphDataMap = new HashMap<>();

        partnerships.forEach(partnership -> {

            if(currentMonthChecker(partnership.getFinishDate())) {
                totalPartnershipsMonthList.add(1);
            }

            if(currentWeekChecker(partnership.getFinishDate())) {
                totalPartnershipsWeekList.add(1);
            }

            Long partnershipId = partnership.getId();
            List<SocialMediaDetails> socialMediaDetails = socialMediaDetailsRepository.findByPartnershipId(partnershipId);
            System.out.println(partnership);
            socialMediaDetails.forEach(socialMediaDetail -> {

                getMonthlyEarnings(topMonths, socialMediaDetail, partnership.getFinishDate());
                System.out.println(socialMediaDetail);
                totalMoneyEarnedList.add(socialMediaDetailsTotalEarned(socialMediaDetail));
                if(currentMonthChecker(partnership.getFinishDate())) {
                    totalMoneyEarnedMonthList.add(socialMediaDetailsTotalEarned(socialMediaDetail));
                }
                if(currentWeekChecker(partnership.getFinishDate())) {
                    totalMoneyEarnedWeekList.add(socialMediaDetailsTotalEarned(socialMediaDetail));
                }
                totalSocialMedia(topSocialMediaMap, socialMediaDetail);
                earningsGraphData(totalEarningsGraphDataMap, partnership.getFinishDate(), socialMediaDetail);
                if(currentMonthChecker(partnership.getFinishDate())) {
                    earningsGraphData(totalEarningsMonthGraphDataMap, partnership.getFinishDate(), socialMediaDetail);
                }
                if(currentWeekChecker(partnership.getFinishDate())) {
                    earningsGraphData(totalEarningsWeekGraphDataMap, partnership.getFinishDate(), socialMediaDetail);
                }
            });
        });

        influencerStats.setInfluencerId(influencerId);
        influencerStats.setTotalPartnerships(getTotalPartnerships(influencerId));
        influencerStats.setTotalMoneyEarned(totalMoneyEarnedList.stream().mapToInt(Integer::intValue).sum());
        influencerStats.setTopSocialMedia(topSocialMediaMap);
        influencerStats.setTotalPartnershipsMonth(totalPartnershipsMonthList.stream().mapToInt(Integer::intValue).sum());
        influencerStats.setTotalPartnershipsWeek(totalPartnershipsWeekList.stream().mapToInt(Integer::intValue).sum());
        influencerStats.setTotalMoneyEarnedMonth(totalMoneyEarnedMonthList.stream().mapToInt(Integer::intValue).sum());
        influencerStats.setTotalMoneyEarnedWeek(totalMoneyEarnedWeekList.stream().mapToInt(Integer::intValue).sum());
        influencerStats.setTotalEarningsGraphData(totalEarningsGraphDataMap);
        influencerStats.setTotalEarningsMonthGraphData(totalEarningsMonthGraphDataMap);
        influencerStats.setTotalEarningsWeekGraphData(totalEarningsWeekGraphDataMap);
        influencerStats.setTopMonths(topMonths);

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

    private void getMonthlyEarnings(HashMap<Integer, Integer> topMonths ,SocialMediaDetails socialMediaDetail, Date date) {
        int currentMonth = getMonthFromGivenDate(date);
        System.out.println("CURRENT MONTH: " + currentMonth);
        topMonths.putIfAbsent(currentMonth, 0);
        topMonths.merge(currentMonth, socialMediaDetailsTotalEarned(socialMediaDetail), Integer::sum);
    }

    private void earningsGraphData(HashMap<String, Integer> graphDataMap, Date finishDate, SocialMediaDetails socialMediaDetail) {
        System.out.println("here graph map " + graphDataMap);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        graphDataMap.putIfAbsent(dateFormat.format(finishDate), 0);
        graphDataMap.merge(dateFormat.format(finishDate), socialMediaDetailsTotalEarned(socialMediaDetail), Integer::sum);
    }

    private boolean currentMonthChecker(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date);
        cal2.setTime(new Date());
        if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            return cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        }
        return false;
    }

    private int getMonthFromGivenDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static boolean currentWeekChecker(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }
}
