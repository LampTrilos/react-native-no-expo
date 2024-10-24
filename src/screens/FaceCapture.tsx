
import React, {useEffect, useState} from 'react';
import {Button, Card, Text} from "react-native-paper";
import {NativeModules, NativeEventEmitter, Image, StyleSheet, View} from 'react-native';
import {useDispatch, useSelector} from "react-redux";
import {setFaceData} from "../store/CurrentCheck.ts";
import CustomTheme from "../assets/Theme"


export default function FaceCapture() {
    const {BiometricModule} = NativeModules;
    //-------------Store section about the currentCheck(Person)---------------------//
     const currentCheck = useSelector(state => state.currentCheckStore);
     const dispatch = useDispatch();
    //-------------End of store section about the currentCheck(Person)---------------------//
//     console.log('Calling Biometric Module.........................ASDFASDFSADFASDFSAFD')
//     BiometricModule.navigateToFaceActivity() ;

    //Upon press of the button, begin the FaceActivity to capture the image of the person
    const onPress = () => {
        //CalendarModule.createCalendarEvent('testName', 'testLocation');
    console.log('Calling Biometric Module.........................ASDFASDFSADFASDFSAFD')
    BiometricModule.navigateToFaceActivity() ;
    };

    //When the Page is loaded, it creates a listener that listens to events from the FaceActivity, in this case an onFaceDataReceived event
    //It also opens the camera for capture
    useEffect(() => {
        console.log('Starting FaceActivity...')
        const eventEmitter = new NativeEventEmitter(NativeModules.DeviceEventManagerModule);
        // Subscribe to the event from NFCActivity
        const subscriptionNFC = eventEmitter.addListener('onFaceDataReceived', (data) => {
            console.log('Face Data:', data.faceData);
            //const parsedNFCData = parseMRZ(data.nfcData)
            console.log(JSON.parse(data.faceData))
            //Update the state about the current Check with the scanned MRZ Data, while also turning String back to json
            dispatch(setFaceData(JSON.parse(data.faceData)));
            //If the MRZ Scan was successful, begin a new subscription, this time for the NFC Scan
        });

        // Cleanup subscription on unmount
        return () => {
            subscriptionNFC.remove();
        };
        // Empty dependency array means this effect runs once when the component mounts
    }, []);

    return (
        <View style={styles.container}>
            <Text >{currentCheck.faceData.base64Value}</Text>
            <Image
                source={require('./../assets/demo-face.jpg')}
                style={styles.image}
            />
            <View style={styles.containerRow}>
            {/*<Button > Cancel </Button>*/}
            <Button mode="contained"   icon="repeat" onPress={onPress}>Επανάληψη</Button>
            </View>
        </View>
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
    image: {
        width: '100%',
        height: '70%',
        resizeMode: 'contain',
    },
    containerRow: {
        flexDirection: 'row', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginBottom: 5,
    },
});