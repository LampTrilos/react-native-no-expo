import {BottomNavigation, Text} from "react-native-paper";
import React from "react";
import PassportCheck from "./PassportCheck/PassportCheck.tsx";
import SearchResults from "./SearchResults/SearchResults.tsx";
import FakeScreen from "../FakeScreen.tsx";

export default function BottomTabs() {

    //Bottom navigation attributes
    const PassportRoute = () => <PassportCheck/>;
    const BiometricsRoute = () => <FakeScreen/>;
    const PhotoRoute = () => <Text>Photo</Text>;
    const ResultsRoute = () => <SearchResults/>;

    const [index, setIndex] = React.useState(0);
    const routes = [
        {key: 'Passport', title: 'Passport', focusedIcon: 'passport-biometric',},
        {key: 'Biometrics', title: 'Biometrics', focusedIcon: 'fingerprint'},
        {key: 'Photo', title: 'Photo', focusedIcon: 'camera'},
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
