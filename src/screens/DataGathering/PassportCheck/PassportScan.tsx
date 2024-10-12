import {useEffect} from "react";
import {Image, StyleSheet, View} from 'react-native';
import {Card} from "react-native-paper";
import { NativeModules } from 'react-native';
//const { NativeEventEmitter, PassportModule } = NativeModules;
const { PassportModule } = NativeModules;



const PassportScan = ({ style }) => {
    // Function to open the camera
    const openCamera = () => {
        console.log('OPENING CAMERA');
        PassportModule.navigateToMRZActivity(); // Ensure PassportModule is correctly imported
    };

    useEffect(() => {
        // Open the camera immediately when the component mounts
        openCamera();
        })

//     useEffect(() => {
//         const eventEmitter = new NativeEventEmitter(NativeModules.PassportModule);
//
//         // Subscribe to the event
//         const subscription = eventEmitter.addListener('onMRZDataReceived', (data) => {
//             console.log('MRZ Data:', data.mrzData);
//         });
//
//         // Cleanup subscription on unmount
//         return () => {
//             subscription.remove();
//         };
//     }, []); // Empty dependency array means this effect runs once when the component mounts

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
