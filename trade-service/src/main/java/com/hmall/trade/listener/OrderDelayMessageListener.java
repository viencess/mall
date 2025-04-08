package com.hmall.trade.listener;

import com.hmall.api.client.PayClient;
import com.hmall.api.dto.PayOrderDTO;
import com.hmall.trade.constants.MQConstants;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.enums.OrderStatus;
import com.hmall.trade.enums.PayStatus;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDelayMessageListener {

    private final IOrderService orderService;
    private final PayClient payClient;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstants.DELAY_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(name = MQConstants.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = MQConstants.DELAY_ORDER_KEY
    ))
    public void listenOrderDelayMessage(Long orderId) {
        // 1、查询本地订单
        Order order = orderService.getById(orderId);
        // 2、判断订单状态，是否已支付
        if (order == null || !OrderStatus.UNPAID.equalsValue(order.getStatus())) {
            // 訂單不存在||已經支付
            return;
        }
        // 3、如果未支付，查询支付流水状态
        PayOrderDTO payOrderDTO = payClient.queryPayOrderByBizOrderNo(orderId);
        if (payOrderDTO != null || PayStatus.TRADE_SUCCESS.equalsValue(payOrderDTO.getStatus())){
            // 4、如果支付流水状态已支付，则修改订单状态为已支付
            orderService.markOrderPaySuccess(orderId);
        }
        // 5、如果支付流水状态未支付，则关闭订单
        orderService.cancelOrder(orderId);
    }
}
