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
@Table(name = "tbl_doc")
public class Doc extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tbl_doc_type_id")
    private DocType docType;

    @OneToMany(mappedBy = "doc", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocDetail> docDetails;

    @OneToMany(mappedBy = "doc", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocRequest> docRequests;

    @Column(unique = true)
    private String title;

    @Builder.Default
    private boolean isUpdated = false;

    public void addDocType(DocType docType) {
        this.docType = docType;
        docType.getDocs().add(this);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void markAsUpdated() {
        this.isUpdated = true;
    }

    public void markAsRead() {
        this.isUpdated = false;
    }
}
