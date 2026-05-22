const values = []

const fibonacci = function(n) {
	if (n == 0) {
		return 0;
	}

	if (n == 1) {
		return 1;
	}

	if (values[n-1] === undefined) {
		values[n-1] = fibonacci(n-1);
	}

	if (values[n-2] === undefined) {
		values[n-2] = fibonacci(n-2);
	}

	return values[n-1] + values[n-2];
}

console.log(fibonacci(6));