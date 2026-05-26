const find_longest_increasing_sequence_length = function(numbers) {
	if (!numbers || numbers.length < 1) {
		return 0;
	}

	let sequence_length = 0;
	let max_value_so_far = undefined;

	for (let i = 0; i < numbers.length ; i++) {
		if (!max_value_so_far || numbers[i] > max_value_so_far) {

		}
	}

	return Math.max(this_sequence_length, max_length);
}

const test_cases = [
	[],
	[0],
	[0,1],
	[1,0],
	[1,2,3],
	[1,2,0],
	[0,3,2],
	[1,2,3,1,2,3],
	[1,2,3,1,2,3,4],
	[1,2,3,4,1,2,3],
	[1,2,3,100,4,5,6],
	[0,5,1,2,3],
	[3,4,-1,0,6,2,3],
	[1,2,3,10,4,5,6,11],
]

test_cases.forEach(numbers => {
	console.log("Input [" + numbers + "]: " + find_longest_increasing_sequence_length(numbers));
})