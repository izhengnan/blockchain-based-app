// SPDX-License-Identifier: MIT
pragma solidity 0.4.26;

contract Shipment{

    struct user{//货主
        uint id;
        string name;
        string passwd;
    }
    struct carrier{//承运商
        uint id;
        string name;
        string info;
        string passwd;
    }
    struct shipment{//物流信息
        uint id;
        string from;
        string to;
        string status;
        uint createTime;
        uint updateTime;
    }
    user[] users;
    carrier[] carriers;

    function addUser(string _name,string _passwd) public{
        user memory u = user(users.length+1,_name,_passwd);//创建一个新的货主对象
        users.push(u);//将货主对象存储至数组中
    }

    function checkUser(uint _id,string _passwd)public view returns(bool){
        if(keccak256(users[_id-1].passwd)==keccak256(_passwd)){
            return true;
        }
        return false;
    }


    function addCarrier(string _name,string _info,string _passwd) public{
        carrier memory c = carrier(carriers.length+1,_name,_info,_passwd);//创建一个新的承运商对象
        carriers.push(c);//将承运商对象存储至数组中
    }
    
    function checkCarrier(uint _id,string _passwd)public view returns(bool){
        if(keccak256(carriers[_id-1].passwd)==keccak256(_passwd)){
            return true;
        }
        return false;
    }
}