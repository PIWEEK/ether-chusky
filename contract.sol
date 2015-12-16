contract MonedaAlcala {
	struct User {
		address addr;
		uint balance;
		bool active;
		string email;
		string id;
	}
	
	mapping (address => User) public participants;
	event AmountSent(address from, address to, uint amount);
	event UserAdded(address addr, string email, string id);
	event UserDisabled(address addr, string email, string id);
	
	modifier onlyparticipants { if (!participants[msg.sender].active) throw; _ }
		
	function MonedaAlcala(string email, string id){
		participants[msg.sender] = User(msg.sender,30,true,email,id);
	}
	
	function addUser(address addr, string email, string id, uint amount) onlyparticipants{
		participants[addr] = User(addr,amount,true,email,id);
		UserAdded(addr,email,id);
	}
		
	function disableUser(address user, address balanceReceiver) onlyparticipants returns (uint){
		send(balanceReceiver, participants[user].balance);
		participants[user].active = false;
		UserDisabled(user,participants[user].email,participants[user].id);
	}
	
	function consultBalance(address user) onlyparticipants returns (uint){
		return (participants[user].balance);
	} 
	
	function send(address receiver, uint amount) onlyparticipants {
        	if (participants[msg.sender].balance < amount) return;
		if (!participants[receiver].active) return;
        	participants[msg.sender].balance -= amount;
		participants[receiver].balance += amount;
        	AmountSent(msg.sender, receiver, amount);
    }
    
}
