package jiyeon.app.payments.memberProfile.dto;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import jiyeon.app.payments.memberProfile.domain.QMemberProfile;

public enum SortType {

    NAME_ASC("name", Order.ASC),
    VIEW_DESC("viewCount", Order.DESC),
    REGISTERED_DESC("registeredAt", Order.DESC);

    private final String field;
    private final Order order;

    SortType(String field, Order order) {
        this.field = field;
        this.order = order;
    }

    public OrderSpecifier<?>[] getOrderSpecifiers(QMemberProfile q) {
        return switch (this) {
            case NAME_ASC -> new OrderSpecifier[]{
                    new OrderSpecifier<>(Order.ASC, q.name),
                    new OrderSpecifier<>(Order.ASC, q.id)
            };
            case VIEW_DESC -> new OrderSpecifier[]{
                    new OrderSpecifier<>(Order.DESC, q.viewCount),
                    new OrderSpecifier<>(Order.DESC, q.id)
            };
            case REGISTERED_DESC -> new OrderSpecifier[]{
                    new OrderSpecifier<>(Order.DESC, q.registeredAt),
                    new OrderSpecifier<>(Order.DESC, q.id)
            };
        };
    }
}
