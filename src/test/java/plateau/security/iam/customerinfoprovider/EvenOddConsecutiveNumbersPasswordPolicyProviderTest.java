package plateau.security.iam.customerinfoprovider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;
import plateau.security.iam.customerinfoprovider.resource.consecutiveevenodd.EvenOddConsecutiveNumbersPasswordPolicyProviderFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class EvenOddConsecutiveNumbersPasswordPolicyProviderTest extends PolicyBaseTest{

    private PasswordPolicyProvider provider = getProvider(new EvenOddConsecutiveNumbersPasswordPolicyProviderFactory());

    @ParameterizedTest
    @MethodSource("provideParametersForNotContainsEvenOrOddConsecutiveNumbers")
    void validate_WhenPasswordNotContainsEvenOrOddConsecutiveNumbers_ThenReturnNull(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNull(response);
    }

    private static Stream<Arguments> provideParametersForNotContainsEvenOrOddConsecutiveNumbers() {
        return Stream.of(
                Arguments.of("123", 2),
                Arguments.of("123", 3),
                Arguments.of("3210", 2)
        );
    }



    @ParameterizedTest
    @MethodSource("provideParametersForContainsEvenOrOddConsecutiveNumbers")
    void validate_WhenPasswordContainsEvenOddConsecutiveNumbersEqualToThresholdOrHigher_ThenReturnPolicyError(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNotNull(response);
    }

    private static Stream<Arguments> provideParametersForContainsEvenOrOddConsecutiveNumbers() {
        return Stream.of(
                Arguments.of("246", 2),
                Arguments.of("246", 3),
                Arguments.of("135", 3),
                Arguments.of("1357", 3)
        );
    }

    @Test
    void validate_WhenPasswordContainsEvenOddConsecutiveNumbersLessThanThreshold_ThenReturnNull() {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(3);

        PolicyError response = provider.validate(generateTestUser().getUsername(), "13");

        assertNull(response);
    }


    @ParameterizedTest
    @ValueSource(strings = {"2468", "1357"})
    void validate_WhenPasswordContainsEvenOddConsecutiveNumbersWhileThresholdIs1_ThenReturnNull(String password) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(1);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNull(response);
    }

}
