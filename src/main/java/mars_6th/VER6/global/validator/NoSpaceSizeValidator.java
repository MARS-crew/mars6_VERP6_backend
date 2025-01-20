package mars_6th.VER6.global.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoSpaceSizeValidator implements ConstraintValidator<NoSpaceSize, String> {
    private int min;
    private int max;

    @Override
    public void initialize(NoSpaceSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // null 값은 허용하지 않음
        }

        // 공백 제거 후 길이 확인
        int lengthWithoutSpaces = value.replace(" ", "").length();
        return lengthWithoutSpaces >= min && lengthWithoutSpaces <= max;
    }
}