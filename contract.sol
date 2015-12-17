contract MonedaAlcala {
	//data of one user of the coin
	struct User {
		uint balance;
		bool active;
		bool admin;
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
	
	//applied to method to avoid it being executed by some unknow sender
	modifier onlyparticipants { if (!participants[msg.sender].active) throw; _ }
	//applied to method to avoid it being executed by someone without admin privileges
	modifier onlyadmins { if (!participants[msg.sender].admin) throw; _ }
	
	//builder of the contract, adds also the invoking address 
	//as admis, so it should be the main address of the node
	function MonedaAlcala(string email, string id){
	    participants[msg.sender] = User(0,true,true,email,id);
	}
	
	//retrieves a participant from its address
	function getParticipant(address addr) onlyparticipants returns (string email, string id, uint balance){
	    if (participants[addr].active){
	        var participant = participants[addr];
	        balance = participant.balance;
	        email = participant.email;
	        id = participant.id;
	        return(email,id,balance);
	    }
	    return("","",0);
	}

	//adds a new user, it has to be called from an already declared admin address
	//the amount is an initial amount for the user, it can be 0
	function addUser(address addr, string email, string id, uint amount) onlyparticipants onlyadmins{
		participants[addr] = User(amount,true,false,email,id);
		UserAdded(addr,email,id);
	}
	
	//disables an user and sends its funds to another user, returns the amount of that funds
	function disableUser(address user, address balanceReceiver) onlyparticipants onlyadmins returns (uint){
	    if(!participants[user].active) return;
		uint amount = participants[user].balance;
		send(balanceReceiver, amount);
		participants[user].active = false;
		UserDisabled(user,participants[user].email,participants[user].id);
		return amount;
	}
	
	//gives back the balance from an user
	//0 could mean both that the user does not exist, or the balance is 0
	//use checkActive to verify if it is a active user
	function consultBalance(address user) onlyparticipants returns (uint){
	    if(!participants[user].active) return;
		return (participants[user].balance);
	} 
	
	//sends some amount form one user to another
	//returns the balance result of the sender count
	//or 0 if the operation is not viable
	function send(address receiver, uint amount) onlyparticipants returns (uint){
	    if (participants[msg.sender].balance < amount) return;
		if (!participants[receiver].active) return;
        participants[msg.sender].balance -= amount;
		participants[receiver].balance += amount;
        AmountSent(msg.sender, receiver, amount);
        return participants[msg.sender].balance;
    }
    
    //checks if an address has an active user
    function isActive(address user) returns (bool){
        return participants[user].active;
    }
    
    //asigns funds to an user, returns the new balance total of the user
    //or 0 if the user is not active 
    function fundUser(address receiver, uint amount) onlyparticipants onlyadmins returns (uint){
        if (!participants[receiver].active) return;
        participants[receiver].balance += amount;
        AmountSent(msg.sender, receiver, amount);
        return participants[receiver].balance;
    }
    
}
