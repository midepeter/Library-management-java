// package com.mycompany.app.utils;

// import java.util.List;
// import org.hibernate.Criteria;


// public final class HibernateGenericResultBuilder<T> implements ResultBuilder<T> {

//     private final Criteria criteria;

//     public HibernateGenericResultBuilder(Criteria criteria) {
//         this.criteria = criteria;
//     }

//     @Override public ResultBuilder<T> withOffset(int offset) {
//         criteria.setFirstResult(offset);
//         return this;
//     }

//     @Override public ResultBuilder<T> withLimit(int limit) {
//         criteria.setMaxResults(limit);
//         return this;
//     }

//     @Override public ResultBuilder<T> orderedBy(String property) {
//         criteria.addOrder(Order.asc(property));
//         return this;
//     }

//     @Override public List<T> result() {
//         return new LinkedHashSet<T>(criteria.list());
//     }
// }