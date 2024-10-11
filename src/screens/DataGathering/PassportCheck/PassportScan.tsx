import {Image, StyleSheet, View,  PermissionsAndroid} from 'react-native';
import {useEffect} from "react";
import {Card} from "react-native-paper";
import MrzReader, { CameraSelector, DocType } from 'react-native-mrz-reader';

const PassportScan = ({style}) => {

    async function requestCameraPermission() {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.CAMERA,
          {
            title: 'Camera Permission',
            message: 'This app needs access to your camera to scan MRZ.',
            buttonNeutral: 'Ask Me Later',
            buttonNegative: 'Cancel',
            buttonPositive: 'OK',
          }
        );
        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
          console.log('You can use the camera');
        } else {
          console.log('Camera permission denied');
        }
      } catch (err) {
        console.warn(err);
      }
    }

  // Request camera permission when the component mounts
  useEffect(() => {
    requestCameraPermission();
  }, []);

   return (
       <MrzReader
         style={{width: '40%', height: '40%'}}
         docType={DocType.ID}
         cameraSelector={CameraSelector.Back}
         onMRZRead={(mrz: string) => {
             console.log('asdfADFADF S8DF S9D8F 09SDAFSADF SDA98F ASF8SADF SADF SA FASFDASFAS SA A FASFD AS FASDF AS F')
           console.log(mrz)
         }}
       />
//        <View style={style}>
//         <Card>
//             <Card.Title title="Scanned Passport" titleStyle={{fontSize: 17, fontWeight: 'bold'}}/>
//             <Card.Content>
//                 <Image
//                     source={{uri: 'https://idscanner-us.s3.amazonaws.com/wp-content/uploads/2022/10/20162707/Sample_Passport.jpg'}}
//                     style={[styles.image]}/>
//             </Card.Content>
//         </Card>
//     </View>
  );
};
const styles = StyleSheet.create({
    image: {
        width: '100%',
        height: '88%',
        resizeMode: 'contain',
    },
});

export default PassportScan;
