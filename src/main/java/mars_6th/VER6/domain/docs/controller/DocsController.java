package mars_6th.VER6.domain.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mars_6th.VER6.domain.docs.entity.Docs;
import mars_6th.VER6.domain.docs.service.DocsService;
import mars_6th.VER6.domain.exception.DocsExceptionType;
import mars_6th.VER6.global.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Docs", description = "Docs API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocsController {

    private final DocsService docsService;

    @Operation(summary = "saveDocs", description = "saveDocs")
    @GetMapping("/save")
    public void saveDocs() {
        docsService.saveDocs();
    }

    // for Test
    @Operation(summary = "getDocs", description = "getDocs")
    @GetMapping("/get")
    public void getDocs() {
        throw new BaseException(DocsExceptionType.NOT_FOUND);
    }

    @Operation(summary = "updateDocs", description = "updateDocs")
    @GetMapping("/update")
    public ResponseEntity<Docs> updateDocs() {
        return ResponseEntity.ok(Docs.builder().id(1L).title("test").build());
    }
}
