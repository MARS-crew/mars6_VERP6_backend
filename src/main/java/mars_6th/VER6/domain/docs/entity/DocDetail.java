package mars_6th.VER6.domain.docs.entity;

import jakarta.persistence.*;
import lombok.*;
import mars_6th.VER6.domain.docs.controller.dto.request.DocDetailRequest;
import mars_6th.VER6.global.utils.BaseEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "tbl_doc_detail")
public class DocDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tbl_doc_id")
    private Doc doc;

    @OneToMany(mappedBy = "docDetail", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocDetailRejectReason> rejectReasons;

    private String content;

    private String version;

    // minio bucket 에 저장된 파일명
    private String uploadFileUrl;

    // 사용자가 입력한 파일명
    private String originalFileName;

    // minio 등록되지 않은 외부 url
    private String externalFileUrl;

    private Long createdBy;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private DocDetailStatus status = DocDetailStatus.PENDING;

    public static DocDetail fromFileName(Long userId, DocDetailRequest request, String uploadFileUrl, String fileName) {
        return DocDetail.builder()
                .content(request.content())
                .version(request.version())
                .uploadFileUrl(uploadFileUrl)
                .originalFileName(fileName)
                .createdBy(userId)
                .build();
    }

    public static DocDetail fromFileUrl(Long userId, DocDetailRequest request, String externalFileUrl) {
        return DocDetail.builder()
                .content(request.content())
                .version(request.version())
                .externalFileUrl(externalFileUrl)
                .originalFileName(externalFileUrl)
                .createdBy(userId)
                .build();
    }

    public void addDoc(Doc doc) {
        this.doc = doc;
        doc.getDocDetails().add(this);
    }

    public boolean existExternalUrl() {
        return externalFileUrl != null && !externalFileUrl.isBlank();
    }

    public void updateStatus(DocDetailStatus status) {
        this.status = status;
    }
}
