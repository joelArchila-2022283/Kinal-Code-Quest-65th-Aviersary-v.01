package com.J.M_CODERS.KinalCodeQuest.model.dto;

import lombok.Data;

@Data
public class RegistroDTO {
    private String username;
    private String password;
    private String confirmarPassword;
    private String nombreAvatar;
}