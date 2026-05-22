const plan_robbery = function(house_values) {
	let dp_i_2 = 0;
	let dp_i_1 = 0;

	for (let i = 0; i < house_values.length; i++) {
		console.log("House " + i + ": " + (dp_i_2 + house_values[i]) + " or " + dp_i_1);
		let dp_i = Math.max(dp_i_2 + house_values[i], dp_i_1);

		dp_i_2 = dp_i_1;
		dp_i_1 = dp_i;
		console.log(dp_i_2 +", " + dp_i_1 + ", " + dp_i);
	}

	return Math.max(dp_i_2, dp_i_1);
}

console.log(plan_robbery([4,2,9,7,1,3,2,4]));
console.log(plan_robbery([2, 7, 9, 3, 1]));
console.log(plan_robbery([1, 2, 3, 1]));
console.log(plan_robbery([5]));
