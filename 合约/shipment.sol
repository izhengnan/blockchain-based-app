// SPDX-License-Identifier: MIT
pragma solidity 0.4.25;
pragma experimental ABIEncoderV2;

contract shipment{

    struct User{//货主
        uint id;
        string name;
        string passwd;
    }
    struct Carrier{//承运商
        uint id;
        string name;
        string info;
        string passwd;
        uint[] shipments;
    }
    struct Shipment{//物流信息
        uint id;
        uint user_id;
        uint carrier;
        string from;
        string to;
        string status;
        uint createTime;
        uint updateTime;
    }
    struct Admin{//管理员
        uint id;
        string account;
        string passwd;
    }

    Admin[] admins;
    User[] users;
    Carrier[] carriers;
    Shipment[] shipments;

    // 构造函数，初始化默认管理员账户
    function() public {
        // 初始化默认管理员：账号admin，密码admin123
        Admin memory admin = Admin(1, "admin", "admin");
        admins.push(admin);
    }

    function addUser(string _name,string _passwd) public{
        User memory u = User(users.length+1,_name,_passwd);//创建一个新的货主对象
        users.push(u);//将货主对象存储至数组中
    }

    function checkUser(uint _id,string _passwd)public view returns(bool){
        if(keccak256((abi.encodePacked(users[_id-1].passwd)))==keccak256((abi.encodePacked(_passwd)))){
            return true;
        }
        return false;
    }

    // 查询所有货主信息
    function getAllUsers() public view returns(uint[], string[], string[]){
        uint[] memory ids = new uint[](users.length);
        string[] memory names = new string[](users.length);
        string[] memory passwds = new string[](users.length);
        
        for(uint i = 0; i < users.length; i++){
            ids[i] = users[i].id;
            names[i] = users[i].name;
            passwds[i] = users[i].passwd;
        }
        return (ids, names, passwds);
    }

    // 修改货主密码
    function updateUserPassword(uint _id, string _newPasswd) public returns(bool){
        if(_id > 0 && _id <= users.length){
            users[_id-1].passwd = _newPasswd;
            return true;
        }
        return false;
    }


    function addCarrier(string _name,string _info,string _passwd) public{
        uint[] storage s;
        Carrier memory c = Carrier(carriers.length+1,_name,_info,_passwd,s);//创建一个新的承运商对象
        carriers.push(c);//将承运商对象存储至数组中
    }
    
    function checkCarrier(uint _id,string _passwd)public view returns(bool){
        if(keccak256((abi.encodePacked(carriers[_id-1].passwd)))==keccak256((abi.encodePacked(_passwd)))){
            return true;
        }
        return false;
    }

    // 管理员登录验证
    function checkAdmin(string _account,string _passwd)public view returns(bool){
        for(uint i = 0; i < admins.length; i++){
            if(keccak256((abi.encodePacked(admins[i].account)))==keccak256((abi.encodePacked(_account)))){
                if(keccak256((abi.encodePacked(admins[i].passwd)))==keccak256((abi.encodePacked(_passwd)))){
                    return true;
                }
            }
        }
        return false;
    }

    // 添加新管理员
    function addAdmin(string _account,string _passwd) public {
        Admin memory admin = Admin(admins.length+1, _account, _passwd);
        admins.push(admin);
    }

    // 获取所有管理员信息
    function getAllAdmins() public view returns(uint[], string[], string[]) {
        uint[] memory ids = new uint[](admins.length);
        string[] memory accounts = new string[](admins.length);
        string[] memory passwds = new string[](admins.length);
        
        for(uint j = 0; j < admins.length; j++){
            ids[j] = admins[j].id;
            accounts[j] = admins[j].account;
            passwds[j] = admins[j].passwd;
        }
        return (ids, accounts, passwds);
    }

    // 修改管理员密码
    function updateAdminPassword(string _account, string _newPasswd) public returns(bool){
        for(uint k = 0; k < admins.length; k++){
            if(keccak256((abi.encodePacked(admins[k].account)))==keccak256((abi.encodePacked(_account)))){
                admins[k].passwd = _newPasswd;
                return true;
            }
        }
        return false;
    }

    function getShipmentsByCarrier(uint _id)public view returns(uint[]){
        return carriers[_id-1].shipments;
    }
    //物流信息录入
    function addShipment(uint _id,uint _carrier,string _from,string _to)public{
        carriers[_carrier-1].shipments.push(shipments.length+1);
        Shipment memory s =Shipment(shipments.length+1,_id,_carrier,_from,_to,"待揽收",block.timestamp,block.timestamp);
        shipments.push(s);
    }
    //物流信息更新
    function updateShipment(uint carrier_id,uint shipment_id,string _status)public{
        if (shipments[shipment_id-1].carrier==carrier_id){
            shipments[shipment_id-1].status =_status;
            shipments[shipment_id-1].updateTime = block.timestamp;
        }
    }
    //物流状态查询
    function getStatus(uint _id) public view returns(string){
        return shipments[_id-1].status;
    }
    //物流信息查询
    function getShipment(uint _id) public view returns(uint,uint,uint,string,string,string,uint,uint){
        Shipment memory s =shipments[_id-1];
        return (s.id,s.user_id,s.carrier,s.from,s.to,s.status,s.createTime,s.updateTime);
    }

    // ============ 物流信息相关接口 ============
    
    // 删除物流信息
    function deleteShipment(uint _shipment_id) public returns(bool){
        require(_shipment_id > 0 && _shipment_id <= shipments.length, "Shipment ID does not exist");
        
        // 获取要删除的物流信息
        uint shipmentIndex = _shipment_id - 1;
        uint carrierId = shipments[shipmentIndex].carrier;
        
        // 从承运商的shipments数组中移除此物流ID
        uint[] storage carrierShipments = carriers[carrierId - 1].shipments;
        for(uint l = 0; l < carrierShipments.length; l++){
            if(carrierShipments[l] == _shipment_id){
                // 将最后一个元素移到当前位置
                carrierShipments[l] = carrierShipments[carrierShipments.length - 1];
                carrierShipments.length--;
                break;
            }
        }
        
        // 调整后续物流信息的ID
        for(uint m = shipmentIndex; m < shipments.length - 1; m++){
            shipments[m] = shipments[m + 1];
            shipments[m].id = shipments[m].id - 1; // ID减1
        }
        shipments.length--;
        
        return true;
    }

    // 查询所有物流信息
    function getAllShipments() public view returns(uint[],uint[],uint[],string[],string[],string[]){
        if(shipments.length == 0){
            return (new uint[](0), new uint[](0), new uint[](0), new string[](0), new string[](0), new string[](0));
        }
        
        uint[] memory ids = new uint[](shipments.length);
        uint[] memory userIds = new uint[](shipments.length);
        uint[] memory carriersArr = new uint[](shipments.length);
        string[] memory froms = new string[](shipments.length);
        string[] memory tos = new string[](shipments.length);
        string[] memory statuses = new string[](shipments.length);
        
        for(uint n = 0; n < shipments.length; n++){
            ids[n] = shipments[n].id;
            userIds[n] = shipments[n].user_id;
            carriersArr[n] = shipments[n].carrier;
            froms[n] = shipments[n].from;
            tos[n] = shipments[n].to;
            statuses[n] = shipments[n].status;
        }
        
        return (ids, userIds, carriersArr, froms, tos, statuses);
    }


    // ============ 货主相关接口 ============
    
    // 按ID查询单个货主信息
    function getUserById(uint _id) public view returns(uint,string,string){
        require(_id > 0 && _id <= users.length, "User ID does not exist");
        User memory u = users[_id-1];
        return (u.id, u.name, u.passwd);
    }

    // 删除货主
    function deleteUser(uint _id) public returns(bool){
        require(_id > 0 && _id <= users.length, "User ID does not exist");
        
        // 检查该用户是否有关联的物流信息
        for(uint q = 0; q < shipments.length; q++){
            if(shipments[q].user_id == _id){
                return false; // 有关联物流信息，不能删除
            }
        }
        
        // 将要删除的用户后面的用户ID减1
        for(uint r = 0; r < users.length; r++){
            if(users[r].id > _id){
                users[r].id = users[r].id - 1;
            }
        }
        
        // 调整物流信息中的user_id
        for(uint s = 0; s < shipments.length; s++){
            if(shipments[s].user_id > _id){
                shipments[s].user_id = shipments[s].user_id - 1;
            }
        }
        
        // 实际删除用户（通过交换到数组末尾然后删除）
        for(uint t = _id - 1; t < users.length - 1; t++){
            users[t] = users[t + 1];
        }
        users.length--;
        
        return true;
    }

    // ============ 承运商相关接口 ============

    // 查询所有承运商信息
    function getAllCarriers() public view returns(uint[], string[], string[], string[]){
        uint[] memory ids = new uint[](carriers.length);
        string[] memory names = new string[](carriers.length);
        string[] memory infos = new string[](carriers.length);
        string[] memory passwds = new string[](carriers.length);
        
        for(uint u = 0; u < carriers.length; u++){
            ids[u] = carriers[u].id;
            names[u] = carriers[u].name;
            infos[u] = carriers[u].info;
            passwds[u] = carriers[u].passwd;
        }
        return (ids, names, infos, passwds);
    }

    // 按ID查询单个承运商信息
    function getCarrierById(uint _id) public view returns(uint,string,string,string){
        require(_id > 0 && _id <= carriers.length, "Carrier ID does not exist");
        Carrier memory c = carriers[_id-1];
        return (c.id, c.name, c.info, c.passwd);
    }

    // 修改承运商信息
    function updateCarrierInfo(uint _id, string _name, string _info) public returns(bool){
        require(_id > 0 && _id <= carriers.length, "Carrier ID does not exist");
        carriers[_id-1].name = _name;
        carriers[_id-1].info = _info;
        return true;
    }

    // 修改承运商密码
    function updateCarrierPassword(uint _id, string _newPasswd) public returns(bool){
        require(_id > 0 && _id <= carriers.length, "Carrier ID does not exist");
        carriers[_id-1].passwd = _newPasswd;
        return true;
    }

    // 删除承运商
    function deleteCarrier(uint _id) public returns(bool){
        require(_id > 0 && _id <= carriers.length, "Carrier ID does not exist");
        
        // 检查该承运商是否有关联的物流信息
        for(uint v = 0; v < shipments.length; v++){
            if(shipments[v].carrier == _id){
                return false; // 有关联物流信息，不能删除
            }
        }
        
        // 调整物流信息中的carrier
        for(uint w = 0; w < shipments.length; w++){
            if(shipments[w].carrier > _id){
                shipments[w].carrier = shipments[w].carrier - 1;
            }
        }
        
        // 实际删除承运商（通过交换到数组末尾然后删除）
        for(uint x = _id - 1; x < carriers.length - 1; x++){
            carriers[x] = carriers[x + 1];
        }
        carriers.length--;
        
        return true;
    }
}