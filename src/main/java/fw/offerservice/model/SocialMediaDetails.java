package fw.offerservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialMediaDetails {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialMediaList name;

    private int stories;
    private int storyPrice;

    private int posts;
    private int postPrice;

    private int highlights;
    private int highlightPrice;

}
