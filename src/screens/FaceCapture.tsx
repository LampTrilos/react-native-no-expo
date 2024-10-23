
import React, {useEffect, useState} from 'react';
import {Button, Card, Text} from "react-native-paper";
import {NativeModules, NativeEventEmitter} from 'react-native';
import {useDispatch, useSelector} from "react-redux";
import {setFaceData} from "../store/CurrentCheck.ts";


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
        <>
            <Card>
                <Card.Title title="Title" subtitle="Subtitle"/>
                <Card.Content>

                    <Text variant="titleLarge">{currentCheck.faceData.base64Value}</Text>
                    <Text variant="bodyMedium">Card content</Text>
                </Card.Content>
                <Card.Cover source={{ uri: 'https://picsum.photos/700' }} />
                <Card.Actions>
                    <Button>Cancel</Button>
                    <Button onPress={onPress}>Ok</Button>
                </Card.Actions>
            </Card>
        </>
    );
};
