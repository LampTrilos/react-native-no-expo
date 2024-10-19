import React, {useEffect, useState} from 'react';
import {View, ScrollView, Image, StyleSheet} from 'react-native';
import {Text, Card, IconButton} from 'react-native-paper';
import CustomTheme from "../../../assets/Theme"
import CheckIcon from "../../../components/CheckIcon.tsx";
import {NativeModules, NativeEventEmitter} from 'react-native';
import {useDispatch, useSelector} from "react-redux";
import {setNFCData} from "../../../store/CurrentCheck.ts";
import {formatDateString} from "../../../utils/utils.tsx";

export default function PersonInformationNFC({style, currentCheck}) {
    // Assuming person is passed as a prop with name, surname, and dateOfBirth properties

    //------------------------------------Section to check if the passport valid until date is past today-----------------------------//
    // Function to check if passportExpiry is past today
    const [isPassportExpired, setIsPassportExpired] = useState(false);
    const checkDate = () => {
        // Get today's date at midnight
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        // Create a date object from person.date2
        const passportExpiry = new Date(currentCheck.nfcData.dateOfExpiry);
        // Check if date2 is before today
        setIsPassportExpired(passportExpiry < today);
    }
    // useEffect to run the check when the component mounts or when person.date2 changes
    useEffect(() => {
        checkDate();
    }, [currentCheck.nfcData.dateOfExpiry]); // Dependency array: check whenever person.validUntil changes
    const dispatch = useDispatch();
    //------------------------------------End of section to check if the passport valid until date is past today-----------------------------//



    //When the Page is loaded, it creates a listener that listens to events from the NFCActivity, in this case an onNFCDataReceived event
    //It also opens the camera for capture
    useEffect(() => {
        console.log('Starting NFC...')
        const eventEmitter = new NativeEventEmitter(NativeModules.DeviceEventManagerModule);
        // Subscribe to the event from NFCActivity
        const subscriptionNFC = eventEmitter.addListener('onNFCDataReceived', (data) => {
            //console.log('NFC Data:', data.nfcData);
            //const parsedNFCData = parseMRZ(data.nfcData)
            //console.log(data.nfcData)
            //Update the state about the current Check with the scanned MRZ Data, while also turning String back to json
            dispatch(setNFCData(JSON.parse(data.nfcData)));
            //If the MRZ Scan was successful, begin a new subscription, this time for the NFC Scan
        });

        // Cleanup subscription on unmount
        return () => {
            subscriptionNFC.remove();
        };
        // Empty dependency array means this effect runs once when the component mounts
    }, [currentCheck.nfcData]);


    return (
        <View style={style}>
            <Card style={{height: '100%'}}>
                <Card.Title title="Πληροφορίες chip διαβατηρίου" titleStyle={{fontSize: 17, fontWeight: 'bold'}}
                            style={{padding: -5}}/>
                <Card.Content >
                    <View style={styles.containerRow}>
                        <View style={styles.containerColumn}>
                            <Image
                                source={{ uri: `data:image/png;base64,${currentCheck.nfcData.faceImage}` }}
                                style={styles.image}
                            />
                            <CheckIcon textShown={"Chip"} attributeChecked={currentCheck.chipChecked}/>
                            <CheckIcon textShown={"MRZ"} attributeChecked={currentCheck.mrzChecked}/>
                        </View>
                        <View style={styles.containerColumn}>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Επώνυμο:</Text>
                                <Text>{currentCheck.nfcData.familyName}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Όνομα:</Text>
                                <Text>{currentCheck.nfcData.firstName}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Υπηκοότητα:</Text>
                                <Text>{currentCheck.nfcData.nationality}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Ημ/νία Γέννησης:</Text>
                                <Text>{formatDateString(currentCheck.nfcData.dateOfBirth, true)}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Φύλο:</Text>
                                <Text>{currentCheck.nfcData.gender}</Text>
                            </View>
                        </View>
                        <View style={styles.containerColumn}>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Χώρα Έκδοσης:</Text>
                                <Text>{currentCheck.nfcData.issueCountry}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Αριθμός Εγγράφου:</Text>
                                <Text>{currentCheck.nfcData.documentNumber}</Text>
                            </View>
                            <View style={{marginBottom: 8}}>
                                <Text style={{fontWeight: 'bold'}}>Έγκυρο μέχρι:</Text>
                                <Text>{formatDateString(currentCheck.nfcData.dateOfExpiry, false)}</Text>
                            </View>
                        </View>
                    </View>
                    <View>
                        {/* Show red exclamation mark and red text if passportExpiry is past */}
                        {isPassportExpired && (
                            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                                <IconButton icon="alert-circle" iconColor="red" size={24} />
                                <Text style={{ color: 'red', fontSize: 14 }}>Το έγγραφο έχει λήξει</Text>
                            </View>
                        )}
                    </View>
                </Card.Content>
            </Card>
        </View>
    )
        ;
};
const styles = StyleSheet.create({
    containerRow: {
        //flex: 1,
        flexDirection: 'row', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginBottom: 5,
        alignItems: 'flex-start', // Ensures content aligns at the top
        justifyContent: 'flex-start', // Spreads columns apart if needed
        paddingVertical: 15
    },
    containerColumn: {
        //flex: 1,
        flexDirection: 'column', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginLeft: 5,
        paddingHorizontal: 10
    },
    image: {
        width: 150,
        height: 150,
        resizeMode: 'contain',
    },
});
