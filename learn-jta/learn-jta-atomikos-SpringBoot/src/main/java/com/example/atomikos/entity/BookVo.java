package com.example.atomikos.entity;

import lombok.Data;

@Data
public class BookVo extends BookDO {

    private UserDO user;
}
