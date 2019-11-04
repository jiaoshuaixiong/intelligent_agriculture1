package com.topwulian.advice;

import lombok.Data;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * @Author: szz
 * @Date: 2019/6/30 下午2:17
 * @Version 1.0
 */
public class TestLambda {
    public static void main(String[] args) {
        User user = new User();
        user.setAge(1);
        user.setName(null);
        user.find(user);
    }

}
@Data
class User{
    @NonNull String name;
    @Size(min = 3)
    Integer age;

    public User() {

    }

    void find(User user){
        System.out.println("jinlia");
    }
}



