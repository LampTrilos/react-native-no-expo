import {Image, StyleSheet, View} from 'react-native';
import {Card} from "react-native-paper";
//Use https://github.com/mat2718/vision-camera-mrz-scanner?tab=readme-ov-file
const PassportScan = ({style}) => (
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
);

const styles = StyleSheet.create({
    image: {
        width: '100%',
        height: '88%',
        resizeMode: 'contain',
    },
});

export default PassportScan;
