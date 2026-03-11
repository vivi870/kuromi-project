package com.example.kuromi.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.kuromi.dto.OrderCancelDTO;
import com.example.kuromi.dto.OrderCreateDTO;
import com.example.kuromi.dto.OrderSendDTO;
import com.example.kuromi.entity.SysOrder;
import com.example.kuromi.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 创建订单（结算）
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody OrderCreateDTO dto) {
        Map<String, Object> result = new HashMap<>();
        try {
            String orderNo = orderService.createOrder(
                    dto.getUsername(),
                    dto.getConsignee(),
                    dto.getPhone(),
                    dto.getAddress()
            );
            result.put("code", 200);
            result.put("msg", "创建订单成功");
            result.put("data", orderNo);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    // 查询用户订单列表
    @GetMapping("/list/{username}")
    public Map<String, Object> getOrderList(@PathVariable String username) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<?> orderList = orderService.getOrderList(username);
            result.put("code", 200);
            result.put("data", orderList);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    // 查询订单详情
    @GetMapping("/detail/{orderNo}")
    public Map<String, Object> getOrderDetail(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> detail = orderService.getOrderDetail(orderNo);
            result.put("code", 200);
            result.put("data", detail);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    // 取消订单
    @PostMapping("/cancel")
    public Map<String, Object> cancelOrder(
           @RequestBody OrderCancelDTO dto ) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderService.cancelOrder(dto.getOrderNo(), dto.getUsername());
            result.put("code", 200);
            result.put("msg", "取消订单成功");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    // 商家更新订单为待发货
    @PostMapping("/send")
    public Map<String, Object> updateToSend(@RequestBody OrderSendDTO dto) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = orderService.updateToSend(dto.getOrderNo());
            result.put("code", 200);
            result.put("msg", "订单已改为待发货");
            result.put("data", success);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    // 用户确认收货：已发货(2) -> 已完成(3)
    @PostMapping("/confirm/{orderNo}")
    public Map<String, Object> confirmReceive(@PathVariable String orderNo, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginUser") == null) {
                result.put("code", 401); result.put("msg", "未登录"); return result;
            }
            SysOrder order = orderService.getOne(new LambdaQueryWrapper<SysOrder>().eq(SysOrder::getOrderNo, orderNo));
            if (order == null) { result.put("code", 404); result.put("msg", "订单不存在"); return result; }
            if (order.getPayStatus() != 2) { result.put("code", 400); result.put("msg", "只有已发货订单才能确认收货"); return result; }
            orderService.update(new LambdaUpdateWrapper<SysOrder>().eq(SysOrder::getOrderNo, orderNo).set(SysOrder::getPayStatus, 3));
            result.put("code", 200); result.put("msg", "确认收货成功，订单已完成");
        } catch (Exception e) {
            result.put("code", 500); result.put("msg", e.getMessage());
        }
        return result;
    }

    // 1. 创建支付宝支付订单（生成二维码链接）
    @PostMapping("/createAlipay")
    public Map<String, Object> createAlipay(@RequestParam String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 查询订单信息
            SysOrder order = orderService.getOne(new LambdaQueryWrapper<SysOrder>()
                    .eq(SysOrder::getOrderNo, orderNo));
            if (order == null) {
                result.put("code", 500);
                result.put("message", "订单不存在");
                return result;
            }
            // 2. 校验订单状态（必须是待支付0）
            if (order.getPayStatus() != 0) {
                result.put("code", 500);
                result.put("message", "订单已支付或已取消");
                return result;
            }

            // 3. 调用支付宝沙箱生成支付链接
            AlipayTradePagePayResponse response = Factory.Payment.Page()
                    .pay("订单支付-" + orderNo,  // 订单标题
                            orderNo,             // 商户订单号
                            order.getTotalAmount().toString(),  // 支付金额
                            "http://localhost:8080/api/order/alipayNotify"); // 回调地址

            // 4. 返回二维码链接（前端用这个生成动态二维码）
            result.put("code", 200);
            result.put("message", "生成支付订单成功");
            result.put("qrCodeUrl", response.getBody()); // 支付链接
            result.put("orderNo", orderNo);
            result.put("amount", order.getTotalAmount());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "生成支付订单失败：" + e.getMessage());
            return result;
        }
    }

    // 2. 支付宝回调接口（沙箱模拟支付后会调用）
    @PostMapping("/alipayNotify")
    public String alipayNotify(HttpServletRequest request) {
        try {
            // 1. 验证回调签名（SDK自动验证）
            // 2. 获取回调参数
            String outTradeNo = request.getParameter("out_trade_no"); // 订单号
            String tradeStatus = request.getParameter("trade_status"); // 支付状态

            // 3. 支付成功才更新订单状态
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                LambdaUpdateWrapper<SysOrder> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(SysOrder::getOrderNo, outTradeNo)
                        .set(SysOrder::getPayStatus, 1) // 0→1 待支付→待发货
                        .set(SysOrder::getUpdateTime, new java.util.Date());
                orderService.update(updateWrapper);
            }
            // 4. 告诉支付宝回调成功
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    // 3. 查询支付状态（前端轮询）
    @GetMapping("/queryAlipay/{orderNo}")
    public Map<String, Object> queryAlipay(@PathVariable String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            SysOrder order = orderService.getOne(new LambdaQueryWrapper<SysOrder>()
                    .eq(SysOrder::getOrderNo, orderNo));
            if (order == null) {
                result.put("code", 500);
                result.put("message", "订单不存在");
                return result;
            }
            // 返回支付状态：0=待支付，1=已支付（待发货）
            result.put("code", 200);
            result.put("payStatus", order.getPayStatus());
            result.put("message", order.getPayStatus() == 1 ? "支付成功" : "待支付");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "查询支付状态失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * 模拟支付成功，更新订单为待发货状态（仅测试用）
     */
    @PostMapping("/updatePayStatus")
    public Map<String, Object> updatePayStatus(@RequestParam String orderNo) {
        Map<String, Object> result = new HashMap<>();
        try {
            LambdaUpdateWrapper<SysOrder> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysOrder::getOrderNo, orderNo)
                    .set(SysOrder::getPayStatus, 1) // 1=待发货（对应前端显示）
                    .set(SysOrder::getUpdateTime, new Date());

            boolean success = orderService.update(updateWrapper);
            if (success) {
                result.put("code", 200);
                result.put("msg", "订单已更新为待发货状态");
            } else {
                result.put("code", 500);
                result.put("msg", "订单不存在或更新失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "更新失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
