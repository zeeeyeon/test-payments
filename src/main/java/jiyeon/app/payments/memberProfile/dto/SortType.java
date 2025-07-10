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

    public OrderSpecifier<?> toOrderSpecifier(QMemberProfile q) {
        return switch (this) {
            case NAME_ASC -> new OrderSpecifier<>(order, q.name);
            case VIEW_DESC -> new OrderSpecifier<>(order, q.viewCount);
            case REGISTERED_DESC -> new OrderSpecifier<>(order, q.registeredAt);
        };
    }
}
