package com.parkingapp.hulapark.FragmentContainers;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFrag#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CardInfo {
    private String cardNumber;
    private String expiryDate;
    private String cvc;
    private String postalCode;

    public CardInfo(String cardNumber, String expiryDate, String cvc, String postalCode) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvc = cvc;
        this.postalCode = postalCode;
    }

    // Getters and setters if needed (optional for Gson)
}
