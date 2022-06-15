package plateau.security.iam.customerinfoprovider;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;
import plateau.security.iam.customerinfoprovider.resource.consecutive.ConsecutiveNumbersPasswordPolicyProviderFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ConsecutiveNumbersPasswordPolicyProviderTest extends PolicyBaseTest{

    private PasswordPolicyProvider provider = getProvider(new ConsecutiveNumbersPasswordPolicyProviderFactory());

    @ParameterizedTest
    @ValueSource(strings = {"132", "111","1659","111111"})
    void validate_WhenPasswordNotContainsConsecutiveNumbers_ThenReturnNull(String password) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(2);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNull(response);
    }

    @ParameterizedTest
    @MethodSource("provideParametersForContainsEvenOrOddConsecutiveNumbers")
    void validate_WhenPasswordContainsConsecutiveNumbersEqualToThresholdOrHigher_ThenReturnPolicyError(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNotNull(response);
    }

    private static Stream<Arguments> provideParametersForContainsEvenOrOddConsecutiveNumbers() {
        return Stream.of(
                Arguments.of("123", 2),
                Arguments.of("12", 2),
                Arguments.of("456", 3),
                Arguments.of("45678", 5)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "23456"})
    void validate_WhenPasswordContainsConsecutiveNumbersLessThanThreshold_ThenReturnNull(String password) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(10);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNull(response);
    }


    @ParameterizedTest
    @ValueSource(strings = {"123456", "23456"})
    void validate_WhenPasswordContainsConsecutiveNumbersWhileThresholdIs1_ThenReturnNull(String password) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(1);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNull(response);
    }
}
