package igniteWithHibernate;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class IgniteFactory {
    private IgniteFactory() {}

    private static String DEFAULT_CONFIG_PATH ="/path/to/apache-ignite-fabric-2.5.0-bin" +
            "/examples/config/example-ignite.xml";


    public static Ignite createIgniteNodeWithDefaultConfiguration(String nodeType){
        if (nodeType.equalsIgnoreCase("c")){

            Ignition.setClientMode(true);
        }

        try{
            System.out.println("Ignite node have created successfully");
            return Ignition.start(DEFAULT_CONFIG_PATH);

        }
        catch (IgniteException e){
            System.out.println("Can not create Ignite node");
            e.printStackTrace();
        }

        return null;
    }


    public static Ignite createIgniteNodeWithSpecificConfiguration(String nodeType, String configFilePath){
        if (nodeType.equalsIgnoreCase("c"))
            Ignition.setClientMode(true);
        try{
            System.out.println("Ignite node have created successfully");
            return Ignition.start(configFilePath);

        }
        catch (IgniteException e){
            System.out.println("Can not create Ignite node");
            e.printStackTrace();
        }

        return null;
    }

    public static Ignite createIgniteNodeWithSpecificConfiguration(String nodeType, IgniteConfiguration igniteConfiguration){

        if (nodeType.equalsIgnoreCase("c"))
            Ignition.setClientMode(true);

        try{
            System.out.println("Ignite Node have created successfully");
            return Ignition.start(igniteConfiguration);
        }
        catch (IgniteException e){
            System.out.println("Can not create Ignite node");
            e.printStackTrace();
        }
        return null;
    }
}