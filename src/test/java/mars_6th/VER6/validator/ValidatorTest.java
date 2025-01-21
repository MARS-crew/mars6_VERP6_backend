package mars_6th.VER6.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import mars_6th.VER6.domain.docs.controller.dto.request.DocRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NoSpaceSizeValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenTitleIsValid_thenNoViolations() {
        // given
        DocRequest request = new DocRequest("ValidTitle");

        // when
        Set<ConstraintViolation<DocRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void whenTitleHasSpacesAndValidLength_thenNoViolations() {
        // given
        DocRequest request = new DocRequest("Valid Title With Spaces");

        // when
        Set<ConstraintViolation<DocRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void whenTitleIsTooShort_thenViolations() {
        // given
        DocRequest request = new DocRequest("");

        // when
        Set<ConstraintViolation<DocRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("공백을 제외한 글자 수는 1자 이상 20자 이하여야 합니다.");
    }

    @Test
    void whenTitleIsTooLong_thenViolations() {
        // given
        DocRequest request = new DocRequest("ThisTitleIsDefinitelyWayTooLongForValidation");

        // when
        Set<ConstraintViolation<DocRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("공백을 제외한 글자 수는 1자 이상 20자 이하여야 합니다.");
    }

    @Test
    void whenTitleOnlyHasSpaces_thenViolations() {
        // given
        DocRequest request = new DocRequest("     "); // Only spaces

        // when
        Set<ConstraintViolation<DocRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("공백을 제외한 글자 수는 1자 이상 20자 이하여야 합니다.");
    }

    @Test
    void whenTitleIsNull_thenViolations() {
        // given
        DocRequest request = new DocRequest(null);

        // when
        Set<ConstraintViolation<DocRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("공백을 제외한 글자 수는 1자 이상 20자 이하여야 합니다.");
    }
}