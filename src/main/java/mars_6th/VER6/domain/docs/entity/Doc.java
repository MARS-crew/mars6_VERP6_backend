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
@Table(name = "tbl_doc")
public class Doc extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_type_id")
    private DocType docType;

    @OneToMany(mappedBy = "doc", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocRequest> docRequest;

    private String title;

    private String content;

    private String version;

    private String fileName;

    private String fileUrl;

    private Long createdBy;

    public void addDocType(DocType docType) {
        this.docType = docType;
        docType.getDocs().add(this);
    }

    public Doc updateName(String title) {
        this.title = title;
        return this;
    }

    public Doc updateVersion(String version) {
        this.version = version;
        return this;
    }

    public Doc updateFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Doc updateContent(String content) {
        this.content = content;
        return this;
    }

    public Doc updateFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }
}
