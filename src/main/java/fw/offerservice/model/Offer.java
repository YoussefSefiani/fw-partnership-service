package fw.offerservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
public class Offer {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    private String brandId;
    private String influencerId;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn(name = "offer_id")
    @OneToMany(cascade = CascadeType.ALL)
    private List<SocialMediaDetails> socialMediaDetails;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "yyyy:dd:MM")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy:dd:MM")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "yyyy:dd:MM")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy:dd:MM")
    private Date endDate;

    private String file;

}
