package mars_6th.VER6.domain.docs.entity;

import jakarta.persistence.*;
import lombok.*;
import mars_6th.VER6.global.utils.BaseEntity;

import java.util.List;

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
    @JoinColumn(name = "doc_id")
    private Doc doc;

    private String name;

    private String title;

    private String content;

    private String fileName;

    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private DocRequestStatus status;

    private Long createdBy;

    public void changeStatus(DocRequestStatus newStatus) {
        this.status = newStatus;
    }

    public DocRequest updateFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public DocRequest updateFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public DocRequest updateContent(String content) {
        this.content = content;
        return this;
    }

    public DocRequest updateName(String name) {
        this.name = name;
        return this;
    }
}
