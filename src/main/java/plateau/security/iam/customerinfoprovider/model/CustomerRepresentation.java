package plateau.security.iam.customerinfoprovider.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerRepresentation {
    private String id;
    private String name;
    private List<String> attrs;
}
