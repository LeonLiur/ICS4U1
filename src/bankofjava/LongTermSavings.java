package bankofjava;

public class LongTermSavings extends SavingsAccount{
    // default constructor: initialized at 1% rate
    public LongTermSavings(Person belong){
        super("Long Term Savings Acc.", belong);
        this.setAnnualRate(0.02);
        this.setTransferFee(0.01);
    }
}
