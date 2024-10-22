package lng.bridge.learning.entity;

/**
 * 关于 virtual_deal 对象的操作
 * 1、当 deal 中有 3个 Open 状态的 Buy 记录，则新生成的 Buy 为 Virtual_Deal
 * 2、Virtual_Deal 的 Buy 不提交订单；根据实时行情数据，判断是否成功买入。
 * 3、Virtual_Deal 的 Buy 成功后，生成真是的 Sell (提交卖出订单)；Sell 真是卖出后，记录 进 Virtual_Deal 表。（获取流动资金）
 * 4、当 Virtual_Deal 中的 buy 状态改为 close 时，则查看其借入的订单，如果是real_real,则需要真是买入。
 */
public class VirtualDeal {

}
