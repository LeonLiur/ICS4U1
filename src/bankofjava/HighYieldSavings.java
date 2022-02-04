package bankofjava;

public class HighYieldSavings extends SavingsAccount {
    // default constructor: initialized at 1% rate
    public HighYieldSavings(Person belong){
        super("High Yield Savings Acc.", belong);
        this.setAnnualRate(0.04);
        this.setTransferFee(0.04);
    }
}
