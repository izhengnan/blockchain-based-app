// SPDX-License-Identifier: MIT
pragma solidity 0.4.26;

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


    User[] users;
    Carrier[] carriers;
    Shipment[] shipments;


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


    function addCarrier(string _name,string _info,string _passwd) public{
        uint[] s;
        Carrier memory c = Carrier(carriers.length+1,_name,_info,_passwd,s);//创建一个新的承运商对象
        carriers.push(c);//将承运商对象存储至数组中
    }
    
    function checkCarrier(uint _id,string _passwd)public view returns(bool){
        if(keccak256((abi.encodePacked(carriers[_id-1].passwd)))==keccak256((abi.encodePacked(_passwd)))){
            return true;
        }
        return false;
    }
    function getShipmentsByCarrier(uint _id)public view returns(uint[]){
        return carriers[_id-1].shipments;
    }
    //物流信息录入
    function addShipment(uint _id,uint _carrier,string _from,string _to)public{
        carriers[_carrier-1].shipments.push(shipments.length);
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
}