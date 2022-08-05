package com.example.studentmanagmentrest.model.dto;



public class StudentDto {

        private String name;

        public StudentDto(String name) {
            this.name = name;
         }

        public StudentDto() { }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    public String getName() {
        return name;
    }
}
