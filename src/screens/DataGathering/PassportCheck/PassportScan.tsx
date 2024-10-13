import {useEffect} from "react";
import {Image, StyleSheet, View} from 'react-native';
import {Card} from "react-native-paper";
import { NativeModules, NativeEventEmitter } from 'react-native';
import {parseMRZ} from "../../../utils/utils.tsx";
import {useDispatch, useSelector} from "react-redux";
import {setMRZData} from "../../../store/CurrentCheck";
const {PassportModule } = NativeModules;



const PassportScan = ({ style }) => {
    //-------------Store section about the currentCheck(Person)---------------------//
    const dispatch = useDispatch();
    //-------------End of store section about the currentCheck(Person)---------------------//

    // Function to open the camera, it calls the PassportModule
    const openCamera = () => {
        //console.log('OPENING CAMERA');
        PassportModule.navigateToMRZActivity(); // Ensure PassportModule is correctly imported
    };


    //When the Page is loaded, it creates a listener that listens to events from the MainActivity, in this case an onMRZDataReceived event with MRZ data
    //It also opens the camera for capture
    useEffect(() => {
        // Open the camera immediately when the component mounts
        openCamera();

        const eventEmitter = new NativeEventEmitter(NativeModules.DeviceEventManagerModule);
        // Subscribe to the event
        const subscription = eventEmitter.addListener('onMRZDataReceived', (data) => {
            console.log('MRZ Data:', data.mrzData);
            const parsedMRData = parseMRZ(data.mrzData)
            console.log(parsedMRData)
            //Update the state about the current Check with the scanned MRZ Data
            dispatch(setMRZData(parsedMRData));
        });

        // Cleanup subscription on unmount
        return () => {
            subscription.remove();
        };
        // Empty dependency array means this effect runs once when the component mounts
    }, []);


return (
    <View style={style}>
        <Card>
            <Card.Title title="Scanned Passport" titleStyle={{fontSize: 17, fontWeight: 'bold'}}/>
            <Card.Content>
                <Image
                    source={{uri: 'https://idscanner-us.s3.amazonaws.com/wp-content/uploads/2022/10/20162707/Sample_Passport.jpg'}}
                    style={[styles.image]}/>
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
});

export default PassportScan;
