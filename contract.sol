contract MonedaAlcala {
	//data of one user of the coin
	struct User {
		address addr;
		uint balance;
		bool active;
		string email;
		string id;
	}
	//map with all the users
	//is public, can be accessed form web3, in future versions it should be secured
	mapping (address => User) public participants;
	//event sent to web3 to inform that a transaction has been made
	event AmountSent(address from, address to, uint amount);
	//event sent to web3 to inform that a new user had been added
	event UserAdded(address addr, string email, string id);
	//event sent to web3 to inform that an existing user has been disabled
	event UserDisabled(address addr, string email, string id);
	
	//condition that some methods have to pass before doing anything
	modifier onlyparticipants { if (!participants[msg.sender].active) throw; _ }
	
	//builder of the contract, adds also the invoking address (should be the main address of the node) as an user
	function MonedaAlcala(string email, string id){
		participants[msg.sender] = User(msg.sender,0,true,email,id);
	}
	
	//adds a new user, it has to be called from an already declared address
	//the amount is an initial amount for the user, it can be 0
	function addUser(address addr, string email, string id, uint amount) onlyparticipants{
		participants[addr] = User(addr,amount,true,email,id);
		UserAdded(addr,email,id);
	}
	
	//disables an user and sends its funds to another user, returns the amount of that funds
	function disableUser(address user, address balanceReceiver) onlyparticipants returns (uint){
		uint amount = participants[user].balance;
		send(balanceReceiver, amount);
		participants[user].active = false;
		UserDisabled(user,participants[user].email,participants[user].id);
		return amount;
	}
	
	//gives back the balance from an user
	function consultBalance(address user) onlyparticipants returns (uint){
		return (participants[user].balance);
	} 
	
	//sends some amount form one user to another
	function send(address receiver, uint amount) onlyparticipants {
        	if (participants[msg.sender].balance < amount) return;
		if (!participants[receiver].active) return;
        	participants[msg.sender].balance -= amount;
		participants[receiver].balance += amount;
        	AmountSent(msg.sender, receiver, amount);
    }
    
}
