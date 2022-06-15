package plateau.security.iam.customerinfoprovider;

import org.keycloak.provider.Provider;
import plateau.security.iam.customerinfoprovider.model.CustomerRepresentation;

public interface CIPProvider extends Provider {
/**
 * TODO:
 * MBY den tuketilecek servisler buraya yazilacak
 * Asagidakiler sadece ornektir silin!!!!
 *
 */

    CustomerRepresentation findCustomerById(String id);
    CustomerRepresentation findCustomerByNatId(String natId);
    CustomerRepresentation findCustomerByEmail(String email);
}
