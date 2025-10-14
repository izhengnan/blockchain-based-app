// SPDX-License-Identifier: MIT
pragma solidity 0.4.26;

contract Shipment{

    struct user{//货主
        uint id;
        string name;

    }
    struct carrier{//承运商
        uint id;
        string name;
        string info;
    }
    struct shipment{//物流信息
        uint id;
        string from;
        string to;
        string statous;
        uint createTime;
        uint updataTime;
        
    }
    
}