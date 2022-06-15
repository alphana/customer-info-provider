package plateau.security.iam.customerinfoprovider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;
import plateau.security.iam.customerinfoprovider.resource.reverseconsecutive.ReverseConsecutiveNumbersPasswordPolicyProviderFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ReverseConsecutiveNumbersPasswordPolicyProviderTest extends PolicyBaseTest{

    private PasswordPolicyProvider provider = getProvider(new ReverseConsecutiveNumbersPasswordPolicyProviderFactory());

    @ParameterizedTest
    @MethodSource("provideParametersForNotContainsReverseConsecutiveNumbers")
    void validate_WhenPasswordNotContainsReverseConsecutiveNumbers_ThenReturnNull(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNull(response);
    }

    private static Stream<Arguments> provideParametersForNotContainsReverseConsecutiveNumbers() {
        return Stream.of(
                Arguments.of("123", 2),
                Arguments.of("27946", 3),
                Arguments.of("3214", 4)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersContainReverseConsecutiveNumbersEqualToThresholdOrHigher")
    void validate_WhenPasswordContainsContainReverseConsecutiveNumbersEqualToThresholdOrHigher_ThenReturnPolicyError(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNotNull(response);
    }

    private static Stream<Arguments> provideParametersContainReverseConsecutiveNumbersEqualToThresholdOrHigher() {
        return Stream.of(
                Arguments.of("32", 2),
                Arguments.of("321", 2),
                Arguments.of("6543", 3),
                Arguments.of("54321", 5)
        );
    }

    @Test
    void validate_WhenPasswordContainsReverseConsecutiveNumbersLessThanThreshold_ThenReturnNull() {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(3);

        PolicyError response = provider.validate(generateTestUser().getUsername(), "32");

        assertNull(response);
    }

    @Test
    void validate_WhenPasswordContainsReverseConsecutiveNumbersWhileThresholdIs1_ThenReturnNull() {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(1);

        PolicyError response = provider.validate(generateTestUser().getUsername(), "76543");

        assertNull(response);
    }

}
