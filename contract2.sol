contract MonedaAlcala {
	//data of one user of the coin
	struct User {
		uint balance;
		bool active;
		bool admin;
		bytes32 email;
		bytes32 id;
	}
	
	//map with all the users
	//is public, can be accessed form web3, in future versions it should be secured
	mapping (address => User) participants;
	address[] addresses;
	
	//event sent to web3 to inform that a transaction has been made
	event AmountSent(address from, address to, uint amount);
	//event sent to web3 to inform that a new user had been added
	event UserAdded(address addr, bytes32 email, bytes32 id);
	//event sent to web3 to inform that an existing user has been disabled
	event UserDisabled(address addr, bytes32 email, bytes32 id);
	
	//applied to method to avoid it being executed by some unknow sender
	modifier onlyparticipants { if (!participants[msg.sender].active) throw; _ }
	//applied to method to avoid it being executed by someone without admin privileges
	modifier onlyadmins { if (!participants[msg.sender].admin) throw; _ }
	
	//builder of the contract, adds also the invoking address 
	//as admis, so it should be the main address of the node
	function MonedaAlcala(bytes32 email, bytes32 id){
	    if((email=="")||(id=="")) throw;
	    participants[msg.sender] = User(0,true,true,email,id);
	    addresses.push(msg.sender);
	}
	
	//adds a new user, it has to be called from an already declared admin address
	//the amount is an initial amount for the user, it can be 0
	function addUser(address addr, bytes32 email, bytes32 id, uint amount) onlyparticipants onlyadmins{
	    if((email=="")||(id=="")) throw;
		participants[addr] = User(amount,true,false,email,id);
		addresses.push(addr);
		UserAdded(addr,email,id);
	}
	
	//disables an user and sends its funds to another user, returns the amount of that funds
	function disableUser(address user, address balanceReceiver) onlyparticipants onlyadmins returns (uint){
		uint amount = participants[user].balance;
		send(balanceReceiver, amount);
		participants[user].active = false;
		int index = findAddressIndex(user);
		if (index != -1){
	        delete addresses[uint(index)];
		}
		UserDisabled(user,participants[user].email,participants[user].id);
		return amount;
	}
	
	//obtains the basic information about an user from its address
	function getUserByAddress(address addr) onlyparticipants returns (bytes32 email, bytes32 id, uint balance){
	    if(!participants[addr].active) return;
	    return (participants[addr].email, participants[addr].id, participants[addr].balance);
	}
	
	//obtains the basic information about an user from its position in the internal array
	//use this index in loops, but do not store it, because it can change if any user is
	//disabled or added
	function getUserByIndex(uint index) onlyparticipants returns (address addr, bytes32 email, bytes32 id, uint balance){
	    addr = addresses[index];
	    if(!participants[addr].active) return;
	    return (addr, participants[addr].email, participants[addr].id, participants[addr].balance);
	}
	
	//obtains the basic information about an user form its id
	function getUserById(bytes32 id) onlyparticipants returns (address addr, bytes32 email, uint balance) {
	    for (uint i = 0; i < addresses.length; i++){
	        addr = addresses[i];
	        if((participants[addr].active) && (participants[addr].id == id)){
	            return (addr,participants[addr].email, participants[addr].balance);
	        }
	    }
	    return;
	}
	
	//auxiliar internal function to retrieve an index in the addresses array form its address
	function findAddressIndex(address addr) internal returns(int){
	    for (uint i = 0; i < addresses.length; i++){
	        if (addresses[i] == addr) return int(i); 
	    }
	    return -1;
	}
	
	//returns the count of current active users
	function getNumberActiveUsers() onlyparticipants returns (uint){
	    return addresses.length;
	}
	
	//gives back the balance from an user
	function consultBalance(address user) onlyparticipants returns (uint){
		return (participants[user].balance);
	} 
	
	//sends some amount form one user to another
	//returns the balance result of the sender count
	function send(address receiver, uint amount) onlyparticipants returns (uint){
	    if (participants[msg.sender].balance < amount) return;
		if (!participants[receiver].active) return;
        participants[msg.sender].balance -= amount;
		participants[receiver].balance += amount;
        AmountSent(msg.sender, receiver, amount);
        return participants[msg.sender].balance;
    }
    
    //checks if an address has an active user
    function isActive(address user) onlyparticipants returns (bool){
        return participants[user].active;
    }
    
    //asigns funds to an user
    function fundUser(address receiver, uint amount) onlyparticipants onlyadmins returns (uint){
        if (!participants[receiver].active) throw;
        participants[receiver].balance += amount;
        AmountSent(msg.sender, receiver, amount);
        return participants[receiver].balance;
    }
    
}
