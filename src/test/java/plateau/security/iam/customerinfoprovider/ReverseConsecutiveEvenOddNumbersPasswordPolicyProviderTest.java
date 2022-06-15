package plateau.security.iam.customerinfoprovider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;
import plateau.security.iam.customerinfoprovider.resource.reverseconsecutiveevenodd.ReverseConsecutiveEvenOddNumbersPasswordPolicyProviderFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ReverseConsecutiveEvenOddNumbersPasswordPolicyProviderTest extends PolicyBaseTest{

    private PasswordPolicyProvider provider = getProvider(new ReverseConsecutiveEvenOddNumbersPasswordPolicyProviderFactory());


    @ParameterizedTest
    @MethodSource("provideParametersForNotContainsReverseConsecutiveEvenOddNumbers")
    void validate_WhenPasswordNotContainsReverseConsecutiveEvenOddNumbers_ThenReturnNull(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNull(response);
    }

    private static Stream<Arguments> provideParametersForNotContainsReverseConsecutiveEvenOddNumbers() {
        return Stream.of(
                Arguments.of("123", 2),
                Arguments.of("64", 3),
                Arguments.of("12321", 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersContainsReverseConsecutiveEvenOddNumbersEqualToThresholdOrHigher")
    void validate_WhenPasswordContainsReverseConsecutiveEvenOddNumbersEqualToThresholdOrHigher_ThenReturnPolicyError(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNotNull(response);
    }

    private static Stream<Arguments> provideParametersContainsReverseConsecutiveEvenOddNumbersEqualToThresholdOrHigher() {
        return Stream.of(
                Arguments.of("53", 2),
                Arguments.of("864", 2),
                Arguments.of("6420", 3),
                Arguments.of("97531", 5)
        );
    }

    @Test
    void validate_WhenPasswordContainsReverseConsecutiveEvenOddNumbersNumbersLessThanThreshold_ThenReturnNull() {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(3);

        PolicyError response = provider.validate(generateTestUser().getUsername(), "75");

        assertNull(response);
    }

    @Test
    void validate_WhenPasswordContainsReverseConsecutiveEvenOddNumbersWhileThresholdIs1_ThenReturnNull() {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(1);

        PolicyError response = provider.validate(generateTestUser().getUsername(), "85420");

        assertNull(response);
    }
}
