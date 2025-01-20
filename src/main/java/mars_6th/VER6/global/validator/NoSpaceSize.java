package mars_6th.VER6.global.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoSpaceSizeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpaceSize {
    int min() default 0;
    int max() default Integer.MAX_VALUE;

    String message() default "공백을 제외한 글자 수는 {min}자 이상 {max}자 이하여야 합니다.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}