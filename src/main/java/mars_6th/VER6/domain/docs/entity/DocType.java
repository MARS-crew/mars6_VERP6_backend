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
@Table(name = "tbl_doc_type")
public class DocType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "docType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doc> docs;

    @Column(unique = true)
    private String name;

    private Long createdBy;

    public void updateName(String name) {
        this.name = name;
    }
}
