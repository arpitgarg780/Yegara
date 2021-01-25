package com.cbitss.vegara;

import java.util.Random;

class randgenerate {

    String phone;
    randgenerate(String phone) {
        this.phone=phone;
    }

    String randgenerateid() {
        int i=0;
        final String characters = "qwertyuiopasdfghjklzxcvbnm1234567890";
        StringBuilder result = new StringBuilder();
        while(i<8){
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            result.append(phone.charAt(i));
            i++;
        }
        while (i<24){
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            i++;
        }
        return result.toString();
    }
}
