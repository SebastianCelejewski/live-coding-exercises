const fibonacci = function(n) {
	if (n == 0) {
		return 0;
	}

	if (n == 1) {
		return 1;
	}

	let prev1 = 0;
	let prev2 = 1;

	let i = 2;
	while (i < n) {
		current = prev1 + prev2;
		prev1 = prev2;
		prev2 = current;
		i++;
	}
	return prev1 + prev2;
}


console.log(fibonacci(5));

