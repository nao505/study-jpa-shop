package jpabook.jpashop.domain;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.DecimalFormat;
import java.util.UUID;

public class TestMain {

    static String uuid;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        for (int i = 0; i < 40000; i++) {
            TestEntity testEntity = new TestEntity();
            String string = UUID.randomUUID().toString();
            if (i == 5000) {
                uuid = string;
            }
            testEntity.setUserName(string);
            em.persist(testEntity);
        }
        em.flush();
        em.clear();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            long start = System.nanoTime();

            TestEntity testEntity = findKeyValue(TestEntity.class, "userName", uuid, em);

            long end = System.nanoTime();
            double result = (double) (end - start) / 1000000000;
            System.out.println(new DecimalFormat("#.#####").format(result) + " seconds - find key value");

            long start1 = System.nanoTime();

            TestEntity testEntity1 = em.find(TestEntity.class, 26789L);

            long end1 = System.nanoTime();
            double result1 = (double) (end1 - start1) / 1000000000;
            System.out.println(new DecimalFormat("#.#####").format(result1) + " seconds - find id");

            tx.commit();
            em.close();
        }).start();
    }

    private static <T> T findKeyValue(Class<T> clazz, String key, Object value, EntityManager em) {
        //SELECT * FROM job WHERE key = value;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        Predicate predicate = cb.equal(rootEntry.get(key), value);
        CriteriaQuery<T> singleQuery = cq.select(rootEntry).where(predicate);
        return em.createQuery(singleQuery).getSingleResult();
    }
}

@Entity
@Table(indexes = {@Index(name = "index_userName", columnList = "userName")})
class TestEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String userName;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}