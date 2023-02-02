package com.tass.common.model.payment.response;

import com.tass.common.model.BaseResponse;
import com.tass.common.model.payment.dto.OrderPaymentData;
import lombok.Data;

@Data
public class OrderPaymentResponse extends BaseResponse {

    private OrderPaymentData data;
}
