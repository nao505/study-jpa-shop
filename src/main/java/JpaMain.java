import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.AddressEntity;
import jpabook.jpashop.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member();
            member.setUserName("member1");
            member.setHomeAddress(new Address("cirty1", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));
            em.persist(member);
            em.flush();
            em.clear();

            System.out.println("=========================");
            Member findMember = em.find(Member.class, member.getId());
//            Address a = findMember.getHomeAddress();
//            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipCode()));
//
//            List<Address> addressHistory = findMember.getAddressHistory();
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            favoriteFoods.remove("치킨");
//            favoriteFoods.add("한식");
/*

            System.out.println("=========================");
            findMember.getAddressHistory().remove(new AddressEntity("old1", "street", "10000"));
            findMember.getAddressHistory().add(new AddressEntity("newCity1", "street", "10000"));
*/

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void timeTest(Runnable runnable) {
        long start = System.nanoTime();

        runnable.run();

        long end = System.nanoTime();
        double c = (double) (end - start) / 1000000000;
        System.out.println(new DecimalFormat("#.#####").format(c) + " seconds");
    }
}