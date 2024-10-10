import {BottomNavigation, Text} from "react-native-paper";
import React from "react";
import PassportCheck from "./PassportCheck/PassportCheck.tsx";
import SearchResults from "./SearchResults/SearchResults.tsx";
import FaceCapture from "../FaceCapture.tsx";
import FakeScreen2 from "../FakeScreen2.tsx";

export default function BottomTabs() {

    //Bottom navigation attributes
    const PassportRoute = () => <PassportCheck/>;
    const PhotoRoute = () => <FakeScreen2/>;
    const BiometricsRoute = () => <FaceCapture/>;
    const ResultsRoute = () => <SearchResults/>;

    const [index, setIndex] = React.useState(0);
    const routes = [
        {key: 'Passport', title: 'Passport', focusedIcon: 'passport-biometric',},
        {key: 'Photo', title: 'Photo', focusedIcon: 'camera'},
        {key: 'Biometrics', title: 'Biometrics', focusedIcon: 'fingerprint'},
        {key: 'Results', title: 'Results', focusedIcon: 'account-search'},
    ];

    const renderScene = BottomNavigation.SceneMap({
        Passport: PassportRoute,
        Biometrics: BiometricsRoute,
        Photo: PhotoRoute,
        Results: ResultsRoute,
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
