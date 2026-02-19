class OrderService
	def get_total_amount_per_customer(orders)
		return {} if orders.nil?

		return orders
			.select { |order| valid?(order)}
			.group_by{ |order| order.customer }
			.transform_values{ |orders_for_customer| orders_for_customer.reduce(BigDecimal("0.0")) { |total, order| total + order.amount}}
	end

	def valid?(order)
		return false if (order.nil?)
		return false if (order.customer.nil?)
		return false if (order.amount.nil?)
		return false if (order.currency.nil?)
		true
	end
end