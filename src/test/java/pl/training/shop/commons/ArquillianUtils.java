package pl.training.shop.commons;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import javax.transaction.UserTransaction;
import java.util.Arrays;

public class ArquillianUtils {

    public static JavaArchive merge(JavaArchive javaArchive, String dependency) {
        var dependencies = Maven.resolver()
                .resolve(dependency)
                .withTransitivity()
                .as(JavaArchive.class);
        return Arrays.stream(dependencies)
                .reduce(javaArchive, Archive::merge);
    }

    public static void doInTransaction(UserTransaction userTransaction, Runnable operations) {
        try {
            userTransaction.begin();
            operations.run();
            userTransaction.commit();
        } catch (Exception exception) {
            throw new AssertionError(exception.getMessage());
        }
    }

}
