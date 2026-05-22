const decode_ways = function(text) {
    if (text.length === 0) {
        return 0;
    }

	let dp_i_plus_1 = 1;
    let dp_i_plus_2 = 1;
    let dp_i = 0;

    for (let i = text.length - 1; i >= 0; i--) {
        dp_i = 0;
        
        let singleDigit = Number(text.substring(i, i + 1));
        let twoDigits = Number(text.substring(i, i + 2));
        
        if (singleDigit > 0) {
            dp_i += dp_i_plus_1;    
        }
    	
        if (twoDigits >= 10 && twoDigits <= 26) {
            dp_i += dp_i_plus_2;
        }

    	dp_i_plus_2 = dp_i_plus_1;
    	dp_i_plus_1 = dp_i;
    }

    return dp_i;
}


const inputs = ["", "0", "5", "05", "15", "121", "151", "1205", "101", "2514235421"]

inputs.forEach(s => {
    console.log("Input \"" + s + "\": " + decode_ways(s) + " ways");
})
