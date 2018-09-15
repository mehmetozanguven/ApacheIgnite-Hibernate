package igniteWithHibernate;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.cache.store.CacheStoreSession;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.CacheStoreSessionResource;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.List;

public class PersonStore extends CacheStoreAdapter<Integer, Person> {
    /**
     * requires this annotation to connect the database
     * via CacheStoreSession.attachment()
     */
    @CacheStoreSessionResource
    private CacheStoreSession cacheStoreSession;

    /**
     * igniteCache.get(..) method calls this method
     * get the data from the database according to the key
     * @param key is the key which comes from get() method
     * @return corresponding Person from the database
     * @throws CacheLoaderException if there is no data according to the key.
     */
    public Person load(Integer key) throws CacheLoaderException {
        System.out.println("Hit the load(..) method\n\n");

        Session hibernateSession = cacheStoreSession.attachment();

        try {
            return (Person) hibernateSession.get(Person.class, key);
        }
        catch (HibernateException e) {
            throw new CacheLoaderException("Failed to load value from cache store [key=" + key + ']', e);
        }
    }


    /**
     * igniteCache.put(key, value) method calls this method.
     * put the data(value) to the database
     * @param entry is the (key, value)
     * @throws CacheWriterException
     */
    public void write(Cache.Entry<? extends Integer, ? extends Person> entry) throws CacheWriterException {
        System.out.println("Hit the write(..) method\n\n");
        int key = entry.getKey();
        Person value = entry.getValue();

        Session hibernateSession = cacheStoreSession.attachment();
        try {
            hibernateSession.saveOrUpdate(value);
        }
        catch (HibernateException e) {
            throw new CacheWriterException("Failed to put value to cache store [key=" + key + ", val" + value + "]\n", e);
        }
    }


    /**
     * igniteCache.remove(key) method calls this method
     * removee the data from database
     * @param key is the id
     * @throws CacheWriterException
     */
    public void delete(Object key) throws CacheWriterException {
        System.out.println("Hit the delete(..) method\n\n");

        Session hibernateSession = cacheStoreSession.attachment();

        try {
            hibernateSession.createQuery("delete " + Person.class.getSimpleName() + " where id = :key").
                    setParameter("key", key).
                    executeUpdate();
        }
        catch (HibernateException e) {
            throw new CacheWriterException("Failed to remove value from cache store [key=" + key + ']', e);
        }
    }


    /**
     * Loads the table to the cache
     * @param clo
     * @param args
     */
    public void loadCache (IgniteBiInClosure<Integer, Person> clo, Object... args){
        System.out.println("Hit the loadCache(..) method\n\n");
        Session hibernateSession = cacheStoreSession.attachment();

        try {
            List list = hibernateSession.createCriteria(Person.class).list();

            for (Object eachEmployee :list){
                Person person = (Person) eachEmployee;
                clo.apply(person.getPersonID(), person);
            }
        }catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
