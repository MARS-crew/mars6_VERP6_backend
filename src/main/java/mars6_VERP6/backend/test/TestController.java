package mars6_VERP6.backend.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "테스트 API")
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    @Operation(summary = "테스트", description = "테스트 api")
    public String test(){
        return "Test Complete";
    }
}
