package plateau.security.iam.customerinfoprovider;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class CIPSpi implements Spi {
    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "customer-info-spi";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return null;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return CIPProviderFactory.class;
    }
}
