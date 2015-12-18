contract MonedaAlcala {
	//data of one user of the coin
	struct User {
	    address addr;
		uint balance;
		bool active;
		bool admin;
		bytes32 email;
		bytes32 id;
	}
	
	//map with all the users
	//is public, can be accessed form web3, in future versions it should be secured
	User[] public participants;
	
	//event sent to web3 to inform that a transaction has been made
	event AmountSent(address from, address to, uint amount);
	//event sent to web3 to inform that a new user had been added
	event UserAdded(address addr, bytes32 email, bytes32 id);
	//event sent to web3 to inform that an existing user has been disabled
	event UserDisabled(address addr, bytes32 email, bytes32 id);
	
	//applied to method to avoid it being executed by some unknow sender
	modifier onlyparticipants { if (!findUser(msg.sender).active) throw; _ }
	//applied to method to avoid it being executed by someone without admin privileges
	modifier onlyadmins { if (!findUser(msg.sender).admin) throw; _ }
	
	//builder of the contract, adds also the invoking address 
	//as admis, so it should be the main address of the node
	function MonedaAlcala(bytes32 email, bytes32 id){
	    if((email=="")||(id=="")) throw;
	    participants.push(User(msg.sender,0,true,true,email,id));
	}
	
	function findUserIndex(address addr) internal returns (uint){
	    for(uint i = 0; i < participants.length; i++){
	        if (participants[i].addr == addr) return i;
	    }
	    return;
	}
	
	function findUser(address addr) internal returns (User){
	    for(uint i = 0; i < participants.length; i++){
	        if (participants[i].addr == addr) return participants[i];
	    }
	    return;
	}
	
	//retrieves a participant from its address
	function getUser(address addr) onlyparticipants returns (bytes32 email, bytes32 id, uint balance){
	    uint i = findUserIndex(addr);
	    if (participants[i].active){
	        return(participants[i].email,participants[i].id,participants[i].balance);
	    }
	    return;
	}
	
	
	
	//adds a new user, it has to be called from an already declared admin address
	//the amount is an initial amount for the user, it can be 0
	function addUser(address addr, bytes32 email, bytes32 id, uint amount) onlyparticipants onlyadmins{
		participants.push(User(addr,amount,true,false,email,id));
		UserAdded(addr,email,id);
	}
	
	//disables an user and sends its funds to another user, returns the amount of that funds
	function disableUser(address user, address balanceReceiver) onlyparticipants onlyadmins returns (uint){
	    uint i = findUserIndex(user);
	    if (!participants[i].active) return;
	    uint amount = participants[i].balance;
		send(balanceReceiver, amount);
		delete participants[i];
		UserDisabled(user,participants[i].email,participants[i].id);
		return amount;
	}
	
	//gives back the balance from an user
	//0 could mean both that the user does not exist, or the balance is 0
	//use checkActive to verify if it is a active user
	function consultBalance(address user) onlyparticipants returns (uint){
	    var participant = findUser(user);
	    if(!participant.active) return;
		return (participant.balance);
	} 
	
	//sends some amount form one user to another
	//returns the balance result of the sender count
	//or 0 if the operation is not viable
	function send(address receiver, uint amount) onlyparticipants returns (uint){
	    uint i_receiver = findUserIndex(receiver);
	    if (!participants[i_receiver].active) return;
	    uint i_sender = findUserIndex(msg.sender);
	    if (participants[i_sender].balance < amount) return;
		participants[i_sender].balance -= amount;
		participants[i_receiver].balance += amount;
        AmountSent(msg.sender, receiver, amount);
        return participants[i_sender].balance;
    }
    
    //checks if an address has an active user
    function isActive(address user) returns (bool){
        return findUser(user).active;
    }
    
    //asigns funds to an user, returns the new balance total of the user
    //or 0 if the user is not active 
    function fundUser(address receiver, uint amount) onlyparticipants onlyadmins returns (uint){
        uint i_receiver = findUserIndex(receiver);
	    if (!participants[i_receiver].active) return;
        participants[i_receiver].balance += amount;
        AmountSent(msg.sender, receiver, amount);
        return participants[i_receiver].balance;
    }
    
}
