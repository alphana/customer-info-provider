package plateau.security.iam.customerinfoprovider;

import org.junit.jupiter.api.BeforeEach;
import org.keycloak.models.*;
import org.keycloak.models.jpa.UserAdapter;
import org.keycloak.models.jpa.entities.UserEntity;
import org.keycloak.policy.PasswordPolicyProvider;
import org.keycloak.policy.PasswordPolicyProviderFactory;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class PolicyBaseTest {

    protected KeycloakSession mockSession = Mockito.mock(KeycloakSession.class);
    protected KeycloakContext mockContext = Mockito.mock(KeycloakContext.class);
    protected RealmModel mockRealm = Mockito.mock(RealmModel.class);
    protected PasswordPolicy mockPasswordPolicy = Mockito.mock(PasswordPolicy.class);
    protected UserProvider mockUserProvider = Mockito.mock(UserProvider.class);

    @BeforeEach
    void init() {
        when(mockContext.getRealm()).thenReturn(mockRealm);
        when(mockRealm.getPasswordPolicy()).thenReturn(mockPasswordPolicy);
        when(mockSession.getContext()).thenReturn(mockContext);
        when(mockSession.users()).thenReturn(mockUserProvider);
    }

    PasswordPolicyProvider getProvider(PasswordPolicyProviderFactory passwordPolicyProviderFactory){
        return passwordPolicyProviderFactory.create(mockSession);
    }

    protected UserModel generateTestUser(String userId, String userName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUsername(userName);
        UserModel userModel = new UserAdapter(null, null, null, userEntity);
        return userModel;
    }

    protected UserModel generateTestUser() {
        return generateTestUser("userId", "userName");
    }

}
