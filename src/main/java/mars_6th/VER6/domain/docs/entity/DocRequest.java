package mars_6th.VER6.domain.docs.entity;

import jakarta.persistence.*;
import lombok.*;
import mars_6th.VER6.global.utils.BaseEntity;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DocRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "docRequest")
    private List<Doc> docs;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private DocRequestStatus status;

    private Long createdBy;
}
