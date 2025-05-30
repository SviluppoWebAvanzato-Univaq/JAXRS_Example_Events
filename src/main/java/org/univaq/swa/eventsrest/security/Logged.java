
package org.univaq.swa.eventsrest.security;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;


/**
 *
 * @author didattica
 * questa annotazione abilita uno specifico tipo di autneticazione sui metodi a cui
 * viene applicata. Si potrebbero anche creare annotazioni diverse per autenticazioni
 * di tipo diverso, ad esempio per ruoli particolari da controllare all'atto della
 * verifica della validità del token
 * 
 */
@NameBinding
@Retention(RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Logged {
    
}
