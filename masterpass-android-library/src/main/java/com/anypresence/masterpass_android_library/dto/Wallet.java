package com.anypresence.masterpass_android_library.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by diego.rotondale on 1/20/2015.
 */
public class Wallet implements Serializable {
    public static final String WALLET_NAME = "wallet_name";
    public static final String PRE_CHECKOUT_TRANSACTION_ID_KEY = "precheckout_transaction_id";
    public static final String CONSUMER_WALLET_ID_KEY = "consumer_wallet_id";
    public static final String WALLET_PARTNER_LOGO_URL_KEY = "wallet_partner_logo_url";
    public static final String MASTERPASS_LOGO_URL_KEY = "masterpass_logo_url";

    @SerializedName(WALLET_NAME)
    String walletName;
    @SerializedName(PRE_CHECKOUT_TRANSACTION_ID_KEY)
    String preCheckoutTransactionId;
    @SerializedName(CONSUMER_WALLET_ID_KEY)
    String consumerWalletId;
    @SerializedName(WALLET_PARTNER_LOGO_URL_KEY)
    String walletPartnerLogoUrl;
    @SerializedName(MASTERPASS_LOGO_URL_KEY)
    String masterpassLogoUrl;

}
