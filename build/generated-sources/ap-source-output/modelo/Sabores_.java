package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Productos;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-26T08:16:37")
@StaticMetamodel(Sabores.class)
public class Sabores_ { 

    public static volatile CollectionAttribute<Sabores, Productos> productosCollection;
    public static volatile SingularAttribute<Sabores, Integer> id;
    public static volatile SingularAttribute<Sabores, String> nombreSabor;
    public static volatile SingularAttribute<Sabores, Integer> status;

}