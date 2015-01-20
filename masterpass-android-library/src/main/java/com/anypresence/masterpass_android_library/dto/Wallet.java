package com.anypresence.masterpass_android_library.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diego.rotondale on 1/20/2015.
 */
public class Wallet {
    @SerializedName("precheckout_transaction_id")
    String preCheckoutTransactionId;
    @SerializedName("wallet_name")
    String walletName;
    @SerializedName("consumer_wallet_id")
    String consumerWalletId;
}
