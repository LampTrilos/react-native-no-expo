import {useEffect, useState} from "react";
import {Image, StyleSheet, View} from 'react-native';
import {Text, Card, IconButton} from 'react-native-paper';
import {parseMRZ} from "../../../utils/utils.tsx";
import {NativeModules, NativeEventEmitter} from 'react-native';
import {useDispatch, useSelector} from "react-redux";
import {setMRZData, setNFCData} from "../../../store/CurrentCheck";

const {PassportModule} = NativeModules;
import CustomTheme from "../../../assets/Theme"


//This page shows the MRZ Data and also inits the MRZ and then the NFC Scan
const PersonInformationMRZ = ({style}) => {
    //-------------Store section about the currentCheck(Person)---------------------//
    //Store section about the mrz data check
    const scannedMRZData = useSelector(state => state.currentCheckStore.mrzData);
    const dispatch = useDispatch();
    //-------------End of store section about the currentCheck(Person)---------------------//
    // Function to check if the capture has already started, in order to prevent a subsequent run without the first having finished
    //const [isCaptureRunning, setIsCaptureRunning] = useState(false);


    // Function to open the camera, it calls the PassportModule
    const openCamera = () => {
        //console.log('OPENING CAMERA');
        PassportModule.navigateToMRZActivity(); // Ensure PassportModule is correctly imported
    };


    //When the Page is loaded, it creates a listener that listens to events from the MainActivity, in this case an onMRZDataReceived event with MRZ data
    //It also opens the camera for capture
    useEffect(() => {
        console.log('New MRZ Page..')
        //to prevent a subsequent run without the first having finished
        //if (!isCaptureRunning) {
            // Open the camera immediately when the component mounts
            console.log('Opening camera..')
            //setIsCaptureRunning(true)
            openCamera();

            const eventEmitter = new NativeEventEmitter(NativeModules.DeviceEventManagerModule);
            // Subscribe to the event from MainActivity
            const subscriptionMRZ = eventEmitter.addListener('onMRZDataReceived', (data) => {
                //console.log('MRZ Data:', data.mrzData);
                const parsedMRZData = parseMRZ(data.mrzData)
                //console.log(parsedMRZData)
                //Update the state about the current Check with the scanned MRZ Data
                dispatch(setMRZData(parsedMRZData));
                //If the MRZ Scan was successful, begin a new subscription, this time for the NFC Scan
                PassportModule.navigateToNFCActivity(data.mrzData);
            });

            // Cleanup subscription on unmount
            return () => {
                subscriptionMRZ.remove();
            };
            // Empty dependency array means this effect runs once when the component mounts
        //}
        }, []);


    return (
        <View style={style}>
            <Card>
                <Card.Title title="Πληροφορίες MRZ" titleStyle={{fontSize: 17, fontWeight: 'bold'}}/>
                <Card.Content>
                    <View style={styles.containerColumn}>
                        <View style={{marginBottom: 8}}>
                            <Text style={{fontWeight: 'bold'}}>Αριθμός Εγγράφου:</Text>
                            <Text>{scannedMRZData?.documentNumber}</Text>
                        </View>
                        <View style={{marginBottom: 8}}>
                            <Text style={{fontWeight: 'bold'}}>Έγκυρο μέχρι:</Text>
                            <Text>{scannedMRZData?.dateOfExpiry}</Text>
                        </View>
                        <View style={{marginBottom: 8}}>
                            <Text style={{fontWeight: 'bold'}}>Ημ/νία Γέννησης:</Text>
                            <Text>{scannedMRZData?.dateOfBirth}</Text>
                        </View>
                        {/*<Image*/}
                        {/*    source={{uri: 'https://idscanner-us.s3.amazonaws.com/wp-content/uploads/2022/10/20162707/Sample_Passport.jpg'}}*/}
                        {/*    style={[styles.image]}/>*/}
                    </View>
                </Card.Content>
            </Card>
        </View>
    )
};

const styles = StyleSheet.create({
    image: {
        width: '100%',
        height: '88%',
        resizeMode: 'contain',
    },
    containerColumn: {
        //flex: 1,
        flexDirection: 'column', // This makes the children arrange in a row
        backgroundColor: CustomTheme.colors.background,
        marginLeft: 5,
        paddingHorizontal: 10
    },
});

export default PersonInformationMRZ;
