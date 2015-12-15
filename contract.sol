contract MonedaAlcala {
	struct User {
		address addr;
		uint balance;
		bool active;
		string email;
		string id;
	}
	mapping (address => User) public participants;
	
	event Sent(address from, address to, uint amount);
	
	function MonedaAlcala(string email, string id){
		participants[msg.sender] = User(msg.sender,0,true,email,id);
	}
	
	function addUser(address addr, string email, string id, uint amount) onlyparticipants{
		participants[addr] = User(addr,amount,true,email,id);
	}
		
	modifier onlyparticipants { if (!participants[msg.sender].active) throw; _ }
	
	function send(address receiver, uint amount) onlyparticipants {
        if (participants[msg.sender].balance < amount) return;
		if (!participants[receiver].active) return;
        participants[msg.sender].balance -= amount;
        participants[receiver].balance += amount;
        Sent(msg.sender, receiver, amount);
    }
}
