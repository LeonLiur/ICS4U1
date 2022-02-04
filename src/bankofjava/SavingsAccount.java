package bankofjava;

public class SavingsAccount extends Account{
    private double annualRate;
    private double transferFee;
    
    // constructor instanciates a SavingsAccount object and register its type and rate after calling the Account constructor which sets the name and pwd
    public SavingsAccount(String desc, Person belong){
        super(desc, belong);
        this.setType(1);
    }

    /** transfer money from the savings account to a chequing account of the same name, returns an integer which shows status code
     *  return code 1: transaction complete
     *  return code 2: transaction failed due to NOT ENOUGH BALANCE
     *  return code 3: transaction failed due to TARGET ACCOUNT IS NULL
     *  return code 4: transaction failed due to TARGET ACCOUNT IS NOT SAVINGS ACCOUNT WITH SAME NAME AS THIS ACCOUNT
     *  */
    protected int toChequing(double amount, Account account){
        if(amount <= 0) return 0;
        amount *= (1 + transferFee);

        if(this.getBalance() < amount) return 2;
        if(account == null)  return 3;
        if(account.getType() != 1 || !account.getBelong().equals(this.getBelong()))  return 4;
        this.deposit(-1 * amount);
        account.deposit(amount);
        return 1;
    }

    protected double[] calc_compount_interest(double time){
        double original = this.getBalance();
        double compound = original * Math.pow(1 + this.annualRate, time/12);
        double ret[] = {compound, compound - original}; // returning the total compound and net interest
        return ret;
    }

    protected double getAnnualRate() {
        return this.annualRate;
    }

    protected void setAnnualRate(double annualRate) {
        this.annualRate = annualRate;
    }

    protected double getTransferFee() {
        return this.transferFee;
    }

    protected void setTransferFee(double transferFee) {
        this.transferFee = transferFee;
    }

}
