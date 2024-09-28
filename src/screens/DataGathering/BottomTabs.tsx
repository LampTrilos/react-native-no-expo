import {BottomNavigation, Text} from "react-native-paper";
import React from "react";
import PassportCheck from "./PassportCheck/PassportCheck.tsx";

export default function BottomTabs() {

    //Bottom navigation attributes
    const PassportRoute = () => <PassportCheck/>;
    const BiometricsRoute = () => <Text>Biometrics</Text>;
    const PhotoRoute = () => <Text>Photo</Text>;

    const [index, setIndex] = React.useState(0);
    const [routes] = React.useState([
        {key: 'Passport', title: 'Passport', focusedIcon: 'passport-biometric',},
        {key: 'Biometrics', title: 'Biometrics', focusedIcon: 'fingerprint'},
        {key: 'Photo', title: 'Photo', focusedIcon: 'camera'},
    ]);

    const renderScene = BottomNavigation.SceneMap({
        Passport: PassportRoute,
        Biometrics: BiometricsRoute,
        Photo: PhotoRoute,
    });

    //-----------------End of Bottom navigation routes------------------//
    return (
        <BottomNavigation
            navigationState={{index, routes}}
            onIndexChange={setIndex}
            renderScene={renderScene}
        />
    )
}