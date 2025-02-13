package mars_6th.VER6.domain.docs.entity;

import jakarta.persistence.*;
import lombok.*;
import mars_6th.VER6.global.utils.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "tbl_doc_req")
public class DocRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tbl_doc_id")
    private Doc doc;

    private String title;

    private String content;

    private String assignee; // 요청 담당자

    @Enumerated(EnumType.STRING)
    private DocRequestStatus status;

    public void addDoc(Doc doc) {
        this.doc = doc;
        doc.getDocRequests().add(this);
    }

    public void updateStatus(DocRequestStatus status) {
        this.status = status;
    }

}
