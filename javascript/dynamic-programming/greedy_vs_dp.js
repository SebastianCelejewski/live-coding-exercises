function greedy(costs) {
    let i = costs.length;
    let totalCost = 0;

    while (i > 1) {
        if (costs[i - 1] >= costs[i - 2]) {
            i -= 2;
        } else {
            i -= 1;
        }

        totalCost += costs[i];
    }

    return totalCost;
}

function dp(costs) {
    const n = costs.length;

    if (n === 1) {
        return costs[0];
    }

    const minCost = new Array(n);

    minCost[0] = costs[0];
    minCost[1] = costs[1];

    for (let i = 2; i < n; i++) {
        minCost[i] =
            costs[i] + Math.min(minCost[i - 1], minCost[i - 2]);
    }

    return Math.min(minCost[n - 1], minCost[n - 2]);
}

function generateRandomInput() {
    const length = Math.floor(Math.random() * 15) + 2;

    const costs = [];

    for (let i = 0; i < length; i++) {
        costs.push(Math.floor(Math.random() * 20) + 1);
    }

    return costs;
}

function runTests(numberOfTests) {
    for (let test = 1; test <= numberOfTests; test++) {
        const input = generateRandomInput();

        const greedyResult = greedy(input);
        const dpResult = dp(input);

        if (greedyResult !== dpResult) {
            console.log("COUNTEREXAMPLE FOUND");
            console.log("Input:", input);
            console.log("Greedy:", greedyResult);
            console.log("DP:", dpResult);
            return;
        }
    }

    console.log("No counterexample found.");
}

runTests(100000);