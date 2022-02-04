package bankofjava;

public class Account implements Comparable<Account>{
    protected long numAcc;
    private double balance;
    private int type; // 1 is savings, 2 is chequing
    private String desc;
    private Person belong;

    // constructor
    public Account(String desc, Person belong){
        this.desc = desc;
        this.belong = belong;

        String rand = "610";
        for(int i = 0; i < 5; i++){
            int randDigit = (int) Math.floor(Math.random() * 10);
            rand = rand.concat(Integer.toString(randDigit));
        }
        numAcc = Long.parseLong(rand);
        balance = 0;

        belong.getOwnedAccounts().add(this);
    }

    protected String getDesc(){
        return this.desc;
    }

    protected void setBelong(Person p){
        this.belong = p;
    }

    protected Person getBelong(){
        return this.belong;
    }

    // getter for balance
    protected double getBalance(){
        return this.balance;
    }

    // setter for balance
    protected boolean deposit(double amount){
        this.balance += amount;
        return true;
    }

    // getter for numAcc
    protected long getNumAcc(){
        return this.numAcc;
    }

    // setter for type
    protected void setType(int type){
        this.type = type;
    }

    // getter for type
    protected int getType(){
        return this.type;
    }

    

    // compareTo override to sort objects of Account
    @Override
    public int compareTo(Account o) {
        return this.belong.getName().compareTo(o.belong.getName());
    }

    @Override
    public String toString(){
        // TODO
        return this.belong.getName() + " - " + desc;
    }

}
