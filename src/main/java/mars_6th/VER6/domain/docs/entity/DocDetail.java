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

    private String content;

    private String version;

    // minio bucket 에 저장된 파일명
    private String fileName;

    // minio 등록되지 않은 외부 url
    private String fileUrl;

    private Long createdBy;

    @Enumerated(EnumType.STRING)
    private DocDetailStatus status;

    public static DocDetail fromFileName(Long userId, DocDetailRequest request, String fileName) {
        return DocDetail.builder()
                .content(request.content())
                .version(request.version())
                .fileName(fileName)
                .createdBy(userId)
                .build();
    }

    public static DocDetail fromFileUrl(Long userId, DocDetailRequest request, String fileUrl) {
        return DocDetail.builder()
                .content(request.content())
                .version(request.version())
                .fileUrl(fileUrl)
                .createdBy(userId)
                .build();
    }

    public void addDoc(Doc doc) {
        this.doc = doc;
        doc.getDocDetails().add(this);
    }

    public boolean existExternalUrl() {
        return fileUrl != null && !fileUrl.isBlank();
    }

    public void updateStatus(DocDetailStatus status) {
        this.status = status;
    }
}
