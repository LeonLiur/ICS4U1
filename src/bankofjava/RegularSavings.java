package bankofjava;

public class RegularSavings extends SavingsAccount{
    // constructor, the rate is variable with the person's credit score
    // the rate is a percentage of the maximum rate, calculated using the following function
    // rate = 2% * (credit/850)^2, this way, people with less credit scores are penalized more
    // https://www.desmos.com/calculator/t5xeqhlxn3
    public RegularSavings(Person belong){
        super("Regular Savings Acc.", belong);
        this.setAnnualRate(0.02 * (belong.credit/(double)850) * (belong.credit/(double)850));
        this.setTransferFee(0);
    }
}
