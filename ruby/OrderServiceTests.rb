gem 'test-unit'
require 'test/unit'
require 'test/unit/ui/console/testrunner'
require 'bigdecimal'
require './Order.rb'
require './OrderService.rb'

class GetTotalAmountPerCustomerTest < Test::Unit::TestCase

	def setup
		@cut = OrderService.new()
	end

	def test_should_never_return_null
		result = @cut.get_total_amount_per_customer(nil)
		assert_not_nil(result)
	end

	def test_should_return_data_for_all_customers()
		orders = create_orders()
		result = @cut.get_total_amount_per_customer(orders)
		assert(result.key?("A"))
		assert(result.key?("B"))
	end

	def test_should_calculate_total_amount_per_customer_while_ignoring_currency()
		orders = create_orders()
		result = @cut.get_total_amount_per_customer(orders)
		assert_equal(BigDecimal("6.0"), result["A"])
		assert_equal(BigDecimal("15.0"), result["B"])
	end

	def test_should_handle_single_order
		result = @cut.get_total_amount_per_customer([Order.new("A", BigDecimal("7.0"), "PLN")])
		assert(result.key?("A"))
		assert_equal(BigDecimal("7.0"), result["A"])
	end

	def test_should_ignore_nil_order
		orders = create_orders()
		orders << nil
		result = @cut.get_total_amount_per_customer(orders)
		assert_equal(BigDecimal("6.0"), result["A"])
		assert_equal(BigDecimal("15.0"), result["B"])
	end

	def test_should_ignore_order_with_nil_customer
		orders = create_orders()
		orders << Order.new(nil, BigDecimal("7.0"), "PLN")
		result = @cut.get_total_amount_per_customer(orders)
		assert_equal(BigDecimal("6.0"), result["A"])
		assert_equal(BigDecimal("15.0"), result["B"])
	end

	def test_should_ignore_order_with_nil_amount
		orders = create_orders()
		orders << Order.new("A", nil, "PLN")
		result = @cut.get_total_amount_per_customer(orders)
		assert_equal(BigDecimal("6.0"), result["A"])
		assert_equal(BigDecimal("15.0"), result["B"])
	end

	def test_should_ignore_order_with_nil_currency
		orders = create_orders()
		orders << Order.new("A", BigDecimal("7.0"), nil)
		result = @cut.get_total_amount_per_customer(orders)
		assert_equal(BigDecimal("6.0"), result["A"])
		assert_equal(BigDecimal("15.0"), result["B"])
	end

	def create_orders()
		return [
			Order.new("A", BigDecimal("1.0"), "PLN"),
			Order.new("A", BigDecimal("2.0"), "EUR"),
			Order.new("A", BigDecimal("3.0"), "USD"),
			Order.new("B", BigDecimal("4.0"), "PLN"),
			Order.new("B", BigDecimal("5.0"), "EUR"),
			Order.new("B", BigDecimal("6.0"), "USD"),
		]
	end
end

# my_tests = Test::Unit::TestSuite.new("My Special Tests")
# my_tests << GetTotalAmountPerCustomerTest.new('test_should_never_return_null')
# my_tests << GetTotalAmountPerCustomerTest.new('test_should_return_data_for_all_customers')
# my_tests << GetTotalAmountPerCustomerTest.new('test_should_calculate_total_amount_per_customer_while_ignoring_currency')
# my_tests << GetTotalAmountPerCustomerTest.new('test_should_handle_single_order')
# Test::Unit::UI::Console::TestRunner.run(my_tests)