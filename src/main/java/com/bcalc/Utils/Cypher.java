/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.Utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;
import org.jasypt.properties.PropertyValueEncryptionUtils;

/**
 *
 * @author kukushkin.dv
 */
public class Cypher {

    public static void main(String[] args) {

        SimplePBEConfig config = new SimplePBEConfig();
        config.setAlgorithm("PBEWithMD5AndTripleDES");
        config.setKeyObtentionIterations(1000);
        config.setPassword("CodePharse");

        StandardPBEStringEncryptor encryptor = new org.jasypt.encryption.pbe.StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        encryptor.initialize();
        String value = "EncryptionValue";
        String encoded = PropertyValueEncryptionUtils.encrypt(value, encryptor);
        System.out.println("pass " + encoded);
        System.out.println("pass " + PropertyValueEncryptionUtils.decrypt(encoded, encryptor));

    }

}
