package com.paygilant.mywallet.OnWallet;


import com.paygilant.mywallet.Contact.ContactModel;

public class Singlton {
    private static Singlton mInstance = null;


    private String amount;

    private  Boolean canAccess;


    private ContactModel contactModel;

    public static Singlton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singlton();
        }
        return mInstance;
    }

    private Singlton(){
        contactModel = null;
        amount = "0";
        canAccess = false;
    }

    public Boolean getCanAccess() {
        return canAccess;
    }

    public void setCanAccess(Boolean canAccess) {
        this.canAccess = canAccess;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ContactModel getContactModel() {
        return contactModel;
    }

    public void setContactModel(ContactModel contactModel) {
        this.contactModel = contactModel;
    }
}

