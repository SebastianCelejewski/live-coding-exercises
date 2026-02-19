from dataclasses import dataclass
from decimal import Decimal


@dataclass(frozen=True)
class Order:
    customer: str
    amount: Decimal
    currency: str