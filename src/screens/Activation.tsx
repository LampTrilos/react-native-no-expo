
import React, {useEffect, useState} from 'react';
import {Button, Card, Text} from "react-native-paper";
import {NativeModules, NativeEventEmitter} from 'react-native';
import {StyleSheet, View} from "react-native";

export default function ActivationScreen() {
    const {BiometricModule} = NativeModules;
    //When the Page is loaded, it immediately opens ActivationActivity
    useEffect(() => {
        console.log('Starting ActivationActivity...')
        BiometricModule.navigateToActivationActivity() ;
        // Empty dependency array means this effect runs once when the component mounts
    }, []);

    function onPress() {
        BiometricModule.navigateToActivationActivity() ;
        }

    return (
        <>
        <View style={styles.container}>
            <Button mode="contained" onPress={onPress} style={styles.button}>
                Οθόνη Ενεργοποίησης
            </Button>
        </View>
        </>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
        alignItems: 'center',
        backgroundColor: 'white',
        padding: 15,
        borderRadius: 15,
    },
    button: {
        top: '45%'
    }
})
