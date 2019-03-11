package com.example.arshit.serversideecom.Model;

public class Order {

    private String CartId;
    private String ProductCity;
    private String ProductCountry;
    private String ProductAddress;
    private String ProductContact;
    private String ProductAmount;


    public Order() {
    }

    public Order(String cartId, String productCity, String productCountry, String productAddress, String productContact, String productAmount) {
        CartId = cartId;
        ProductCity = productCity;
        ProductCountry = productCountry;
        ProductAddress = productAddress;
        ProductContact = productContact;
        ProductAmount = productAmount;
    }

    public String getCartId() {
        return CartId;
    }

    public void setCartId(String cartId) {
        CartId = cartId;
    }

    public String getProductCity() {
        return ProductCity;
    }

    public void setProductCity(String productCity) {
        ProductCity = productCity;
    }

    public String getProductCountry() {
        return ProductCountry;
    }

    public void setProductCountry(String productCountry) {
        ProductCountry = productCountry;
    }

    public String getProductAddress() {
        return ProductAddress;
    }

    public void setProductAddress(String productAddress) {
        ProductAddress = productAddress;
    }

    public String getProductContact() {
        return ProductContact;
    }

    public void setProductContact(String productContact) {
        ProductContact = productContact;
    }

    public String getProductAmount() {
        return ProductAmount;
    }

    public void setProductAmount(String productAmount) {
        ProductAmount = productAmount;
    }


}
