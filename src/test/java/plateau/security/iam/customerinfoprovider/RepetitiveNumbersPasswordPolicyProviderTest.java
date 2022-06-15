package plateau.security.iam.customerinfoprovider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PolicyError;
import plateau.security.iam.customerinfoprovider.resource.repretitive.RepetitiveNumbersPasswordPolicyProviderFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RepetitiveNumbersPasswordPolicyProviderTest extends PolicyBaseTest {

    private PasswordPolicyProvider provider = getProvider(new RepetitiveNumbersPasswordPolicyProviderFactory());

    @ParameterizedTest
    @MethodSource("provideParametersForNotContainsRepetitiveNumbers")
    void validate_WhenPasswordNotContainsRepetitiveNumbers_ThenReturnNull(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNull(response);
    }

    private static Stream<Arguments> provideParametersForNotContainsRepetitiveNumbers() {
        return Stream.of(
                Arguments.of("12", 2),
                Arguments.of("113", 3),
                Arguments.of("123456", 2)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersContainsRepetitiveNumbersEqualToThresholdOrHigher")
    void validate_WhenPasswordContainsRepetitiveNumbersEqualToThresholdOrHigher_ThenReturnPolicyError(String password, Integer threshold) {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(threshold);

        PolicyError response = provider.validate(generateTestUser().getUsername(), password);

        assertNotNull(response);
    }

    private static Stream<Arguments> provideParametersContainsRepetitiveNumbersEqualToThresholdOrHigher() {
        return Stream.of(
                Arguments.of("11", 2),
                Arguments.of("111", 2),
                Arguments.of("1111", 3),
                Arguments.of("11111", 5)
        );
    }

    @Test
    void validate_WhenPasswordContainsRepetitiveNumbersLessThanThreshold_ThenReturnNull() {
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(3);

        PolicyError response = provider.validate(generateTestUser().getUsername(), "11");

        assertNull(response);
    }

    @Test
    void validate_WhenPasswordContainsRepetitiveNumbersWhileThresholdIs1_ThenReturnNull() {
        UserModel userModel = generateTestUser("userId", "userName");
        when(mockPasswordPolicy.getPolicyConfig(anyString())).thenReturn(1);
        when(mockUserProvider.getUserByUsername(any(), any(RealmModel.class))).thenReturn(userModel);

        PolicyError response = provider.validate(userModel.getUsername(), "1111111");
        assertNull(response);
    }

}
