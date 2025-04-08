package com.hmall.trade.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    UNPAID(1, "未付款"),
    PAID_UNSHIPPED(2, "已付款,未发货"),
    SHIPPED_UNCONFIRMED(3, "已发货,未确认"),
    CONFIRMED_SUCCESS(4, "确认收货，交易成功"),
    CANCELED_CLOSED(5, "交易取消，订单关闭"),
    FINISHED_EVALUATED(6, "交易结束，已评价");

    private final int value;
    private final String desc;

    OrderStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    // 检查 value 是否匹配
    public boolean equalsValue(Integer value) {
        return value != null && this.value == value;
    }
}
