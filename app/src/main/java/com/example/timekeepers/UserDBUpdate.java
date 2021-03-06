package com.example.timekeepers;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public final class UserDBUpdate {

    public UserDBUpdate() {}

    public static void updateUserInformation(String userName, String userEmail) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("Users_Name", userName);
        userInfo.put("Email", userEmail);

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userEmail)
                .set(userInfo);
    }
}
