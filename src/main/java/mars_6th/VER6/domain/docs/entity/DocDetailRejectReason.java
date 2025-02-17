package mars_6th.VER6.domain.docs.entity;

import jakarta.persistence.*;
import lombok.*;
import mars_6th.VER6.global.utils.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "tbl_doc_detail_reject_reason")
public class DocDetailRejectReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tbl_doc_detail_id")
    private DocDetail docDetail;

    private String reason;

    public static DocDetailRejectReason from(DocDetail docDetail, String reason) {
        return DocDetailRejectReason.builder()
                .docDetail(docDetail)
                .reason(reason)
                .build();
    }

    public void addDocDetail(DocDetail docDetail) {
        this.docDetail = docDetail;
        docDetail.getRejectReasons().add(this);
    }
}
