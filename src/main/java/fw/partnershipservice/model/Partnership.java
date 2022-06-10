package fw.partnershipservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Partnership {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    private Long brandId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String brandName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String influencerName;

    private Long influencerId;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn(name = "partnership_id")
    @OneToMany(cascade = CascadeType.ALL)
    private List<SocialMediaDetails> socialMediaDetails;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "yyyy-MM-dd")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "yyyy-MM-dd")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "yyyy-MM-dd")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date finishDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private MultipartFile file;

    public Partnership(Long id, String brandName, String description, Status status, List<SocialMediaDetails> socialMediaDetails, Date startDate, Date endDate, Date finishDate, MultipartFile file) {
        this.id = id;
        this.brandName = brandName;
        this.description = description;
        this.status = status;
        this.socialMediaDetails = socialMediaDetails;
        this.startDate = startDate;
        this.endDate = endDate;
        this.finishDate = finishDate;
        this.file = file;
    }

}
