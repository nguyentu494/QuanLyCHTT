/*
 * @ (#) JPa.java   1.0     10/04/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved
 */

package test;

import jakarta.persistence.*;

/*
 * @description:
 * @author: Tuss Nguyen
 * @date: 10/04/2024
 * @version: 1.0
 */
public class JPa {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            et.commit();
        } catch (Exception e) {
            et.rollback();
        }
        em.close();
    }
}
