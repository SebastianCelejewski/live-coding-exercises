const calculate_min_cost = function(steps_cost) {
	let min_cost_k_2 = 0;
	let min_cost_k_1 = 0;
	for (let k = 0; k < steps_cost.length; k++) {
		let min_cost_k = steps_cost[k] + Math.min(min_cost_k_2, min_cost_k_1);
		min_cost_k_2 = min_cost_k_1;
		min_cost_k_1 = min_cost_k;
	}
	return Math.min(min_cost_k_2, min_cost_k_1);
}

console.log(calculate_min_cost([10, 15, 20]));

console.log(calculate_min_cost([1,100,1,1,1,100,1,1,100,1]));
