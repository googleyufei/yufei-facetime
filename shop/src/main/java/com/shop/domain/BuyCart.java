package com.shop.domain;

import java.util.ArrayList;
import java.util.List;

import com.shop.domain.book.OrderContactInfo;
import com.shop.domain.book.OrderDeliverInfo;
import com.shop.domain.book.PaymentWay;

/**
 * ���ﳵ
 */
public class BuyCart {
	/* ������ */
	private List<BuyItem> items = new ArrayList<BuyItem>();
	/* �ջ���������Ϣ */
	private OrderDeliverInfo deliverInfo;
	/* ��������ϵ��Ϣ */
	private OrderContactInfo contactInfo;
	/* �ջ����붩�����Ƿ���ͬ */
	private Boolean buyerIsrecipients;
	/* ֧����ʽ */
	private PaymentWay paymentWay;
	/* ���ͷ� */
	private float deliveFee = 10f;
	/* ���� */
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public float getDeliveFee() {
		return deliveFee;
	}

	public void setDeliveFee(float deliveFee) {
		this.deliveFee = deliveFee;
	}

	public PaymentWay getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(PaymentWay paymentWay) {
		this.paymentWay = paymentWay;
	}

	public Boolean getBuyerIsrecipients() {
		return buyerIsrecipients;
	}

	public void setBuyerIsrecipients(Boolean buyerIsrecipients) {
		this.buyerIsrecipients = buyerIsrecipients;
	}

	public OrderDeliverInfo getDeliverInfo() {
		return deliverInfo;
	}

	public void setDeliverInfo(OrderDeliverInfo deliverInfo) {
		this.deliverInfo = deliverInfo;
	}

	public OrderContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(OrderContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	/**
	 * ��ӹ�����
	 * 
	 * @param item
	 */
	public void addBuyItem(BuyItem item) {
		if (items.contains(item)) {// ��������Ѿ������ڹ��ﳵ,�ۼ��乺������
			for (BuyItem bItem : items) {
				if (bItem.equals(item)) {
					bItem.setAmount(bItem.getAmount() + 1);
					break;
				}
			}
		} else {
			items.add(item);
		}
	}

	/**
	 * ɾ������
	 * 
	 * @param item
	 */
	public void deleteBuyItem(BuyItem item) {
		if (this.items.contains(item))
			this.items.remove(item);
	}

	/**
	 * ��չ��ﳵ
	 */
	public void deleteAll() {
		items.clear();
	}

	public List<BuyItem> getItems() {
		return items;
	}

	/**
	 * ������Ʒ���ܽ��
	 */
	public float getTotalSellPrice() {
		float result = 0f;
		for (BuyItem item : items) {
			result += item.getProduct().getSellprice() * item.getAmount();
		}
		return result;
	}

	/**
	 * ������Ʒ���ܽ�ʡ���
	 */
	public float getTotalSavePrice() {
		float result = 0f;
		for (BuyItem item : items) {
			result += item.getProduct().getMarketprice() * item.getAmount();
		}
		return result - getTotalSellPrice();
	}

	/**
	 * ���㶩���ܽ��
	 * 
	 * @return
	 */
	public float getOrderTotalPrice() {
		return getTotalSellPrice() + getDeliveFee();
	}

}
