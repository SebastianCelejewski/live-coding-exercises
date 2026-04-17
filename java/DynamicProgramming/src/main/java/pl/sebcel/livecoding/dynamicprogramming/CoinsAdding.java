package pl.sebcel.livecoding.dynamicprogramming;

public class CoinsAdding {
	
	public int calculateMinNumberOfRequiredCoins(int[] coins, int sum) {
		if (sum == 0) {
			return 0;
		}
		
		if (sum < 0 || coins == null || coins.length == 0) {
			return Integer.MAX_VALUE;
		}
		
		int[] dp = new int[sum + 1];
		
		for (int i = 0; i <= sum; i++) {
			System.out.println("i: " + i);
			
			int min = Integer.MAX_VALUE;
			
			for (int c = 0; c < coins.length; c++) {
				int coinValue = coins[c];
				System.out.println("  coin: " + coinValue);
				int proposal = Integer.MAX_VALUE;
				
				if (i < coinValue) {
					// coin is too big
					System.out.println("    coin is too big -> proposal: MAX_VALUE");
					proposal = Integer.MAX_VALUE;
				}
				
				if (i == coinValue) {
					// coin matches i
					System.out.println("    coin matches perfectly -> proposal: 1");
					proposal = 1;
				}
				
				if (i > coinValue) {
					if (dp[i - coinValue] == Integer.MAX_VALUE) {
						// not possible then, not possible now
						System.out.println("    adding this coin won't work, because result for i-" + coinValue + " did not exist -> proposal: MAX_VALUE");
						proposal = Integer.MAX_VALUE;
					} else {
						// possible then - adding one coin
						System.out.println("    adding this coin will work -> proposal: " + (dp[i-coinValue] + 1));
						proposal = dp[i - coinValue] + 1;
					}
				}
				
				if (proposal < min) {
					min = proposal;
				}
			}
			
			System.out.println("  dp["+i+"] = min(...) = " + min);
			
			dp[i] = min;
		}
		
		return dp[sum];
	}

}


// check all coin types
//   for every coin type check if we have dp[i-coinValue]
//       take min for all coin types

// i = 0
//     c = 2
//          mamy dp[0-2] = dp[-2]? nie
// 



// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15

/*
i = 0, c = 0 (coinValue = 2)
do we have i-2 ? no -> no proposal
i = 0, c = 1 (coinValue = 3)
do we have i - 3 ? no -> no proposal
i = 0, c = 2 (coinValue = 5)
do we have i - 5 ? no -> no proposal
min(no proposal, no proposal, no proposal) = no proposal

dp[0] = no proposal
dp[1] = no proposal

i = 2, c = 0 (coinValue = 2)
do we have i-2? yes, but no proposal -> is i-2==0? yes -> we have a match -> 1
i = 2, c = 1 (coinValue = 3)
do we have i-3? no -> no proposal
i = 2, c = 2 (coinValue = 5)
do we have i-5? no -> no proposal
min(1, no proposal, no proposal) = 1
dp[2] = 1;

i = 3, c = 0 (coinValue = 2)
do we have i-2? yes, but no proposal -> is i-2==0? no -> no proposal
i = 3, c = 1 (coinValue = 3)
do we have i-3? yes, but no proposal -> is i-3==0? yes -> 1
i = 3, c = 2 (coinValue = 5)
do we have i-5? no -> no proposal

min(no proposal, 1, no proposal) = 1
dp[3] = 1

i = 4, c = 0 (coinValue = 2)
do we have i-2? yes, it is 1 -> 1 + 1 = 2
i = 4, c = 1 (coinValue = 3)
do we have i-3? yes, but it is no proposal -> is i-3==0? no -> no proposal
i = 4, c = 2 (coinValue = 5)
do we have i-5? no -> no proposal
min(2, no proposal, no proposal) = 2
dp[4] = 2

i = 5, c = 0 (coinValue = 2)
do we have i-2? yes, it is 1 -> 1 + 1 = 2
i = 5, c = 1 (coinValue = 3)
do we have i-3? yes, it is 1 -> 1 + 1 = 2
i = 5, c = 2 (coinValue = 5)
do we have i-5? yes, but it is no proposal -> is i-5==0? yes -> 1
min(2,2,1) = 1





*/