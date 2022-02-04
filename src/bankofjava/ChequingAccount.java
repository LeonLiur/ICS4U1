package bankofjava;

public class ChequingAccount extends Account{
    protected final int LIMIT = 10000;
    public ChequingAccount(String desc, Person belong){
        super(desc, belong);
        this.setType(2);
    }

    /** take away money from the account, returns integer value showing status code
     *  return code 1: transaction complete
     *  return code 2: transaction failed due to NOT ENOUGH BALANCE
     *  return code 3: transaction failed due to amount EXCEEDING LIMIT
     *  */
    protected int withdraw(double amount){
        if(this.getBalance() < amount)  return 2;

        return deposit(-1 * amount)?1:3;
    }

    /** transfer money from the savings account to another account in friendlist, returns an integer which shows status code
     *  return code 0: transaction failed due to AMOUNT IS INVALID
     *  return code 1: transaction complete
     *  return code 2: transaction failed due to NOT ENOUGH BALANCE
     *  return code 3: transaction failed due to TARGET ACCOUNT BEING NULL OR NOT FRIENDS WITH SOURCE ACCOUNT
     *  return code 4: transaction failed due to TARGET ACCOUNT BEING THE SAME ACCOUNT AS SOURCE ACCOUNT
     *  return code 5: transaction failed due to transaction amount EXCEEDING LIMIT
     *  */
    protected int transfer(double amount, Account account){
        if(amount <= 0) return 0;
        if(this.getBalance() < amount)  return 2;
        if(account == null || !this.getBelong().getFriendList().contains(account.getBelong())) return 3;
        if(account == this) return 4;
        if(amount > LIMIT)  return 5;

        this.deposit(-1 * amount);
        account.deposit(amount);
        return 1;
    }

    @Override
    protected boolean deposit(double amount){
        return super.deposit(amount);
    }
}
