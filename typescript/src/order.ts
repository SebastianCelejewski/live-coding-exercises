import Decimal from "decimal.js";

export interface Order {
  customer: string;
  amount: Decimal;
  currency: string;
}
