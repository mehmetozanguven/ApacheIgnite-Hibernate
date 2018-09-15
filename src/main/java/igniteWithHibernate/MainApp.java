package igniteWithHibernate;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.store.CacheStoreSessionListener;
import org.apache.ignite.cache.store.hibernate.CacheHibernateStoreSessionListener;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;

import java.util.Iterator;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

public class MainApp {

    private static final String HIBERNATE_PATH ="hibernate.cfg.xml";
    private static final String IGNITE_CONFIG_PATH="ignite-configuration.xml";
    private static final String CACHE_NAME="sampleCache";

    /**
     * Prepare the cache configuration.
     * set hibernate session listener
     * @return configuration as CacheConfiguration
     */
    public static CacheConfiguration prepareHibernateCacheConfig(){
        CacheConfiguration<Integer, Person> cacheCfg = new CacheConfiguration<>(CACHE_NAME);

        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);
        cacheCfg.setCacheMode(CacheMode.REPLICATED);
        cacheCfg.setWriteBehindEnabled(true);
        cacheCfg.setAtomicityMode(TRANSACTIONAL);

        cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(PersonStore.class));

        cacheCfg.setCacheStoreSessionListenerFactories((Factory<CacheStoreSessionListener>) () -> {
            CacheHibernateStoreSessionListener lsnr = new CacheHibernateStoreSessionListener();

            lsnr.setHibernateConfigurationPath(HIBERNATE_PATH);

            return lsnr;
        });

        return cacheCfg;
    }

    public static void printCache(IgniteCache igniteCache){
        System.out.println("Print the all caches");
        Iterator<Cache.Entry<Integer, String>> cacheIterator = igniteCache.iterator();
        while (cacheIterator.hasNext()){
            Cache.Entry cacheEntry = cacheIterator.next();
            System.out.println("Key: " + cacheEntry.getKey() + ", value:" + cacheEntry.getValue());
        }
    }

    public static void main(String[] args) {
         /* To solve that:
              SEVERE: Failed to resolve default logging config file: config/java.util.logging.properties
        */
        System.setProperty("java.util.logging.config.file", "java.util.logging.properties");

        // create ignite node with specific config path
        Ignite ignite = IgniteFactory.createIgniteNodeWithSpecificConfiguration("s", IGNITE_CONFIG_PATH);

        // new person object
        Person person = new Person(3, "Jack", 34, 456.4);

        // create igniteCache with config setup
        IgniteCache igniteCache = ignite.getOrCreateCache(prepareHibernateCacheConfig());

        // put the new person to the cache and underlying database
        igniteCache.put(person.getPersonID(), person);

        // load the all datas in the database to the cache
        igniteCache.loadCache(null);

        // print the igniteCache
        printCache(igniteCache);


    }

}
