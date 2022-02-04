package bankofjava;

import java.util.ArrayList;

public class Person implements Comparable<Person>{
    private ArrayList<Account> ownedAccounts = new ArrayList<>();
    private long password;
    private String name;
    private ArrayList<Person> friendList;
    protected int credit;

    public Person(String name, String pwd, int credit){
        this.name = name;
        this.credit = credit;
        long hash = 0;
        long X = 131;
        long MOD = 1000000007;
        this.friendList = new ArrayList<>();

        for(char s : pwd.toCharArray()){
            hash = hash * X % MOD + s;
            hash %= MOD;
        }

        this.password = hash;
        this.friendList.add(this);
    }

    public ArrayList<Account> getOwnedAccounts() {
        return this.ownedAccounts;
    }

    public void setOwnedAccounts(ArrayList<Account> x) {
        this.ownedAccounts = x;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    protected double totalWorth(){
        double ans = 0;
        for(Account ac : ownedAccounts) ans += ac.getBalance();
        return ans;
    }

    // method for log in, returns true if credentials are correct
    protected boolean logIn(String name, String pwd){
        long hash = 0;
        long X = 131;
        long MOD = 1000000007;

        for(char s : pwd.toCharArray()){
            hash = hash * X % MOD + s;
            hash %= MOD;
        }

        return this.name.equals(name) && this.password == hash;
    }

    // return true if addition is successful, false if failed
    protected boolean addFriend(Person toAdd){
        try{
            toAdd.friendList.add(this);
            this.friendList.add(toAdd);
        }catch(Exception e){
            System.out.println("[x] exception in adding friend: " + e);
            return false;
        }

        return true;
    }

    // getter for friendList()
    protected ArrayList<Person> getFriendList(){
        return this.friendList;
    }

    // method to compare wealth, returns String
    protected String compareWealth(Person comparee){
        if(!friendList.contains(comparee))  return "The comparee is not your friend";

        double balanceA = this.totalWorth();
        double balanceB = comparee.totalWorth();

        if(balanceA > balanceB) return String.format("You are $%f richer than your friend %s", balanceA - balanceB, comparee.getName());
        else if(balanceA < balanceB)    return String.format("You are $%f poorer than your friend %s", balanceB - balanceA, comparee.getName());
        else    return String.format("You and your friend %s are equally rich/poor", comparee.getName());
    }

    @Override
    public int compareTo(Person o) {
        return this.getName().compareTo(o.getName());
    }
}
