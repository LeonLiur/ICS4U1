package bankofjava;
 
import java.util.ArrayList;
 
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.*;
 
public class AppJ extends Application{
    private static final DecimalFormat def = new DecimalFormat("0.00");
    
    private StackPane homePane = new StackPane();
    private StackPane loginPane = new StackPane();
    private StackPane transferPane = new StackPane();
    private StackPane chooseAccountPane = new StackPane();
    private StackPane newAccountPane = new StackPane();
    private Pane friendsPane = new Pane();
 
    Account letapAccount;
    Account kodyAccount;
    Account testerAccount;
    ArrayList<Account> accounts = new ArrayList<Account>();
    ArrayList<Person> people = new ArrayList<Person>();
    Account activeAccount;
 
    Scene loginScene = new Scene(loginPane, 400, 400);
    Button loginButton = new Button("LOGIN");
    VBox loginScreen = new VBox();
    TextField username = new TextField();
    PasswordField password = new PasswordField();
    Label warning = new Label(
            "This bank uses rolling hash for its passwords because its IT team is too dumb for SHA256! Hack us!");
    Button neww = new Button("New to Bank of Java?");

    Scene newAccountScene = new Scene(newAccountPane, 400, 400);
    TextField tfName = new TextField("full name");
    TextField tfPwd = new TextField("password");
    TextField tfPwdRepeat = new TextField("repeat password");
    TextField tfCredit = new TextField("credit score");
    TextField tfTypeAcc = new TextField("type of account");
    Label txt = new Label("BoJ offers 5 types of different accounts:\n#1: Regular Chequing Acc. - fixed chequing with a transaction limit\n#2. Student Chequing Acc. (beta) - fixed chequing for students" +
    "\n#3. Regular Savings Acc. - flexible rate with credit score\n#4. Long term Savings Acc. - high rate savings with high transaction fee\n#5. High Yield Savings Acc. - very high rate savings with very high transaction fees");
    Button createAcc = new Button("Create!");
    Label ss = new Label("Please fill out all the information!");


    Scene homeScene = new Scene(homePane, 400, 400);
    VBox homeScreen = new VBox();
    Label accBalLabel;
    Label accNumLabel;
    Button transferButton = new Button("TRANSFER MONEY");
    Button friendsButton = new Button("VIEW FRIENDS");
    TextField moveMoney = new TextField("amount");
    Button withdrawButton = new Button("WITHDRAW");
    Button depositButton = new Button("DEPOSIT");
    Label homeAlert = new Label("");
 
    Scene transferScene = new Scene(transferPane, 400, 400);
    Button sendMoneyButton = new Button("SEND");
    VBox transferScreen = new VBox();
    ObservableList<Account> options = FXCollections.observableArrayList();
    ComboBox<Account> payees = new ComboBox<>(options);
    TextField payee = new TextField();
    TextField amount = new TextField();
    Label alert = new Label("");
    Label message = new Label(
            "* Payee has to be in your friends list\n* Select their name from the dropdown menu\n* The list is sorted lexicographically using NOT bubble sort\n* If y"
                    +
                    "ou have a savings account, then you can ONLY transfer to a chequing acount with the same name");
 
    Scene friendsScene;
    HBox friendsScreen;
    Label freindsAlert = new Label("");
    ListView<String> friendsList = new ListView<>();
    TextField tfAddFriend = new TextField("full name");
    Button addFriendButton = new Button("ADD FRIEND");
 
    Button returnButtonFriends = new Button("RETURN");
    Button returnButtonTransfer = new Button("RETURN");
    Button logOutButton = new Button("DIP");

    Scene chooseAccountScene = new Scene(chooseAccountPane, 400, 400);
    Label accs = new Label("You have several accounts with bankofjava, please choose one to log in:");
    TextField tfChoose = new TextField();
    Button submitChoice = new Button("SUBMIT CHOICE");
    Label msg = new Label("");
    ArrayList<Account> choices = new ArrayList<>();
 
    @Override
    public void init() {
        // CONFIGURING LOGIN SCREEN
        configureLogInScreen();
 
        // CONFIGURING HOME SCREEN
        configureHomeScreen();
 
        // CONFIGURE TRANSFER SCREEN
        configureTransferScreen();
 
        // CONFIGURE FRIEND SCREEN
        configureFriendScreen();

        // CONFIGURE CHOOSE ACCOUNT SCREEN
        configureAccountScreen();

        tfName.setTranslateX(-140);
        tfName.setTranslateY(-180);
        tfName.setMaxWidth(100);

        tfPwd.setTranslateX(-115);
        tfPwd.setTranslateY(-150);
        tfPwd.setMaxWidth(150);

        tfPwdRepeat.setTranslateX(-115);
        tfPwdRepeat.setTranslateY(-120);
        tfPwdRepeat.setMaxWidth(150);

        tfCredit.setTranslateX(-140);
        tfCredit.setTranslateY(-90);
        tfCredit.setMaxWidth(100);

        tfTypeAcc.setTranslateX(-135);
        tfTypeAcc.setTranslateY(-60);
        tfTypeAcc.setMaxWidth(110);

        txt.setWrapText(true);
        txt.setTranslateY(20);
        txt.setTranslateX(10);

        createAcc.setTranslateY(110);
        createAcc.setTranslateX(-155);

        ss.setTranslateX(100);
        ss.setTranslateY(110);
        newAccountPane.getChildren().addAll(tfName, tfPwd, tfPwdRepeat, tfCredit, tfTypeAcc, txt, createAcc, ss);
    }

    private void configureAccountScreen() {
        chooseAccountPane.getChildren().addAll(accs, tfChoose, submitChoice, msg);
        accs.setTranslateY(-120);
        accs.setStyle("fx-font-size: 10px;");

        tfChoose.setMaxWidth(30);
        tfChoose.setTranslateY(140);
        submitChoice.setTranslateY(180);

        tfChoose.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitChoice.fire();
            }
        });
    }
 
    private void configureFriendScreen() { 
        returnButtonFriends.relocate(335, 0);
        // Using HBox
        // friendsScreen = new HBox(friendsList);
        // friendsScene = new Scene(friendsScreen, 400, 400);
 
        // Using Pane
        friendsPane.getChildren().addAll(friendsList, returnButtonFriends, tfAddFriend, addFriendButton, freindsAlert);
        friendsScene = new Scene(friendsPane, 400, 400);
        friendsList.setPrefSize(300, 300);

        tfAddFriend.setTranslateY(330);
        addFriendButton.setTranslateX(200);
        addFriendButton.setTranslateY(330);
        freindsAlert.setTranslateY(370);
    }
 
    private void configureTransferScreen() {
        transferScreen.setSpacing(8);
        transferScreen.setPadding(new Insets(10, 10, 10, 10));
        transferScreen.getChildren().addAll(
                new Label("To: "),
                payees,
                new Label("Amount: "),
                amount,
                sendMoneyButton,
                returnButtonTransfer,
                message,
                alert);
        message.setWrapText(true);
        transferPane.getChildren().addAll(transferScreen);
        alert.setTranslateY(50);
    }
 
    private void configureHomeScreen() {
        homeScreen.setSpacing(8);
        homeScreen.setPadding(new Insets(10, 10, 10, 10));
 
        accBalLabel = new Label("Account Balance: loading...");
        accNumLabel = new Label("Account Number: loading...");
 
        accBalLabel.setStyle("-fx-text-fill: black; -fx-font-size: 20px;");
        accNumLabel.setStyle("-fx-text-fill: black; -fx-font-size: 20px;");
 
        logOutButton.setTranslateX(0);
        logOutButton.setTranslateY(200);

        moveMoney.setTranslateX(200);
        moveMoney.setTranslateY(0);
        moveMoney.setMaxWidth(100);
        withdrawButton.setTranslateX(100);
        withdrawButton.setTranslateY(100);
        depositButton.setTranslateX(200);
        depositButton.setTranslateY(0);
 
        homeScreen.getChildren().addAll(
                accBalLabel,
                accNumLabel,
                transferButton,
                friendsButton,
                logOutButton,
                moveMoney,
                withdrawButton,
                depositButton);
        homePane.getChildren().addAll(homeScreen);
    }
 
    private void configureLogInScreen() {
        warning.setLayoutX(300);
        warning.setLayoutY(300);
        warning.setWrapText(true);
        neww.setTranslateY(150);
        loginScreen.setSpacing(8);
        loginScreen.setPadding(new Insets(10, 10, 10, 10));
        loginScreen.getChildren().addAll(
                new Label("Username"),
                username,
                new Label("Password"),
                password,
                loginButton, warning, neww);
        loginPane.getChildren().addAll(loginScreen);
        password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });
    }
 
    @Override
    public void start(Stage primaryStage) {
        Person letap = new Person("Letap Patel", "iL0vepython", 750);
        Person cody = new Person("Cody Naildner", "a0tWeeb", 600);
        Person tester = new Person("Leo Liu", "aaaaa", 500);
        people.add(letap);
        people.add(cody);
        people.add(tester);

        letapAccount = new HighYieldSavings(letap);
        letapAccount.deposit(1000000);
 
        kodyAccount = new StudentChequing(cody);
        kodyAccount.deposit(90000);
 
        testerAccount = new RegularChequing(tester);
        Account testerAccount2 = new HighYieldSavings(tester);
        Account testerAccount3 = new LongTermSavings(tester);
        testerAccount.deposit(100000);

        tester.addFriend(cody);
        tester.addFriend(letap);

        accounts.add(letapAccount);
        accounts.add(kodyAccount);
        accounts.add(testerAccount);
        accounts.add(testerAccount2);
        accounts.add(testerAccount3);
 
        primaryStage.setScene(loginScene);
        primaryStage.show();
        primaryStage.setTitle("Login - Bank of Java");

        addFriendButton.setOnAction(actionEvent->{
            boolean found = false;
            String name = tfAddFriend.getText();
            for(Person person : people){
                if(person.getName().equals(name)){
                    if(person.getFriendList().contains(activeAccount.getBelong())){
                        System.out.println("[-] already friends with addee");
                        freindsAlert.setText("You are already friends with them!");
                    }else{
                        person.addFriend(activeAccount.getBelong());
                        freindsAlert.setText("You are now their friend!");
                        freindsAlert.setStyle("-fx-text-fill: green;");
                        System.out.println("[+] added new friend " + person.getName());
                        friendsButton.fire();
                    }
                    found = true;
                    break;
                }
            }
            if(!found){
                freindsAlert.setText("Name not found");
                freindsAlert.setStyle("-fx-text-fill: red;");
            }
        });

        createAcc.setOnAction(actionEvent ->{
            String name = tfName.getText();
            String pwd1 = tfPwd.getText();
            String pwd2 = tfPwd.getText();
            String credit = tfCredit.getText();
            String type = tfTypeAcc.getText();

            ss.setWrapText(true);

            try{
                boolean flag = false;
                int tp = Integer.parseInt(type);
                if(!pwd1.equals(pwd2)){
                    ss.setText("Passwords don't match!");
                }else if(tp <= 0 || tp > 5){
                    ss.setText("Invalid type!");
                }else{
                    for(Person p : people){
                        if(p.getName() == name){
                            ss.setText("You already have an account at BoJ, we are adding another account under your name");
                            Account ac;
                            switch(tp){
                                case 1:
                                    ac = new RegularChequing(p);
                                    accounts.add(ac);
                                    break;
                                case 2:
                                    ac = new StudentChequing(p);
                                    accounts.add(ac);
                                    break;
                                case 3:
                                    ac = new RegularSavings(p);
                                    accounts.add(ac);
                                    break;
                                case 4:
                                    ac = new LongTermSavings(p);
                                    accounts.add(ac);
                                    break;
                                case 5:
                                    ac = new HighYieldSavings(p);
                                    accounts.add(ac);
                                    break;
                            }
                            flag = true;
                            tfName.setText("full name");
                            tfPwd.setText("password");
                            tfPwdRepeat.setText("repeat passwrod");
                            tfTypeAcc.setText("account type");
                            tfCredit.setText("credit score");
                            break;
                        }
                    }
                    if(!flag){
                        ss.setText("You don't have an account at BoJ, we are adding it for you!");
                        Person newp = new Person(name, pwd1, Integer.parseInt(credit));
                        people.add(newp);
                        Account ac;
                            switch(tp){
                                case 1:
                                    ac = new RegularChequing(newp);
                                    accounts.add(ac);
                                    break;
                                case 2:
                                    ac = new StudentChequing(newp);
                                    accounts.add(ac);
                                    break;
                                case 3:
                                    ac = new RegularSavings(newp);
                                    accounts.add(ac);
                                    break;
                                case 4:
                                    ac = new LongTermSavings(newp);
                                    accounts.add(ac);
                                    break;
                                case 5:
                                    ac = new HighYieldSavings(newp);
                                    accounts.add(ac);
                                    break;
                            }
                            flag = true;
                    }

                    if(flag){
                        primaryStage.setScene(loginScene);
                        primaryStage.setTitle("Bank Of Java");
                        primaryStage.show();
                        ss.setText("");
                    }
                }
            }catch(Exception e){
                ss.setText("Invalid type or credit value!");
            }
        });
 
        submitChoice.setOnAction(actionEvent ->{
            boolean ok = false;;
            int cho = -1;
            String choi = tfChoose.getText();
            try{
                cho = Integer.parseInt(choi);
                ok = cho > 0 && cho <= choices.size();
            }catch(Exception e){
                
            }
            if(ok){
                msg.setText("Valid choice");
                activeAccount = choices.get(cho - 1);
                System.out.println("[+] new active account: " + activeAccount.getBelong().getName() + "----");

                accBalLabel.setText(("Account Balance: $" + activeAccount.getBalance()));
                accNumLabel.setText("Account Number: " + activeAccount.getNumAcc());

                options.clear();
                for(Person p : activeAccount.getBelong().getFriendList()){
                    options.addAll(p.getOwnedAccounts());
                }

                Collections.sort(options);

                primaryStage.setScene(homeScene);
                primaryStage.setTitle("Home - Bank of Java");
                primaryStage.show();

                warning.setText("This bank uses rolling hash for its passwords because its IT team is too dumb for SHA256! Hack us!");
                warning.setStyle("-fx-text-fill: black; -fx-font-size: 13px;");

                username.setText("");
                password.setText("");
                msg = new Label("");
            }else{
                msg.setText("Invalid choice, choose again!");
                tfChoose.setText("");
                msg.setStyle("-fx-text-fill: red;");
            }
        });
        
        loginButton.setOnAction(actionEvent -> {
            String usr = username.getText();
            String pwd = password.getText();
            boolean clear = false;

            for(Person person : people){
                if(person.logIn(usr, pwd)){
                    clear = true;
                    if(person.getOwnedAccounts().size() == 1){
                        activeAccount = person.getOwnedAccounts().get(0);
                        System.out.println("[+] new active account: " + activeAccount.getBelong().getName() + "----");

                        accBalLabel.setText(("Account Balance: $" + activeAccount.getBalance()));
                        accNumLabel.setText("Account Number: " + activeAccount.getNumAcc());

                        options.clear();
                        for(Person p : activeAccount.getBelong().getFriendList()){
                            options.addAll(p.getOwnedAccounts());
                        }

                        Collections.sort(options);

                        primaryStage.setScene(homeScene);
                        primaryStage.setTitle("Home - Bank of Java");
                        primaryStage.show();

                        warning.setText("This bank uses rolling hash for its passwords because its IT team is too dumb for SHA256! Hack us!");
                        warning.setStyle("-fx-text-fill: black; -fx-font-size: 13px;");

                        username.setText("");
                        password.setText("");
                    }else{
                        primaryStage.setScene(chooseAccountScene);
                        int counter = 1;
                        for(Account ac : person.getOwnedAccounts()){
                            accs.setText(accs.getText() + "\n\n" + ac.numAcc + " - " + ac.getDesc() + " - #" + counter);
                            choices.add(ac);
                            counter ++;
                        }
                        accs.setText(accs.getText() + "\nplease enter the number representing the account you want to enter");
                    }
                    
                }
            }
            if(!clear){
                System.out.println("[-] failed log-in attempt");
                warning.setText("The password or username you entered is incorrect");
                warning.setStyle("-fx-text-fill: red; -fx-font-size: 11px;");
            }
        });
 
        logOutButton.setOnAction(actionEvent -> {
            primaryStage.setScene(loginScene);
            primaryStage.show();
            primaryStage.setTitle("Login - Bank of Java");
            System.out.println("[+] logged out of " + activeAccount.getBelong().getName());
            activeAccount = null;
        });
 
        transferButton.setOnAction(actionEvent -> {
            primaryStage.setScene(transferScene);
            primaryStage.setTitle("Transfer Money - Bank of Java");
            primaryStage.show();
 
        });
 
        friendsButton.setOnAction(actionEvent -> {
            primaryStage.setScene(friendsScene);
            primaryStage.setTitle("Friends List - Bank of Java");
            primaryStage.show();


            friendsList.getItems().clear();
            List<Person> temp = new ArrayList<>();

            temp = activeAccount.getBelong().getFriendList();
            temp.sort((Person a1, Person a2) -> a1.totalWorth() - a2.totalWorth() > 0?-1:1);

            friendsList.setStyle("-fx-font-family: Arial; -fx-font-size: 10pt");

            for(int i = 0; i < temp.size(); i++){
                Person ac = temp.get(i);
                String nme = ac.getName();
                double balance = ac.totalWorth();
                String res = def.format(balance);
                for(int j = def.format(balance).length(); j < 20; j++)  res += " ";
                res += nme;
                friendsList.getItems().add(res);
                // System.out.println(res);
            }
        });
 
        sendMoneyButton.setOnAction(actionEvent -> {
            Account target = payees.getValue();
            double amt = 0;
            try{
                amt = Double.parseDouble(amount.getText());
            }catch(NumberFormatException e){
                amt = -1;
                System.out.println("[-] caught number format exception invalid amount into long");
            }
            if(activeAccount instanceof ChequingAccount){
                int transaction = ((ChequingAccount) activeAccount).transfer(amt, target);
                switch(transaction){
                    case 0:
                        alert.setText("Txn failed, cause: INVALID_AMOUNT");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: invalid amount");
                        break;
                    case 1:
                        alert.setText("Txn complete!\nsuccesful transaction $" + amt + " to " + target.getBelong().getName() + "\nNew balance: " + activeAccount.getBalance());
                        alert.setStyle("-fx-text-fill: green;");
                        System.out.println("[+] succesful transaction $" + amt + " to " + target.getBelong().getName());
                        break;
                    case 2:
                        alert.setText("Txn failed, cause: NOT_ENOGH_BALANCE");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: insufficient fund");
                        break;
                    case 3:
                        alert.setText("Txn failed, cause: TARGET_NULL");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: null target");
                        break;
                    case 4:
                        alert.setText("Txn failed, cause: TARGET_==_SOURCE");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: invalid target");
                        break;
                    case 5:
                        alert.setText("Txn failed, cause: AMOUNT_EXCEED_LIMIT");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: exceeding limit");
                        break;
                    default:
                        break;
                }
                alert = new Label("");
            }else if(activeAccount instanceof SavingsAccount){
                int transaction = ((SavingsAccount) activeAccount).toChequing(amt, target);
                switch(transaction){
                    case 0:
                        alert.setText("Txn failed, cause: INVALID_AMOUNT");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: invalid amount");
                        break;
                    case 1:
                        SavingsAccount downcast = (SavingsAccount)  activeAccount;
                        alert.setText("Txn complete!\nsuccesful transaction $" + amt + " to " + target.getBelong().getName() + "\nNew balance: " + activeAccount.getBalance()
                        + "\nTransaction fee: " + (downcast.getTransferFee() * amt));
                        alert.setStyle("-fx-text-fill: green;");
                        System.out.println("[+] succesful transaction $" + amt + " to " + target.getBelong().getName());
                        break;
                    case 2:
                        alert.setText("Txn failed, cause: NOT_ENOGH_BALANCE");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: insufficient fund");
                        break;
                    case 3:
                        alert.setText("Txn failed, cause: TARGET_NULL");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: null target");
                        break;
                    case 4:
                        alert.setText("Txn failed, cause: TARGET_ISNT_SAME_NAME_ACC");
                        alert.setStyle("-fx-text-fill: red;");
                        System.out.println("[-] failed transaction: invalid target");
                        break;
                    default:
                        break;
                }
                alert = new Label("");
            }else{
                message.setText("Your account has some serious issues, please contact tech support");
            }
 
        });
 
        returnButtonFriends.setOnAction(actionEvent -> {
            primaryStage.setScene(homeScene);
            primaryStage.setTitle("Home - Bank of Java");
            primaryStage.show();
 
            freindsAlert = new Label("");
        });
 
        returnButtonTransfer.setOnAction(actionEvent -> {
            primaryStage.setScene(homeScene);
            primaryStage.setTitle("Home - Bank of Java");
            primaryStage.show();
            
            alert.setText("");
            alert.setStyle("-fx-text-fill: black");
        });
 
        depositButton.setOnAction(actionEvent ->{
            String v = moveMoney.getText();
            try{
                Double amt = Double.parseDouble(v);
                if(activeAccount instanceof ChequingAccount){
                    if(amt > ((ChequingAccount) activeAccount).LIMIT){
                        throw new Exception("[-] fake exception");
                    }
                }
                activeAccount.deposit(amt);
                accBalLabel.setText(("Account Balance: $" + activeAccount.getBalance()));
                accNumLabel.setText("Account Number: " + activeAccount.getNumAcc());
            }catch(Exception e){
                homeAlert.setText("Invalid value or exceeding limit");
                moveMoney.setText("");
            }
        });

        withdrawButton.setOnAction(actionEvent ->{
            String v = moveMoney.getText();
            try{
                Double amt = Double.parseDouble(v);
                if(activeAccount.getBalance() < amt)    throw new Exception("Fake exception");
                activeAccount.deposit(amt * -1);
                accBalLabel.setText(("Account Balance: $" + activeAccount.getBalance()));
                accNumLabel.setText("Account Number: " + activeAccount.getNumAcc());
            }catch(Exception e){
                homeAlert.setText("Invalid value");
                moveMoney.setText("");
            }
        });

        neww.setOnAction(actionEvent ->{
            primaryStage.setScene(newAccountScene);
            primaryStage.setTitle("Make a New Account");
            primaryStage.show();
        });
    }
 
    public static void main(String[] args) {
        try{
            launch(args);
        }catch(Exception e){
            System.out.println("[HELP] uncaught exception caught: " + e);
        }
    }
}
