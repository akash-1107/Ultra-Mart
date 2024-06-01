package com.ultramart.backend.dto;

import lombok.Data;

@Data
public class UserDto {
        
        private Long id;
        
        private String email;
        
        private String password;
        
        private String name;
    
        private String role;
    
        private byte[] image;    
        
        public UserDto() {
        }
    
        public UserDto(Long id, String email, String password, String name, String role, byte[] image) {
            this.id = id;
            this.email = email;
            this.password = password;
            this.name = name;
            this.role = role;
            this.image = image;
        }
    
        public Long getId() {
            return id;
        }
    
        public String getEmail() {
            return email;
        }
    
        public String getPassword() {
            return password;
        }
    
        public String getName() {
            return name;
        }
    
        public String getRole() {
            return role;
        }
    
        public byte[] getImage() {
            return image;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public void setRole(String role) {
            this.role = role;
        }
    
        public void setImage(byte[] image) {
            this.image = image;
        }
}
