package plateau.security.iam.customerinfoprovider;


import plateau.security.iam.customerinfoprovider.model.CustomerRepresentation;

public class CIPProviderImpl implements CIPProvider{
    @Override
    public CustomerRepresentation findCustomerById(String id) {
//soapservisi çağrıları
        return null;
    }

    @Override
    public CustomerRepresentation findCustomerByNatId(String natId) {
        return null;
    }

    @Override
    public CustomerRepresentation findCustomerByEmail(String email) {
        return null;
    }

    @Override
    public void close() {

    }
}
